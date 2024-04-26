package com.example.artexplorer;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.image.BufferedImage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArtDataUtil {

    private URI accessPoint;
    HttpURLConnection connection;
    InputStreamReader reader;

    public ArtDataUtil() {
    }

    public void setAccessPoint(String url) throws URISyntaxException {
        accessPoint = new URI(url);
    }

    public URI getAccessPoint() {
        return accessPoint;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public void openConnection() throws IOException {
        connection = (HttpURLConnection) accessPoint.toURL().openConnection();
        connection.setRequestMethod("GET");
    }

    public void closeConnection() {
        connection.disconnect();
    }

    public InputStreamReader getInputStreamReader() throws IOException {
        reader = new InputStreamReader(connection.getInputStream());
        return reader;
    }

    public BufferedImage getImage(String artID) {

        String url = "https://api.artic.edu/api/v1/artworks/" + artID + "?fields=id,title,image_id";
        try {

            setAccessPoint(url);
            openConnection();

            BufferedReader reader = new BufferedReader(getInputStreamReader());

            // Assuming 'jsonString' is the JSON string read from the input stream
            String jsonString = reader.readLine();

            closeConnection();

            // Create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse JSON string into JsonNode
            @SuppressWarnings("VulnerableCodeUsages")
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Access specific fields
            String iiifUrl = jsonNode.get("config").get("iiif_url").asText();
            String imageId = jsonNode.get("data").get("image_id").asText();

            setAccessPoint(iiifUrl + "/" + imageId + "/full/843,/0/default.jpg");
            openConnection();

            // Read the image data
            InputStream inputStream = getConnection().getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            closeConnection();
            return image;


        } catch (Exception e) { System.out.println(e.getMessage()); }

        return null;
    }

    public static void main (String [] args) {

        ArtDataUtil data = new ArtDataUtil();

        try{

            data.setAccessPoint("https://api.artic.edu/api/v1/agents/search?query[term][is_artist]=true");
            data.openConnection();

            BufferedReader reader = new BufferedReader(data.getInputStreamReader());

            String line = reader.readLine();
            System.out.println(line);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
