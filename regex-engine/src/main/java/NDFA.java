import java.util.*;

public class NDFA {
    NFAState startState;
    NFAState acceptState; //or "final state"

    private static int nextStateId = 0;
    public NDFA(NFAState startState, NFAState acceptState) {
        this.startState = startState;
        this.acceptState = acceptState;
    }

    public NFAState getStartState() {
        return startState;
    }

    public void setStartState(NFAState startState) {
        this.startState = startState;
    }

    public NFAState getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(NFAState acceptState) {
        this.acceptState = acceptState;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<NFAState> visited = new HashSet<>();

        Queue<NFAState> queue = new LinkedList<>();
        queue.offer(startState);

        while (!queue.isEmpty()) {
            NFAState currentState = queue.poll();
            // Check if the state has been visited
            if (visited.contains(currentState)) {
                continue;
            }
            visited.add(currentState);

            sb.append("State ").append(currentState.getId());
            if (currentState.isAccept()) {
                sb.append(" (Accepting)");
            } else if (currentState.isStart()) {
                sb.append(" (Starting)");
            }
            sb.append("\n");

            // Epsilon transitions
            for (NFAState epsilonState : currentState.getEpsilonTransitions()) {
                sb.append("  -> ε -> ").append(epsilonState.getId()).append("\n");
                if (!visited.contains(epsilonState)) {
                    queue.offer(epsilonState);
                }
            }

            // Transitions on symbols
            for (Map.Entry<Character, Set<NFAState>> entry : currentState.getTransitions().entrySet()) {
                char symbol = entry.getKey();
                Set<NFAState> symbolStates = entry.getValue();
                for (NFAState symbolState : symbolStates) {
                    sb.append("  -> '").append(symbol).append("' -> ").append(symbolState.getId()).append("\n");
                    if (!visited.contains(symbolState)) {
                        queue.offer(symbolState);
                    }
                }
            }
        }
        return sb.toString();
    }

    public NDFA rename(NDFA ndfa) {
        Set<NFAState> visited = new HashSet<>();
        renameStates(ndfa.startState, visited);
        return ndfa;
    }

    public void renameStates(NFAState currentState, Set<NFAState> visited) {
        //Set<NFAState> visited = new HashSet<>();
        Queue<NFAState> queue = new LinkedList<>();

            // Check if the state has already been visited
        if (visited.contains(currentState)) {
            return; // Avoid renaming already visited states
        }
            // Rename the state
            currentState.setId(getNextStateId());

            // Mark the state as visited
            visited.add(currentState);

            // Recursively rename epsilon transitions
            for (NFAState epsilonState : currentState.getEpsilonTransitions()) {
                renameStates(epsilonState, visited);
            }

            // Recursively rename transitions on symbols
            for (Set<NFAState> symbolStates : currentState.getTransitions().values()) {
                for (NFAState symbolState : symbolStates) {
                    renameStates(symbolState, visited);
                }
            }
    }

    private static int getNextStateId() {
        return nextStateId++;
    }
}
