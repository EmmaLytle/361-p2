package fa.nfa;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import fa.State;
import fa.dfa.DFA;

/***************************************
 * CS 361: P2
 * Creates an NFA then converts the NFA to DFA
 * @Authors Emma Lytle, Irene Galca, Shinji Kasai
 ***************************************/
public class NFA implements NFAInterface {

	private HashSet<NFAState> states;
	private NFAState startState;
	private Set<NFAState> finalState;
	private HashSet<Character> alphabet; // Sigma
	private ArrayList<NFAState> numStates;
	
	// Constructor
	public NFA() {

		states = new HashSet<NFAState>();
		alphabet = new HashSet<Character>(); // Sigma
		finalState = new HashSet<NFAState>();
		numStates = new ArrayList<NFAState>();
	}
	
	/**********************************************************
	 * Adds the transition to our table of transition states
	 * @param valueOf the value of the initial state
	 * @param c alphabet symbol
	 * @param valueOf2 value of state transitioned to.
	 ***********************************************************/
	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if (from == null) {
			System.err.println("ERROR: No DFA state exists with name " + fromState);
			System.exit(2);
		} else if (to == null) {
			System.err.println("ERROR: No DFA state exists with name " + toState);
			System.exit(2);
		}

		from.addTrans(onSymb, to);

		if (!alphabet.contains(onSymb) && onSymb != 'e') {

			alphabet.add(onSymb);
		}
	}
	
	/***************************************************************
	 * Adds the epsilon enclosure to our set of transition states
	 * @param s The state connected by epsilon enclosure
	 * @return set of epsilon enclosures
	 ***************************************************************/
	@Override
	public Set<NFAState> eClosure(NFAState s) {
		Set<NFAState> l = new HashSet<>();
		return depthFirstSearch(l, s);
	}

	private Set<NFAState> depthFirstSearch(Set<NFAState> l, NFAState s) {
		Set<NFAState> temp = new HashSet<>();
		Set<NFAState> visited = l;

		temp.add(s);
		if (s.getTrans().containsKey('e') && !visited.contains(s)) {
			visited.add(s);
			for (NFAState n : s.getTo('e')) {
				temp.addAll(depthFirstSearch(visited, n));
			}
		}
		return temp;
	}
	
	/*****************************************************************
	 * gets the DFA state that corresponds with an existing NFA state
	 * @return null
	 *****************************************************************/
	@Override
	public DFA getDFA() {
		// Must implement the breadth first search algorithm.
		Queue<Set<NFAState>> workQueue = new LinkedList<Set<NFAState>>();
		DFA d = new DFA(); // Step 1. https://www.javatpoint.com/automata-conversion-from-nfa-to-dfa
		// d.addStartState("[" + startState.getName() + "]"); //Step 2.
		workQueue.add(eClosure(startState));

		while (!workQueue.isEmpty()) {
			Set<NFAState> currentNode = workQueue.poll(); // current workItem.
			boolean isFinalState = false;

			for (NFAState n : currentNode) {
				if (n.isFinal()) {
					isFinalState = true;
				}
			}

			if (d.getStartState() == null && !isFinalState) {
				d.addStartState(currentNode.toString());
			} else if (d.getStartState() == null && isFinalState) {
				d.addFinalState(currentNode.toString());
				d.addStartState(currentNode.toString());
			}

			for (Character symb : getABC()) {
				Set<NFAState> setOfStateForSymb = new HashSet<NFAState>();
				for (NFAState v : currentNode) {
					if (v.getTrans().get(symb) != null) {
						for (NFAState t : v.getTrans().get(symb)) {
							setOfStateForSymb.addAll(eClosure(t));
						}
					}
				}

				boolean dfaHasState = false;

				for (State s : d.getStates()) {
					if (s.getName().equals(setOfStateForSymb.toString())) {
						dfaHasState = true;
					}
				}
				if (setOfStateForSymb.toString() == "[]") {
					if (!dfaHasState) {
						d.addState("[]");
						workQueue.add(setOfStateForSymb);
					}
					d.addTransition(currentNode.toString(), symb, "[]");
				} else if (!dfaHasState) {
					boolean isFinal = false;
					for (NFAState ns : setOfStateForSymb) {
						if (ns.isFinal()) {
							isFinal = true;
						}
					}
					if (isFinal) {
						workQueue.add(setOfStateForSymb);
						d.addFinalState(setOfStateForSymb.toString());
					} else {
						workQueue.add(setOfStateForSymb);
						d.addState(setOfStateForSymb.toString());
					}
				}
				d.addTransition(currentNode.toString(), symb, setOfStateForSymb.toString());
			}
		}
		return d;
	}
	
	/**********************************************************************
	 * Get to a specified state from a state given an alphabet symbol
	 * @param from the NFAState to move from
	 * @param onSymb alphabet symbol
	 * @return from
	 **********************************************************************/
	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		return from.getTo(onSymb);
	}
	/***************************************************
	 * Should add the final state to our set of states.
	 * @param nextToken
	 ***************************************************/
	@Override
	public void addFinalState(String nextToken) {
		NFAState fs = checkIfExists(nextToken);
		if (fs == null) {
			fs = new NFAState(nextToken, true);
			finalState.add(fs);
			states.add(fs);
		} else {
			System.out.println("Error: this final state already exists.");
		}
	}
	
	/***************************************************
	 * Should add the start state to our set of states
	 * @param startStateName
	 ***************************************************/
	@Override
	public void addStartState(String startStateName) {
		NFAState s = checkIfExists(startStateName);
		if (s == null) {
			s = new NFAState(startStateName);
			startState = s;
			states.add(s);
		} else {
			System.out.println("Error: this start state already exists.");
		}
	}
	
	/********************************************************
	 * Adds existing states in Automata to our set of states
	 * @param nextToken next existing state
	 ********************************************************/
	@Override
	public void addState(String nextToken) {
		NFAState s = checkIfExists(nextToken);
		if (s == null) {
			states.add(new NFAState(nextToken));
		} else {
			System.out.println("Error: this state already exists.");
		}
	}
	
	@Override
	public Set<? extends State> getStates() {
		return states;
	}
	
	@Override
	public Set<? extends State> getFinalStates() {
		return finalState;
	}
	
	@Override
	public State getStartState() {
		return startState;
	}
	
	@Override
	public Set<Character> getABC() {
		return alphabet;
	}

	/**
	 * Check if a state with such name already exists
	 * 
	 * @param name
	 * @return null if no state exist, or DFAState object otherwise.
	 */
	private NFAState checkIfExists(String name) {
		NFAState ret = null;
		for (NFAState s : states) {
			if (s.getName().equals(name)) {
				ret = s;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * builds the transition table, and prints it
	 * @return transitionTable
	 */
	private String transitionTable() {

		String transitionTable = "	";
		char[][] matrix = new char[numStates.size() + 1][alphabet.size() + 1];
		StringBuilder bldmatrix = new StringBuilder();

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i == 0) {
					if (j == 0) {
						matrix[i][j] = ' ';
					} else {
						ArrayList<Character> trans = new ArrayList<>(alphabet);
						matrix[i][j] = trans.get(j - 1);
					}
				} else {
					if (j == 0) {
						matrix[i][j] = numStates.get(i - 1).getName().charAt(0);

					} else {

						ArrayList<Character> trans2 = new ArrayList<>(alphabet);
						matrix[i][j] = ((NFAState) getToState(numStates.get(i - 1), trans2.get(j - 1))).getName()
								.charAt(0);
					}
				}
			}
		}

		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix[x].length; y++) {
				bldmatrix.append("\t" + matrix[x][y]);

			}
			bldmatrix.append("\n");
		}

		transitionTable = bldmatrix.toString();

		return transitionTable;

	}
	
	/**
 		*Q = { [a] [a, b] }
		*Sigma = { 0 1 }
		*delta =
		*0 1
		*[a] [a] [a, b]
		*[a, b] [a] [a, b]
		*q0 = [a]
		*F = { [a, b] }
 		* in which they were instantiated in the NFA.
	 * @return String representation of the NFA
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Q = {  ");
		sb.append(getStartState().getName() + "  ");

		for (State states : states) {
			sb.append(states.getName() + " ");
		}
		for (State finalstates : finalState) {
			sb.append(finalstates.getName() + " ");
		}
		sb.append("}\n");
		sb.append("Sigma = { ");
		for (Character sigmas : alphabet) {
			sb.append(sigmas + " ");
		}
		sb.append("}\n");
		sb.append("delta =\n");
		sb.append(transitionTable());
		sb.append("q0 = " + getStartState().getName() + "\n");
		sb.append("F = { ");
		for (State finalstates : finalState) {
			sb.append(finalstates.getName() + " ");
		}
		sb.append("}");

		return sb.toString();

	}

}
