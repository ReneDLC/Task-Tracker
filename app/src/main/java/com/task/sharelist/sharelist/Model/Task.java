package com.task.sharelist.sharelist.Model;

import java.io.Serializable;
import java.util.HashMap;

public class Task implements Serializable{
    private String title;
    private String description;
    private String date;

    public Task()
    {
        //default constructor for DataSnapshot.getValue(Task.class)
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    public HashMap<String, String> toFirebaseObject()
    {
        HashMap<String, String> task = new HashMap<String, String>();
        task.put("title",title);
        task.put("description", description);
        task.put("date", date);

        return task;
    }

}
