package android.bg.ro.boardgame.models;

public class PubPicture {

    int id;

    private byte[] picture;

    public PubPicture(int id, byte[] picture) {
        this.id = id;
        this.picture = picture;
    }

    public PubPicture() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
