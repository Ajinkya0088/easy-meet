package com.nerds.easymeet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MeetingModel implements Serializable {
    public String id;
    public String title, description;
    public long timestamp;
    public ArrayList<String> participants;
    public String speech_to_text;
    public Map<String, String> speaker_labels;
    public String summery;
    public double sentiment;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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

    public double getSentiment() {
        return sentiment;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public Map<String, String> getSpeaker_labels() {
        return speaker_labels;
    }

    public void setSpeaker_labels(Map<String, String> speaker_labels) {
        this.speaker_labels = speaker_labels;
    }
}
