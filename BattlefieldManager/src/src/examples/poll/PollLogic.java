package src.examples.poll;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PollLogic {
    //key = choice value, value = count of choice

    private Map<String, Integer> pollData;

    public PollLogic(String[] choices) {
        initPoll(choices);
    }

    private void initPoll(String[] choices) {
        pollData = new LinkedHashMap<>();
        if (choices != null) {
            for (String choice : choices) {
                pollData.put(choice, 0);
            }
        }
    }

    public void addChoice(String choice) {
        if (choice == null) {
            return;
        }

        if (!pollData.containsKey(choice)) {
            return;
        }
        
        int choiceCount = pollData.get(choice);
        pollData.put(choice, ++choiceCount);
    }

    public Map<String, Integer> getPollData() {
        return Collections.unmodifiableMap(pollData);
    }

}
