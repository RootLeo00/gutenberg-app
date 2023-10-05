import java.util.Map;

public class RegExToNFAConverter {
    private static int stateIdCounter = 0;

    private NDFA right_ndfa;
    private NDFA left_ndfa;

    public NDFA convert(RegExTree regexTree) {
        return convertLeavesFromLastToRoot(regexTree, left_ndfa, right_ndfa);
    }


    private NDFA convertLeavesFromLastToRoot(RegExTree regexTree, NDFA left_ndfa, NDFA right_ndfa) {
        if (regexTree == null) return null; //TODO throw exception
        /*System.out.print(regexTree.toString() + " ");
        System.out.print(regexTree.rootToString()+ " ");
        System.out.print(regexTree.subTrees.size() + "\n");*/

        // Check if it's a leaf node (no children)
        if (regexTree.subTrees.isEmpty()) {
            //System.out.print("leaf: "+ regexTree.toString() + " ");
            //convertSubtree(regexTree, this.startState, this.acceptState);
            return symbolOperation(regexTree);
        }
        // Traverse the right subtree (last leaf)
        if(regexTree.subTrees.size() > 1) {
            right_ndfa=convertLeavesFromLastToRoot(regexTree.subTrees.get(1), left_ndfa, right_ndfa);
        }

        // Traverse the left subtree (move up towards the root)
        left_ndfa=convertLeavesFromLastToRoot(regexTree.subTrees.get(0), left_ndfa,left_ndfa);

        //finally convert the root
        return convertSubtree(regexTree, left_ndfa, right_ndfa);
    }

    private static void epsilonTransition(NFAState fromState, NFAState toState) {
        fromState.getEpsilonTransitions().add(toState);
    }
    private void transition(NFAState fromState, char symbol, NFAState toState) {
        fromState.addTransition(symbol, toState);
    }

    private NDFA alternationOperation(NDFA r1, NDFA r2) {
        //System.out.println("alternationOperation on " + r1 + " and " + r2 + "\n");
        // taking the automata for R1 and R2 and adding two new states, one the start
        //state and the other the accepting state.
        NFAState newStartState = new NFAState(stateIdCounter++, false, true);
        NFAState newAcceptState = new NFAState(stateIdCounter++, true, false);
        // The new start state has an e-transition to the start states of the automata for R1 and R2.
        epsilonTransition(newStartState, r1.getStartState());
        epsilonTransition(newStartState, r2.getStartState());
        // The accepting states of the automata for R1 and R2 have e-transitions to the new accepting state.
        epsilonTransition(r1.getAcceptState(), newAcceptState);
        epsilonTransition(r2.getAcceptState(), newAcceptState);
        // Remove old start/accept state
        r1.getAcceptState().setAccept(false);
        r2.getAcceptState().setAccept(false);
        r1.getStartState().setStart(false);
        r2.getStartState().setStart(false);
        return new NDFA(newStartState, newAcceptState);
    }

    private static NDFA concatenationOperation(NDFA r1, NDFA r2) {
        //System.out.println("concatenationOperation on " + r1.toString() + " and " + r2.toString() + "\n");
        // We add an e-transition from the accepting state of the automaton for R1 to the start state of the automaton for R2.
        epsilonTransition(r1.getAcceptState(), r2.getStartState());
        // Remove old start/accept state
        r1.getAcceptState().setAccept(false);
        r2.getStartState().setStart(false);
        return new NDFA(r1.getStartState(), r2.getAcceptState());
    }

    private static NDFA starOperation(NDFA r1) {
        //We add to the automaton for R1 a new start and accepting state.
        NFAState startState = new NFAState(stateIdCounter++, false, true);
        NFAState acceptState = new NFAState(stateIdCounter++, true, false);
        //The start state has an e-transition to the accepting state and to the start state of the automaton for R1.
        epsilonTransition(startState, acceptState);
        epsilonTransition(startState, r1.getStartState());
        //The accepting state of the automaton for R1 is given an e-transition back to its start state, and one to the accepting state of the automaton for R1.
        epsilonTransition(r1.getAcceptState(), r1.getStartState());
        epsilonTransition(r1.getAcceptState(), acceptState);
        // Remove old start/accept state
        r1.getAcceptState().setAccept(false);
        r1.getStartState().setStart(false);
        return new NDFA(startState, acceptState);
    }

    private static NDFA plusOperation(NDFA r1) {
        //We add to the automaton for R1 a new start and accepting state.
        NFAState startState = new NFAState(stateIdCounter++, false, true);
        NFAState acceptState = new NFAState(stateIdCounter++, true, false);
        //The start state has an e-transition to the accepting state and to the start state of the automaton for R1.
        //epsilonTransition(startState, acceptState); //the only difference with star operation: we don't add the transition to the accept state, because we want to force at least one repetition
        epsilonTransition(startState, r1.getStartState());
        //The accepting state of the automaton for R1 is given an e-transition back to its start state, and one to the accepting state of the automaton for R1.
        epsilonTransition(r1.getAcceptState(), r1.getStartState());
        epsilonTransition(r1.getAcceptState(), acceptState);
        // Remove old start/accept state
        r1.getAcceptState().setAccept(false);
        r1.getStartState().setStart(false);
        return new NDFA(startState, acceptState);
    }


    private NDFA symbolOperation(RegExTree regexTree) {
        //System.out.println("symbolOperation with symbol "+regexTree.rootToString().charAt(0)+"\n");
        //we have to create a new start and accept state and connect them with x-transition
        NFAState startState = new NFAState(stateIdCounter++, false, true);
        NFAState acceptState = new NFAState(stateIdCounter++, true,     false);
        // start state and accept state get connected by x-transition (where x is the symbol in input)
        transition(startState, regexTree.rootToString().charAt(0), acceptState);
        return new NDFA(startState, acceptState);
    }


    private NDFA convertSubtree(RegExTree regexTree, NDFA r1, NDFA r2) {
        //print the root
        //System.out.print("\nroot given: "+ regexTree.root + " ");
        if (regexTree.root == RegExTokenType.CONCAT.getValue()) {
            return concatenationOperation(r1, r2);
        } else if (regexTree.root == RegExTokenType.ALTERN.getValue()) {
            return alternationOperation(r1, r2);
        } else if (regexTree.root == RegExTokenType.STAR.getValue()) {
            return starOperation(r1);
        } else if (regexTree.root == RegExTokenType.PLUS.getValue()) {
            return plusOperation(r1);
        } else {
            return symbolOperation(regexTree);
        }

    }

}
