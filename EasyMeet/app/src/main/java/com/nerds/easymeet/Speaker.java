package com.nerds.easymeet;

public class Speaker {
    public Long id;
    public Float startTime;
    public Float endTime;

    public Speaker(Long id, Float startTime, Float endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Float getStartTime() {
        return startTime;
    }

    public Float getEndTime() {
        return endTime;
    }

    public Long getId() {
        return id;
    }
}
