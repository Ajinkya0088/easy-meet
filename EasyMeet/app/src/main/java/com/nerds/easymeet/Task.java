package com.nerds.easymeet;

public class Task {
    String meetingId;
    String task;

    public Task(String meetingId, String task) {
        this.meetingId = meetingId;
        this.task = task;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
