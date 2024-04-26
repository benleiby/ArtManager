package com.example.artexplorer;

import java.util.*;

public class UserProfile {

    // what do i have?
    // viewdurationMap
    // so i have the durations that the user has viewed the artworks
    // therefore, i have 6 starting artworks and all their attributes
    // and i have how long the user looked at each artwork.
    // sort the artworks from highest view to lowest
    // how will i construct user preferences from this data?
    // so i can assign a weight to each artwork

    // then create a system for getting the similarity between two pieces of art
    // i want to recommend the user art that is most similar to the art that is high in viewHistory
    // so i look for similar art starting at the most viewed piece of art and when i run out of similar art
    // i move on to the second most viewed piece of art and so on.

    private Map<String, Long> viewHistory;

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private ArtDataUtil data;


    public UserProfile(Map<String, Long> viewDurationMap) {
        viewHistory = viewDurationMap;
        viewHistory = sortMapByValue(viewHistory);
        data = new ArtDataUtil();
    }

    public void addToViewHistory(Map<String, Long> viewDurationMap) {
        viewHistory = addMaps(viewHistory, viewDurationMap);
        viewHistory = sortMapByValue(viewHistory);
    }

    private Map<String, Long> addMaps(Map<String, Long> map1, Map<String, Long> map2) {
        Map<String, Long> result = new HashMap<>();

        // Add all entries from map1
        for (Map.Entry<String, Long> entry : map1.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            result.put(key, value);
        }

        // Add entries from map2, increasing value if key already exists
        for (Map.Entry<String, Long> entry : map2.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (result.containsKey(key)) {
                Long sum = result.get(key) + value;
                result.put(key, sum);
            } else {
                result.put(key, value);
            }
        }

        return result;
    }

    public Map<String, Long> sortMapByValue(Map<String, Long> map) {
        List<Map.Entry<String, Long>> entryList = new ArrayList<>(map.entrySet());

        // Sort the list based on values
        entryList.sort(Map.Entry.comparingByValue());

        // Construct a new LinkedHashMap to preserve the order
        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
