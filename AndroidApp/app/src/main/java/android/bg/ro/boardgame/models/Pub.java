package android.bg.ro.boardgame.models;

import java.io.Serializable;
import java.util.List;

public class Pub implements Serializable{

    int id;

    private String email;

    private String name;

    private String address;

    private String description;

    private boolean selected;

    private List<Integer> picturesId;

    private byte[] profilePicture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<Integer> getPicturesId() {
        return picturesId;
    }

    public void setPicturesId(List<Integer> picturesId) {
        this.picturesId = picturesId;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}
