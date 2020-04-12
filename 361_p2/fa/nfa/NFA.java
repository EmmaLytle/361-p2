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
		 
		return null;
	}

	@Override
	public Set<? extends State> getStates() {
		// TODO Auto-generated method stub
		return null; //?? Possible cast??
	}

	@Override
	public Set<? extends State> getFinalStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State getStartState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Character> getABC() {
		// TODO Auto-generated method stub
		return alphabet;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		// TODO Auto-generated method stub
		return null;
	}


}
