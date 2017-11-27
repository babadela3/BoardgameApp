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

    @JoinColumn(name = "picture")
    private String picture;

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
