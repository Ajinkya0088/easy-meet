package com.nerds.easymeet.data;

import java.io.Serializable;

public class Task implements Serializable {
    public String meetingId;
    public String task;
    public String assigner_id;
    public boolean status = false;

    public Task() {

    }

    public Task(String meetingId, String task, String assigner_id, boolean status) {
        this.meetingId = meetingId;
        this.task = task;
        this.assigner_id = assigner_id;
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAssigner_id() {
        return assigner_id;
    }

    public void setAssigner_id(String assigner_id) {
        this.assigner_id = assigner_id;
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
