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

    public DFA rename(DFA dfa) {
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
