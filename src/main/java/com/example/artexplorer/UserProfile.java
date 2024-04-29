package com.example.artexplorer;

import java.io.File;
import java.util.*;

public class UserProfile {

    private Map<String, Long> viewHistory;
    private Map<String, Integer> ratingMap;

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private ArtDataUtil data;

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private ArtDataProcessor processor;

    public UserProfile(Map<String, Long> viewDurationMap, Map<String, Integer> ratingMap) {

        viewHistory = viewDurationMap;
        this.ratingMap = ratingMap;
        viewHistory = sortMapByValue(viewHistory);
        processor = new ArtDataProcessor();
        data = new ArtDataUtil();

//        for (HashMap.Entry<String, Long> entry : viewHistory.entrySet()) {
//            System.out.println("Id: " + entry.getValue());
//            System.out.println("Duration: " + entry.getKey());
//        }

    }

    public Map<String, Long> getViewHistory() {
        return viewHistory;

    }

    public void addToRatingMap(Map<String, Integer> ratingMap) {
        this.ratingMap.putAll(ratingMap);
    }

    public void addToViewHistory(Map<String, Long> viewDurationMap) {
        viewHistory = addMaps(viewHistory, viewDurationMap);
        viewHistory = sortMapByValue(viewHistory);

//        for (HashMap.Entry<String, Long> entry : viewHistory.entrySet()) {
//            System.out.println("Id: " + entry.getValue());
//            System.out.println("Duration: " + entry.getKey());
//        }

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

    public Map<String, Integer> getRatingMap() {
        return ratingMap;
    }

    public void setRatingMap(Map<String, Integer> ratingMap) {
        this.ratingMap = ratingMap;
    }
}
