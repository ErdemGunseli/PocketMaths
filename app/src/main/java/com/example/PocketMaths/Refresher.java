package com.example.PocketMaths;

/**
 * The instances of this class contain the content for refreshers.
 * Refreshers are a quick summary of the topic that is needed to solve a given question.
 */
public class Refresher {

    private int id;
    private String topic;
    private int imageId;
    private boolean shown;

    /**
     * Constructor for Refresher
     *
     * @param id      The ID of the refresher.
     * @param topic   The topic of the refresher.
     * @param imageId The image ID of the image of the refresher.
     */
    public Refresher(int id, String topic, int imageId) {
        this.id = id;
        this.topic = topic;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
