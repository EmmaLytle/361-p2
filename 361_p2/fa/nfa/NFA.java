package fa.nfa;

import java.util.Set;
import fa.nfa.NFAState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Queue;
import fa.State;
import fa.dfa.DFA;


public class NFA implements NFAInterface{

	private LinkedHashSet<NFAState> states;
	private LinkedHashSet<NFAState> e_close;
	private NFAState startState;  
	private Set<NFAState> finalState; 
	private HashSet<Character> alphabet; // Sigma
	private LinkedHashMap<String, NFAState> trans;// Did we want this to be a map? how do we put things on the map? 
	private ArrayList<NFAState> numStates;
	
	//Constructor
	 public NFA() { 
		   
         states = new LinkedHashSet<NFAState>();
         alphabet = new LinkedHashSet<Character>(); // Sigma
         trans = new LinkedHashMap<String, NFAState>();
         finalState = new LinkedHashSet<NFAState>();
         numStates = new ArrayList<NFAState>(); 
     }
	 
	public void addFinalState(String nextToken) {
		
		NFAState fs = checkIfExists(nextToken);
		if(fs == null) {
			fs = new NFAState(nextToken, true);
			finalState.add(fs);
			states.add(fs);
		}else {
			System.out.println("Error: this final state already exists.");
		}
	}

	public void addStartState(String startStateName) {
	
		NFAState s = checkIfExists(startStateName);
		if(s == null) {
			s = new NFAState(startStateName, true);
			startState = s;
			states.add(s);
		}else {
			System.out.println("Error: this start state already exists.");
		}
	}

	public void addState(String nextToken) {
		
		NFAState s = checkIfExists(nextToken);
		if( s == null){
			states.add(new NFAState(nextToken, true));
		} else {
			System.out.println("Error: this state already exists.");
		}
	}

	
	public void addTransition(String valueOf, char c, String valueOf2) {
		//TODO 
		alphabet.add(c);
		//Hash put Tuple
		//trans.put(new NFAState(valueOf, true), c), new NFAState(valueOf2, true);
	}
	
	/**
	 * Check if a state with such name already exists
	 * @param name
	 * @return null if no state exist, or DFAState object otherwise.
	 */
	private NFAState checkIfExists(String name){
		NFAState ret = null;
		for(NFAState s : states){
			if(s.getName().equals(name)){
				ret = s;
				break;
			}
		}
		return ret;
	}
		
	public Set<NFAState> eClosure(NFAState s){
		//This method will take care of epsilon enclosures.
		//implement using depth first search algorithm.

		trans.add(s); // You can't add you have to put because it is a map 
		//trans.put(key, s);//what is key? 
		for (int i = 0; i <= states.size(); i++) {
		
			if(!e_close.contains(s)) {
				trans.add(s);// Same comment as above you need to put not add 
			}
		}
		
		return e_close;

	}
	public DFA getDFA() {
		// TODO Auto-generated method stub
		
		 // Must implement the breadth first search algorithm.
		return null;
		//return DFA; //sk
	}

	@Override
	public Set<? extends State> getStates() {
		return states; 
	}

	@Override
	public Set<? extends State> getFinalStates() {
		return finalState; //sk
	}

	@Override
	public State getStartState() {
		return startState;  //sk
	}

	@Override
	public Set<Character> getABC() {
		return alphabet;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// TODO Auto-generated method stub
		return toState; //sk
	}

	public String transitionTable(){ 
		
		String transitionTable = "	";
		char[][] matrix = new char[numStates.size()+1][alphabet.size()+1];
		StringBuilder bldmatrix = new StringBuilder();
		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if (i == 0) {
					if (j == 0) {
					matrix[i][j] = ' ';
					} else {
						ArrayList<Character> trans = new ArrayList<>(alphabet);
							matrix[i][j] = trans.get(j-1);
					}
				} else {
					if (j == 0) {
						matrix[i][j] =  numStates.get(i-1).getName().charAt(0);
						
					} else {

						ArrayList<Character> trans2 = new ArrayList<>(alphabet);
						matrix[i][j] = ((NFAState) getToState(numStates.get(i - 1), trans2.get(j - 1))).getName()
								.charAt(0);
					}
				}
			}
		}
		
		for (int x = 0; x < matrix.length; x++) {
			for(int y = 0; y < matrix[x].length; y++) {
				bldmatrix.append("\t" + matrix[x][y]);
				
			}
			bldmatrix.append("\n");
		}
		
		transitionTable = bldmatrix.toString();
		
		return transitionTable;
		
	}
	
	public String toString() //sk
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Q = { ");
		sb.append(getStartState().getName() + " ");
		
		for (State states: states) {
			sb.append(states.getName() + " ");
		}
		for (State finalstates: finalState) {
			sb.append(finalstates.getName() + " ");
		}
		sb.append("}\n");
		sb.append("Sigma = { ");
		for (Character sigmas: alphabet) {
			sb.append(sigmas + " ");
		}
		sb.append("}\n");
		sb.append("delta =\n");
		sb.append(transitionTable());
		sb.append("q0 = " + getStartState().getName()+"\n");
		sb.append("F = { ");
		for (State finalstates: finalState) {
			sb.append(finalstates.getName() + " ");
		}
		sb.append("}");
		
        return sb.toString();
        
	}
 
}
