package fa.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import fa.State;
import fa.nfa.NFAState;

/***************************************
 * CS 361: P2
 * Defines what a NFAState is as well as extends State.
 * @Authors Emma Lytle, Irene Galca, Shinji Kasai
 ***************************************/
public class NFAState extends State {
	private HashMap<Character,HashSet<NFAState>> delta;

	private boolean isFinal;//remembers its type

	/******************************
	 * Default constructor
	 * @param name the state name
	 ******************************/
	
	public NFAState(String name){
		initDefault(name);
		isFinal = false;
	}

	 public NFAState(String name, boolean isFinal){
		initDefault(name);
		this.isFinal = isFinal;
	}

	private void initDefault(String name) {
		this.name = name;
		delta = new HashMap<Character,HashSet<NFAState>> ();
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
		HashSet<NFAState>temp = delta.get(onSymb);
		if(temp == null) {
			temp = new HashSet<NFAState>();
		}
		temp.add(toState);
		
		delta.put(onSymb,  temp);
	}

	public HashMap<Character,HashSet<NFAState>> getTrans()
	{
		return delta;
	}
	
	/*************************************************************
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return the new state 
	 *************************************************************/
	public Set<NFAState> getTo(char symb) {

		HashSet<NFAState> ret = delta.get(symb);
		if(ret == null) {
			 System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
			 System.exit(2);
		}
		return delta.get(symb);

	}

	
}


