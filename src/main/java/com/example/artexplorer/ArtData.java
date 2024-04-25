package com.example.artexplorer;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class ArtData {

    private URI accessPoint;
    private HttpURLConnection connection;

    public ArtData() {
    }

    public boolean setAccessPoint(String uri) {

        try {
            accessPoint = new URI(uri);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;

    }

    public URI getAccessPoint() {
        return accessPoint;
    }

    public boolean openConnection() {

        try {
            connection = (HttpURLConnection) accessPoint.toURL().openConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;

    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    public static void main (String [] args) {

        ArtData data = new ArtData();

        System.out.println(data.setAccessPoint("https://api.artic.edu/api/v1/artworks/129884"));
        System.out.println(data.openConnection());

    }

}
