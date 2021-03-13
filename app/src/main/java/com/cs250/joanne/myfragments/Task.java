package com.cs250.joanne.myfragments;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String name;
    private final int id;
    private static int curId = 0;
    private String dueDate;
    private String completedDate;
    private String category;

    public Task(String title, String dueDate, String category) {
        this.id = curId++;
        this.name = title;
        this.dueDate = dueDate;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }
}
