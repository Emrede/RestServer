package de.emre.restserver;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "logs")
public class Summary {

    private String name;
    private int count;
    private DateTime date;

    public Summary(String name, int count, DateTime date) {
        this.name = name;
        this.count = count;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}