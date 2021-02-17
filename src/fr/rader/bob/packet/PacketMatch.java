package fr.rader.bob.packet;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PacketMatch {

    private final HashMap<String, Object> matches;

    public PacketMatch(LinkedHashMap<String, Object> data) {
        matches = new HashMap<>();

        for(Map.Entry<String, Object> entry : data.entrySet()) {
            addMatch(entry.getKey(), entry.getValue());
        }
    }

    public void addMatch(String matchValue, Object newMatch) {
        matches.put(matchValue, newMatch);
    }

    public LinkedHashMap<String, Object> getMatch(String matchValue) {
        if(!matches.containsKey(matchValue)) return null;
        return (LinkedHashMap<String, Object>) matches.get(matchValue);
    }

    @Override
    public String toString() {
        return "PacketMatch{" +
                "matches=" + matches +
                '}';
    }
}
