package com.nerds.easymeet;

import java.util.ArrayList;

public class MeetingModel {
    public String id;
    public String title,description;
    public int timestamp;
    public ArrayList<String> participants;
    public String speech_to_text;
    public String summery;
    public float confidence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public String getSpeech_to_text() {
        return speech_to_text;
    }

    public void setSpeech_to_text(String speech_to_text) {
        this.speech_to_text = speech_to_text;
    }

    public String getSummery() {
        return summery;
    }

    public void setSummery(String summery) {
        this.summery = summery;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }
}
