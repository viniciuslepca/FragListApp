package com.cs250.joanne.myfragments;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private String name;
    private final int id;
    private static int curId = 0;
    private String dueDate;
    private String completedDate;
    private String category;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public Task(String title, String dueDate, String category) {
        this.id = curId++;
        this.name = title;
        this.dueDate = dueDate;
        if (category.equals("")) {
            this.category = "misc";
        } else {
            this.category = category;
        }
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
        return this.dueDate;
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

    public Date getDueDateFormatted() {
        if (this.dueDate == null) return null;
        try {
            return formatter.parse(this.dueDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date getCompletedDateFormatted() {
        if (this.completedDate == null) return null;
        try {
            return formatter.parse(this.dueDate);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public int compareTo(Task other) {
        Date thisDueDate = this.getDueDateFormatted();
        Date otherDueDate = other.getDueDateFormatted();

        return thisDueDate.compareTo(otherDueDate);
    }

    @Override
    public String toString() {
        return "{name=" + this.name + ", dueDate=" + this.dueDate
                + ", completedDate=" + this.completedDate + ", category=" + this.category + "}";
    }
}
