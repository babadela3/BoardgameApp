package ro.bg.model.dto;

import java.io.Serializable;

public class BoardGameAndroidDTO implements Serializable{

    private int id;

    private String name;

    private String description;

    private String picture;

    private boolean added;

    public BoardGameAndroidDTO() {
    }

    public BoardGameAndroidDTO(int id, String name, String description, String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }
}
