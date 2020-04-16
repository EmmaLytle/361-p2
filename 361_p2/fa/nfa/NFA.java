package fa.nfa;

import java.util.Set;
import fa.nfa.NFAState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

public class NFA implements NFAInterface {

	private LinkedHashSet<NFAState> states;
	private LinkedHashSet<NFAState> e_close;
	private NFAState startState;
	private Set<NFAState> finalState;
	private HashSet<Character> alphabet;
	private LinkedHashMap<String, NFAState> trans;
	private ArrayList<NFAState> numStates;

	/**************************
	 * Constructor
	 **************************/
	public NFA() {

		states = new LinkedHashSet<NFAState>();
		alphabet = new LinkedHashSet<Character>();
		trans = new LinkedHashMap<String, NFAState>();
		finalState = new LinkedHashSet<NFAState>();
		numStates = new ArrayList<NFAState>();
	}

	/***************************************************
	 * Should add the final state to our set of states.
	 * 
	 * @param nextToken
	 ***************************************************/
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
	 * 
	 * @param startStateName
	 ***************************************************/
	public void addStartState(String startStateName) {

		NFAState s = checkIfExists(startStateName);
		if (s == null) {
			s = new NFAState(startStateName, true);
			startState = s;
			states.add(s);
		} else {
			System.out.println("Error: this start state already exists.");
		}
	}

	/********************************************************
	 * Adds existing states in Automata to our set of states
	 * 
	 * @param nextToken next existing state
	 ********************************************************/
	public void addState(String nextToken) {

		NFAState s = checkIfExists(nextToken);
		if (s == null) {
			states.add(new NFAState(nextToken, true));
		} else {
			System.out.println("Error: this state already exists.");
		}
	}

	/**********************************************************
	 * Adds the transition to our table of transition states
	 * 
	 * @param valueOf  the value of the initial state
	 * @param c        alphabet symbol
	 * @param valueOf2 value of state transitioned to.
	 ***********************************************************/
	public void addTransition(String valueOf, char c, String valueOf2) {
		// TODO
		alphabet.add(c);
		// Hash put Tuple
		// trans.put(new NFAState(valueOf, true), c), new NFAState(valueOf2, true);
	}

	/******************************************************************
	 * Check if a state with such name already exists
	 * 
	 * @param name
	 * @return null if no state exist, or DFAState object otherwise.
	 ******************************************************************/
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

	/***************************************************************
	 * Adds the epsilon enclosure to our set of transition states
	 * 
	 * @param s The state connected by epsilon enclosure
	 * @return set of epsilon enclosures
	 ***************************************************************/
	public Set<NFAState> eClosure(NFAState s) {
		// This method will take care of epsilon enclosures.
		// implement using depth first search algorithm.

		trans.put(s.getName(), s);
		// trans.put(key, s);//what is key?
		for (int i = 0; i <= states.size(); i++) {

			if (!e_close.contains(s)) {
				trans.put(s.getName(), s);
			}
		}

		return e_close;

	}

	/*****************************************************************
	 * gets the DFA state that corresponds with an existing NFA state
	 * 
	 * @return null
	 *****************************************************************/
	public DFA getDFA() {
		// TODO Auto-generated method stub
		// Must implement the breadth first search algorithm.
		Queue<Set<NFAState>> queue = new LinkedList<Set<NFAState>>();
		DFAState temp = null;
		NFAState nfa_temp = null;

		while (!queue.isEmpty()) {
			queue.poll();

			for (int i = 0; i < queue.size(); i++) {
				if (temp.equals(nfa_temp)) {
					eClosure(nfa_temp);
				}
				queue.add((Set<NFAState>) nfa_temp);
			}

			// return DFA;
			// return null;
		}
		return null;
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

	/**********************************************************************
	 * Get to a specified state from a state given an alphabet symbol
	 * 
	 * @param from   the NFAState to move from
	 * @param onSymb alphabet symbol
	 * @return from
	 **********************************************************************/
	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// TODO Auto-generated method stub

		// Idea from the last project.
		if (alphabet.contains(onSymb)) {
			return (Set<NFAState>) trans.get(from);
		} else {
			return (Set<NFAState>) from;
		}

		// return toState;
	}

	/******************************************************************
	 * Builds the transition table with all the existing transitions
	 * 
	 * @return transitionTable
	 ******************************************************************/
	public String transitionTable() {

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

	/***********************************************************
	 * toString method to print out the output of our automata
	 * 
	 * @return sb our string builder to build the table
	 ***********************************************************/
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Q = { ");
		sb.append(getStartState().getName() + " ");

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
