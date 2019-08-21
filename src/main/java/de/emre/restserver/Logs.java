package de.emre.restserver;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logs")
public class Logs {
    @Id
    private String id;

    private String name;
    private String date;
    private String level;
    private String server;
    private String detail;
    private String source;

    public Logs(String id, String name, String date, String level, String server, String detail, String source) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.level = level;
        this.server = server;
        this.detail = detail;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
