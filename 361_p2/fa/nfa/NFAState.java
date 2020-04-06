/***************************************
 * CS 361: P2
 * @Authors Emma Lytle, Irene Galca
 ***************************************/

package fa.nfa;

import java.util.HashMap;

import fa.nfa.NFAState;

public class NFAState {
	private HashMap<Character,NFAState> delta;//delta
	private boolean isFinal;//remembers its type
	private String name;

	/******************************
	 * Default constructor
	 * @param name the state name
	 ******************************/
	public NFAState(String name, boolean isFinal){
		initDefault(name);
		isFinal = false;
	}

	private void initDefault(String name) {
		// TODO Auto-generated method stub
		this.name = name;
		delta = new HashMap<Character, NFAState>();
	}
	
	/****************************************************
	 * Accessor for the state type
	 * @return true if final and false otherwise
	 ****************************************************/
	public boolean isFinal(){
		return isFinal;
	}
	

	/*****************************************************
	 * Add the transition from <code> this </code> object
	 * @param onSymb the alphabet symbol
	 * @param toState to DFA state
	 *****************************************************/
	public void addTrans(char onSymb, NFAState toState) {
		delta.put(onSymb,  toState);
	}
	
	/*************************************************************
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return the new state 
	 *************************************************************/
	public NFAState getTo(char symb) {
		NFAState ret = delta.get(symb);
		if(ret == null) {
			 System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
			 System.exit(2);
		}
		return delta.get(symb);
	}
}


