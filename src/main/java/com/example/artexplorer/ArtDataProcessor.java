package com.example.artexplorer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class ArtDataProcessor {

    public ArtDataProcessor() {

    }

    public double cosineSimilarity(long[] vectorA, long[] vectorB) {
        // Convert long arrays to double arrays
        double[] doubleVectorA = new double[vectorA.length];
        double[] doubleVectorB = new double[vectorB.length];
        for (int i = 0; i < vectorA.length; i++) {
            doubleVectorA[i] = vectorA[i];
            doubleVectorB[i] = vectorB[i];
        }

        // Calculate cosine similarity using double arrays
        return cosineSimilarity(doubleVectorA, doubleVectorB);
    }

    public double cosineSimilarity(double[] vectorA, double[] vectorB) {
        // Calculate the dot product
        double dotProduct = 0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
        }

        // Calculate the magnitudes
        double magnitudeA = calculateMagnitude(vectorA);
        double magnitudeB = calculateMagnitude(vectorB);

        // Calculate the cosine similarity

        // Calculate the cosine similarity
        double similarity = dotProduct / (magnitudeA * magnitudeB);

        // Round the similarity to three decimal places
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(similarity));
    }

    private double calculateMagnitude(double[] vector) {
        double sumOfSquares = 0;
        for (double value : vector) {
            sumOfSquares += value * value;
        }
        return Math.sqrt(sumOfSquares);
    }

    public HashMap<String, long []> getFeatureSpaces(ArrayList<Artwork> works) {

        HashMap<String, long[]> result = new HashMap<>();

        for (Artwork work : works) {
            long [] featureSpace = getFeatureSpace(work);
            result.put(work.getArtId(), featureSpace);
        }

        return result;

    }

    public long [] getFeatureSpace(Artwork work) {

        XXHashFactory factory = XXHashFactory.fastestInstance();
        XXHash32 hash32 = factory.hash32();

        // Initialize hash values
        int artistHash = 0;
        int originHash = 0;
        int typeHash = 0;
        int deptHash = 0;

        // Calculate hash values for non-null fields
        if (work.getArtistId() != null) {
            byte[] artistBytes = work.getArtistId().getBytes();
            artistHash = hash32.hash(artistBytes, 0, artistBytes.length, 0);
        }
        if (work.getOrigin() != null) {
            byte[] originBytes = work.getOrigin().getBytes();
            originHash = hash32.hash(originBytes, 0, originBytes.length, 0);
        }
        if (work.getTypeId() != null) {
            byte[] typeBytes = work.getTypeId().getBytes();
            typeHash = hash32.hash(typeBytes, 0, typeBytes.length, 0);
        }
        if (work.getDeptId() != null) {
            byte[] deptBytes = work.getDeptId().getBytes();
            deptHash = hash32.hash(deptBytes, 0, deptBytes.length, 0);
        }

        // Handle null value for date end
        long dateEndValue = 0;
        if (work.getDateEnd() != null) {
            try {
                dateEndValue = Long.parseLong(work.getDateEnd());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }

        return new long[]{artistHash, originHash, dateEndValue, typeHash, deptHash};
    }

    public ArrayList<Artwork> getWorks() throws IOException {

        ArrayList<Artwork> works = new ArrayList<Artwork>();

        File objectFile = new File("src/main/resources/com/example/artexplorer/POJOArtworks");

        if (objectFile.isFile()) {

            Scanner reader = new Scanner(objectFile);

            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                String [] tokens = line.split(",");
                System.out.println(Arrays.toString(tokens));

                Artwork work = new Artwork(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                works.add(work);
            }

            return works;

        }



        File directory = new File("C:\\Users\\sirbe\\Desktop\\Project Data\\artic-api-data\\json\\artworks");

        if (!directory.isDirectory()) {
            System.err.println("Invalid directory path.");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            System.err.println("No files found in the directory.");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        assert files != null;
        for (File file : files) {

            if (file.getName().endsWith(".json")) {

                if (mapper.readTree(file).get("is_boosted") == null || !mapper.readTree(file).get("is_boosted").asBoolean()) {
                    System.out.println("skipping " + file.getName());
                } else {
                    System.out.println("adding-----------------------------------------------------------------------------" + file.getName());
                    works.add(mapper.readValue(file, Artwork.class));
                }

            }

        }

        for (Artwork work : works) {
            System.out.println(work);
        }
        System.out.println("elements retrieved: " + works.size());

        return works;

    }

}
