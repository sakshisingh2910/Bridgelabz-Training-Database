package com.lms.model;

public class Job {
    private int id;
    private String title;
    private String department;

    public Job() {
    }

    public Job(int id, String title, String department) {
        this.id = id;
        this.title = title;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}