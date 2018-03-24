package ro.bg.model;

import javax.persistence.*;

@Entity
@Table(name = "pub_pictures")
public class PubPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_pub_picture_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_pub_id")
    private Pub pub;

    @JoinColumn(name = "picture", columnDefinition="mediumblob")
    private byte[] picture;

    public PubPicture(Pub pub, byte[] picture) {
        this.pub = pub;
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

    public Pub getPub() {
        return pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    public byte[] getPicture() { return picture; }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
