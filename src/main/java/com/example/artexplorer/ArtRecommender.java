package com.example.artexplorer;

import java.io.IOException;
import java.util.*;

public class ArtRecommender {

    ArrayList<Artwork> works;
    ArtDataProcessor processor;

    public ArtRecommender() {

        processor = new ArtDataProcessor();

        try {
            works = processor.getWorks();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public String [] getNMostSimilarArtworks(UserProfile user, int n) {

        Map<String, Long> viewHistory = user.getViewHistory();
        ArrayList<Artwork> viewedArt = new ArrayList<>();
        Map<String, long[]> featureSpaces = new HashMap<>();
        Map<String, Double> similairitiesToUser = new HashMap<>();
        long [] weightedAverageSpace = new long[5];

        String [] result = new String[n];

        for (HashMap.Entry<String, Long> entry : viewHistory.entrySet()) {
            for (Artwork work : works) {
                if (entry.getKey().equals(work.getArtId())) {
                    viewedArt.add(work);
                }
            }
        }

        featureSpaces = processor.getFeatureSpaces(viewedArt);

        for (HashMap.Entry<String, long[]> space : featureSpaces.entrySet()) {

//            System.out.println(space.getKey() + " duration: " + viewHistory.get(space.getKey()));
//            System.out.println(Arrays.toString(space.getValue()));

            // weight the feauture space

            double weight = (double) (viewHistory.get(space.getKey()) / 5000);
            System.out.println(weight);

            for (int i = 0; i < space.getValue().length; i++) {
                space.getValue()[i] = (long) (space.getValue()[i] * weight);
            }

            //System.out.println("Weighted : " + Arrays.toString(space.getValue()));

            // add to averageWeightedSpace

            for (int i = 0; i < space.getValue().length; i++) {
                weightedAverageSpace[i] += space.getValue()[i];
            }

            //System.out.println("New sum :" + Arrays.toString(weightedAverageSpace));

        }

        // divide to get weighted average space.

        for (int i = 0; i < weightedAverageSpace.length; i++) {

            weightedAverageSpace[i] = weightedAverageSpace[i] / viewHistory.entrySet().size();

        }

        // use cosing similarity to check similarity to other artworks

        for (Artwork work : works) {

            if (!viewedArt.contains(work)) {
                double similairty = processor.cosineSimilarity(weightedAverageSpace, processor.getFeatureSpace(work));
                similairitiesToUser.put(work.getArtId(), similairty);
            }



        }

//        for (HashMap.Entry<String, Double> entry : similairitiesToUser.entrySet()) {
//
//            System.out.println("ArtId :" + entry.getKey());
//            System.out.println("Similiarty to user :" + entry.getValue());
//
//        }

        similairitiesToUser = sortMapByValue(similairitiesToUser);

        int i = 0;
        for (HashMap.Entry<String, Double> entry : similairitiesToUser.entrySet()) {
            if (i == n) {
                break;
            }

            result[i] = entry.getKey();
            i++;
        }

        return result;

    }

    public Map<String, Double> sortMapByValue(Map<String, Double> map) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(map.entrySet());

        // Sort the list based on values
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Construct a new LinkedHashMap to preserve the order
        Map<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


}
