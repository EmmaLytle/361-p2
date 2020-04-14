package fa.nfa;

import java.util.Set;
import fa.nfa.NFAState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;


public class NFA implements NFAInterface{

	private LinkedHashSet<NFAState> states;
	private LinkedHashSet<NFAState> e_close;
	private NFAState startState;  
	private Set<NFAState> finalState; 
	private HashSet<Character> alphabet; // Sigma
	private LinkedHashMap<String, NFAState> trans;// Did we want this to be a map? how do we put things on the map? 
	private ArrayList<NFAState> numStates;
	private ArrayList<NFAState> visited;
	
	//Constructor
	 public NFA() { 
		   
         states = new LinkedHashSet<NFAState>();
         alphabet = new LinkedHashSet<Character>(); // Sigma
        // trans = new LinkedHashMap<String, NFAState>();
         finalState = new LinkedHashSet<NFAState>();
		 numStates = new ArrayList<NFAState>(); 
		 visited = new ArrayList<NFAState>();
		 e_close = new LinkedHashSet<NFAState>();
     }
	
    
	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if(from == null){
			System.err.println("ERROR: No DFA state exists with name " + fromState);
			System.exit(2);
		} else if (to == null){
			System.err.println("ERROR: No DFA state exists with name " + toState);
			System.exit(2);
		}

		from.addTrans(onSymb, to);

		if(!alphabet.contains(onSymb)){
			alphabet.add(onSymb);
		}

	}
	
	@Override
	public Set<NFAState> eClosure(NFAState s){
		//This method will take care of epsilon enclosures.
		//implement using depth first search algorithm.

		visited.add(s);
		HashMap<Character,HashSet<NFAState>> temp = s.getTrans();
		for (Character c : temp.keySet()) {
			if(c == 'e'){
				e_close.add(s);	
			}	
			for (NFAState nfaState : temp.get(c)) {
				if(!visited.contains(nfaState))	{
					eClosure(nfaState);
				}
			}			
		}
		return e_close;
	}


	
	@Override
	public DFA getDFA() {
		// Must implement the breadth first search algorithm.
		ArrayList<NFAState> visited2 = new ArrayList<NFAState>();
		Queue<NFAState> workQueue = new LinkedList<NFAState>();
		NFAState t;

		DFA d = new DFA();  //Step 1.  https://www.javatpoint.com/automata-conversion-from-nfa-to-dfa

		d.addStartState("[" + startState.getName() + "]");  //Step 2.
		workQueue.add(startState);
	
		visited2.add(startState);
		while(workQueue.size() != 0)
		{
			t = workQueue.poll(); //current workItem.
			String fromState = t.getName();
			//Find Transitions from this state.  If set of states not in DFA, Add
			//Step 3.  For each symbol
			HashMap<Character,HashSet<NFAState>> transitions = t.getTrans();
			for (Character symb : transitions.keySet()) {
				
				HashSet<NFAState> delta = transitions.get(symb);
				boolean containsFinalState = false;
				ArrayList<String> cn = new ArrayList<String>();
				for (NFAState ns : delta) {
					if(ns.isFinal()){
						containsFinalState = true;
					}
					cn.add( ns.getName() );
				}
				String toState = "[" + String.join(",", cn )+ "]";
				if(containsFinalState){
					d.addFinalState(toState);
				}else{

					d.addState(toState);
				}	
				
				//d.addTransition(fromState, symb, toState);  //1, a, 2,1
				//d.addTransition(toState, symb, toState);  //2,1, a , 2,1
				
				//Add each state to the workQueue
				for (NFAState nfaState : delta) {
					if(!visited2.contains(nfaState))	{
						workQueue.add(nfaState);
						visited2.add(nfaState);
					}
				}	
				
			}
		}

	
		return d;
	}
	

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		return from.getTo(onSymb);
	}
	
	

	@Override
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
	@Override
	public void addStartState(String startStateName) {
		NFAState s = checkIfExists(startStateName);
		if(s == null) {
			s = new NFAState(startStateName);
			startState = s;
			states.add(s);
		}else {
			System.out.println("Error: this start state already exists.");
		}
	}
	@Override
	public void addState(String nextToken) {
		NFAState s = checkIfExists(nextToken);
		if( s == null){
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
	private String transitionTable(){ 
		
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
		sb.append("Q = {  ");
		sb.append(getStartState().getName() + "  " );
		
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