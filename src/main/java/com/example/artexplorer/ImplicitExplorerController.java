package com.example.artexplorer;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.embed.swing.SwingFXUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ImplicitExplorerController {

    @FXML
    public ScrollPane scrollPane;
    @FXML
    public HBox gallery;
    @FXML
    public Button newGalleryButton;

    public VBox currentDisplay;
    public VBox currentDisplaySetter;
    public ImageView currentImage;

    private Timeline scrollAnimation;

    @FXML
    public TextField ratingBox;

    @SuppressWarnings({"FieldMayBeFinal"})
    private Map<String, Long> viewDurationMap = new HashMap<>();
    @SuppressWarnings({"FieldMayBeFinal"})
    private Map<String, Integer> ratingMap = new HashMap<>();
    private Long startTime;

    @SuppressWarnings("FieldCanBeLocal")
    private String [] artIds;

    private UserProfile user;

    private ArtRecommender recommender;


    @FXML
    public void initialize() {


        Platform.runLater(() -> scrollPane.requestFocus());
        currentDisplay = (VBox) gallery.getChildren().getFirst();
        currentImage = (ImageView) ((StackPane) currentDisplay.getChildren().getFirst()).getChildren().getFirst();

        ArtDataUtil data = new ArtDataUtil();
        artIds = getArtIds();

        recommender = new ArtRecommender();

        for (int i = 0; i < artIds.length; i++) {

            currentDisplaySetter = (VBox) gallery.getChildren().get(i);
            currentImage = (ImageView) ((StackPane) currentDisplaySetter.getChildren().getFirst()).getChildren().getFirst();
            currentImage.setImage(SwingFXUtils.toFXImage(data.getImage(artIds[i]), null));

        }

        scrollPane.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.RIGHT) {
                scrollRight();
            } else if (event.getCode() == KeyCode.LEFT) {
                scrollLeft();
            } else if (event.getCode() == KeyCode.UP) {

                if (ratingBox.getText().startsWith("?")) {
                    ratingBox.setText("1 / 5");
                }
                else if (!ratingBox.getText().startsWith("5")) {
                    ratingBox.setText(Character.getNumericValue(ratingBox.getText().charAt(0)) + 1 + " / 5");
                }

            } else if (event.getCode() == KeyCode.DOWN) {

                if (ratingBox.getText().startsWith("1")) {
                    ratingBox.setText("? / 5");
                }
                else if (!ratingBox.getText().startsWith("?")) {
                    ratingBox.setText(Character.getNumericValue(ratingBox.getText().charAt(0)) - 1  + " / 5");
                }

            }

        });

        startTimer();


    }

    public void onNewGalleryButtonClick() {

        ArtDataUtil data = new ArtDataUtil();

        // end the last timer
        endTimer();

        // update user view history
        if (user == null) {
            user = new UserProfile(viewDurationMap, ratingMap);
        } else {
            user.addToViewHistory(viewDurationMap);
            user.addToRatingMap(ratingMap);
        }

        ratingMap.clear();
        viewDurationMap.clear();

        // swtich scene and determine which artworks to recommend next.

        artIds = recommender.getNMostSimilarArtworks(user, 6);
        System.out.println(Arrays.toString(artIds));

        currentImage = (ImageView) ((StackPane) currentDisplay.getChildren().getFirst()).getChildren().getFirst();

        for (int i = 0; i < artIds.length; i++) {

            currentDisplaySetter = (VBox) gallery.getChildren().get(i);
            currentImage = (ImageView) ((StackPane) currentDisplaySetter.getChildren().getFirst()).getChildren().getFirst();

            try {
                currentImage.setImage(SwingFXUtils.toFXImage(data.getImage(artIds[i]), null));
            } catch (NullPointerException ignored) {

            }

        }

        for (HashMap.Entry<String, Integer> entry : ratingMap.entrySet()) {

            System.out.println("Id: " + entry.getKey());
            System.out.println("Rating: " + entry.getValue());

        }


        currentDisplay = (VBox) gallery.getChildren().getFirst();
        scrollPane.setHvalue(0);
        scrollPane.requestFocus();

    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
    }

    private void endTimer() {

        long duration = System.currentTimeMillis() - startTime;
        String displayId = currentDisplay.getId();
        String artId = artIds[Character.getNumericValue(displayId.charAt(displayId.length() - 1))];

        viewDurationMap.putIfAbsent(artId, duration);

        if (viewDurationMap.containsKey(artId)) {
            long sum = viewDurationMap.get(artId) + duration;
            viewDurationMap.put(artId, sum);
        }

    }

    private String [] getArtIds() {

        //return new String [] {"129884", "28560", "21023", "137125", "27992", "229393"};
        return new String [] {"99539", "6596", "21023", "137125", "27992", "229393"};

    }

    private void scrollRight() {

        // Calculate the index of the next artwork
        int currentIndex = gallery.getChildren().indexOf(currentDisplay);
        if (currentIndex < gallery.getChildren().size() - 1) {

            String displayId = currentDisplay.getId();
            String artId = artIds[Character.getNumericValue(displayId.charAt(displayId.length() - 1))];
            ratingMap.put(artId, Character.getNumericValue(ratingBox.getText().charAt(0)));

            if (startTime != null) endTimer();

            currentDisplay = (VBox) gallery.getChildren().get(currentIndex + 1);

            // Scroll the currentArt into view
            scrollIntoView(currentDisplay);

        }
    }

    private void scrollLeft() {

        // Calculate the index of the previous artwork
        int currentIndex = gallery.getChildren().indexOf(currentDisplay);
        if (currentIndex > 0) {

            String displayId = currentDisplay.getId();
            String artId = artIds[Character.getNumericValue(displayId.charAt(displayId.length() - 1))];
            ratingMap.put(artId, Character.getNumericValue(ratingBox.getText().charAt(0)));


            if (startTime != null) endTimer();

            currentDisplay = (VBox) gallery.getChildren().get(currentIndex - 1);

            // Scroll the currentArt into view
            scrollIntoView(currentDisplay);

        }
    }

    private void scrollIntoView(Node node) {

        double viewportWidth = scrollPane.getWidth();
        double contentWidth = gallery.getBoundsInLocal().getWidth();
        double nodeX = node.getBoundsInParent().getMinX();

        // Calculate the target scroll position
        double targetX = nodeX - scrollPane.getViewportBounds().getMinX();
        double scrollX = Math.max(0.0, Math.min(1.0, targetX / (contentWidth - viewportWidth)));

        // Create a timeline for smooth scrolling animation
        if (scrollAnimation != null && scrollAnimation.getStatus() == Timeline.Status.RUNNING) {
            scrollAnimation.stop();
        }

        scrollAnimation = new Timeline();
        scrollAnimation.getKeyFrames().add(
                new KeyFrame(Duration.millis(750), // Animation duration
                        new KeyValue(scrollPane.hvalueProperty(), scrollX)
                )
        );
        scrollAnimation.play();

        // Set the horizontal scroll position
        scrollPane.setHvalue(scrollX);
        startTimer();
        ratingBox.setText("? / 5");

    }

}