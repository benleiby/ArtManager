package com.example.artexplorer;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.embed.swing.SwingFXUtils;

public class ImplicitExplorerController {

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public HBox gallery;

    public VBox currentDisplay;
    public VBox currentDisplaySetter;
    public ImageView currentImage;

    private Timeline scrollAnimation;

    @SuppressWarnings("FieldCanBeLocal")
    private String [] artIds;

    @FXML
    public void initialize() {

        Platform.runLater(() -> scrollPane.requestFocus());
        currentDisplay = (VBox) gallery.getChildren().getFirst();
        currentImage = (ImageView) ((StackPane) currentDisplay.getChildren().getFirst()).getChildren().getFirst();

        ArtDataUtil data = new ArtDataUtil();
        artIds = getArtIds();

        for (int i = 0; i < artIds.length; i++) {

            currentDisplaySetter = (VBox) gallery.getChildren().get(i);
            currentImage = (ImageView) ((StackPane) currentDisplaySetter.getChildren().getFirst()).getChildren().getFirst();
            currentImage.setImage(SwingFXUtils.toFXImage(data.getImage(artIds[i]), null));

        }


        scrollPane.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.RIGHT) {
                scrollRight();
            }
            else if (event.getCode() == KeyCode.LEFT) {
                scrollLeft();
            }

        });

    }

    private String [] getArtIds() {

        return new String [] {"111628", "28560", "21023", "137125", "27992", "229393"};

    }

    private void scrollRight() {

        // Calculate the index of the next artwork
        int currentIndex = gallery.getChildren().indexOf(currentDisplay);
        if (currentIndex < gallery.getChildren().size() - 1) {
            currentDisplay = (VBox) gallery.getChildren().get(currentIndex + 1);

            // Scroll the currentArt into view
            scrollIntoView(currentDisplay);

        }
    }

    private void scrollLeft() {

        // Calculate the index of the previous artwork
        int currentIndex = gallery.getChildren().indexOf(currentDisplay);
        if (currentIndex > 0) {
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

    }

}