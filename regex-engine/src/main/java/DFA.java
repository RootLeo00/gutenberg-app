import java.util.*;

public class DFA {
    private List<DFAState> table;

    public DFA() {
        this.table = new ArrayList<>();
    }

    public void addState(Set<Integer> state, Map<Character, Set<Integer>> transitions, boolean isStartingState, boolean isAcceptingState) {
        DFAState dfaState = new DFAState(state, transitions, isStartingState, isAcceptingState);
        table.add(dfaState);
    }

    public List<DFAState> getTable() {
        return table;
    }

    public DFA mergeStates(DFA dfa) {
        Map<Set<Integer>,Integer> renamedStates = new HashMap<>(); //key: old id, value: new id
        int id=-1;
        //create the corrisponding table
        for(DFAState state : dfa.getTable()){
            if(!renamedStates.containsKey(state.getState())){
                renamedStates.put(state.getState(),++id);
            }
        }
        //rename the DFA
        for(Integer newId : renamedStates.values()){
            Set<Integer> oldState= dfa.getTable().get(newId).getState();
            dfa.getTable().get(newId).setState(newId);
            for(DFAState state : dfa.getTable()){
                // rename the transition table
                for (Map.Entry<Character, Set<Integer>> entry : state.getTransitions().entrySet()) {
                    if(entry.getValue().equals(oldState)){
                        Set<Integer> newSet = new HashSet<>();
                        newSet.add(newId);
                        entry.setValue(newSet);
                    }
                }
            }
        }

        return dfa;
    }


    public DFA deleteUnreachableStates(DFA dfa) {
        // Unreachable states are the states that are not reachable from the initial state of the DFA, for any input string. These states can be removed.

        Set<Set<Integer>> reachableStates = findReachableStates(dfa);

        // Create a new DFA with only the reachable states
        DFA newDFA = new DFA();
        for (DFAState state : dfa.getTable()) {
            if (reachableStates.contains(state.getState())) {
                newDFA.addState(state);
            }
        }

        return newDFA;
    }

    private Set<Set<Integer>> findReachableStates(DFA dfa) {
        Set<Set<Integer>> reachableStates = new HashSet<>();
        Set<Set<Integer>> visited = new HashSet<>();
        Queue<DFAState> queue = new LinkedList<>();

        // Start with the initial state
        DFAState startingState = dfa.getStartingState();
        reachableStates.add(startingState.getState());
        visited.add(startingState.getState());
        queue.offer(startingState);

        while (!queue.isEmpty()) {
            DFAState currentState = queue.poll();
            for (Character symbol : currentState.getTransitions().keySet()) {
                Set<Integer> nextState = currentState.getTransitions().get(symbol);
                if (!visited.contains(nextState)) {
                    reachableStates.add(nextState);
                    visited.add(nextState);
                    queue.offer(dfa.getDFAStatebyId(nextState));
                }
            }
        }

        return reachableStates;
    }


    public DFAState getStartingState() {
        for (DFAState state : table) {
            if (state.isStartingState()) {
                return state;
            }
        }
        return null;
    }

    public void addState(DFAState dfaState) {
        table.add(dfaState);
    }

public DFAState getDFAStatebyId(Integer id){
    for (DFAState state : table) {
        if (state.getState().contains(id)) {
            return state;
        }
    }
    return null;
}

    public DFAState getDFAStatebyId(Set<Integer> id){
        for (DFAState state : table) {
                if (state.getState().equals(id)) {
                    return state;
                }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("State\tTransitions\tStarting\tAccepting\n");
        sb.append("------------------------------------------------\n");
        for (DFAState state : table) {
            sb.append(state.toString());
            sb.append("\n");
        }
        sb.append("------------------------------------------------\n");
        return sb.toString();
    }
}
