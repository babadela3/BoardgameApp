package ro.bg.model.dto;

import java.io.Serializable;

public class FriendAndroidDTO implements Serializable{

    private int id;

    private String name;

    private byte[] profilePicture;

    private boolean hasInvited;

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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isHasInvited() {
        return hasInvited;
    }

    public void setHasInvited(boolean hasInvited) {
        this.hasInvited = hasInvited;
    }
}
