package fa.nfa;

import java.util.Set;
import fa.nfa.NFAState;
import java.util.HashSet;
import java.util.Queue;
import fa.State;
import fa.dfa.DFA;


public class NFA implements NFAInterface{

	private HashSet<NFAState> states;
	private NFAState start;
	private HashSet<NFAState> end;
	private HashSet<Character> alphabet;
	private HashSet<NFAState> trans;
	private HashSet<NFAState> e_close;

	

	private NFAState startState; // added sk
	private Set<NFAState> finalState; //added sk
	
	 public NFA() { // added sk
		   
         states = new LinkedHashSet<NFAState>();
         sigma = new LinkedHashSet<Character>();
         transitions = new LinkedHashMap<String, NFAState>();
         finalStates = new LinkedHashSet<NFAState>();
         numstates = new ArrayList<NFAState>();
     }
	 
	public void addFinalState(String nextToken) {
		// TODO Auto-generated method stub
		NFAState fs = checkIfExists(nextToken);
		if(fs == null) {
			fs = new NFAState(nextToken, true);
			end.add(fs);
			states.add(fs);
		}else {
			System.out.println("Error: this final state already exists.");
		}
	}

	public void addStartState(String startStateName) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(startStateName);
		if(s == null) {
			s = new NFAState(startStateName, true);
			start = s;
			states.add(s);
		}else {
			System.out.println("Error: this start state already exists.");
		}
	}

	public void addState(String nextToken) {
		// TODO Auto-generated method stub
		NFAState s = checkIfExists(nextToken);
		if( s == null){
			states.add(new NFAState(nextToken, true));
		} else {
			System.out.println("Error: this state already exists.");
		}
	}

		
	
	public void addTransition(String valueOf, char c, String valueOf2) {
		// TODO Auto-generated method stub
		alphabet.add(c);
		//Hash put Tuple
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
		trans.add(s);
		for (int i = 0; i <= states.size(); i++) {
		
			if(!e_close.contains(s)) {
				trans.add(s);
			}
		}
		
		return e_close;

	}
	public DFA getDFA() {
		// TODO Auto-generated method stub
		
		 // Must implement the breadth first search algorithm.
		 
		return DFA; //sk
	}

	@Override
	public Set<? extends State> getStates() {
		// TODO Auto-generated method stub
		return state; //?? Possible cast?? //sk
	}

	@Override
	public Set<? extends State> getFinalStates() {
		// TODO Auto-generated method stub
		return finalState; //sk
	}

	@Override
	public State getStartState() {
		// TODO Auto-generated method stub
		return startState; ; //sk
	}

	@Override
	public Set<Character> getABC() {
		// TODO Auto-generated method stub
		return alphabet;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// TODO Auto-generated method stub
		return toState; //sk
	}
public String transitionTable(){ added sk
		
		String transitionTable = "	";
		char[][] matrix = new char[numstates.size()+1][sigma.size()+1];
		StringBuilder bldmatrix = new StringBuilder();
		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if (i == 0) {
					if (j == 0) {
					matrix[i][j] = ' ';
					} else {
						ArrayList<Character> trans = new ArrayList<>(sigma);
							matrix[i][j] = trans.get(j-1);
					}
				} else {
					if (j == 0) {
						matrix[i][j] =  numstates.get(i-1).getName().charAt(0);
						
					} else {

						ArrayList<Character> trans2 = new ArrayList<>(sigma);
						matrix[i][j] = getToState(numstates.get(i-1), trans2.get(j-1)).getName().charAt(0);
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
		for (State finalstates: finalStates) {
			sb.append(finalstates.getName() + " ");
		}
		sb.append("}\n");
		sb.append("Sigma = { ");
		for (Character sigmas: sigma) {
			sb.append(sigmas + " ");
		}
		sb.append("}\n");
		sb.append("delta =\n");
		sb.append(transitionTable());
		sb.append("q0 = " + getStartState().getName()+"\n");
		sb.append("F = { ");
		for (State finalstates: finalStates) {
			sb.append(finalstates.getName() + " ");
		}
		sb.append("}");
		
        return sb.toString();
        
	}

}
