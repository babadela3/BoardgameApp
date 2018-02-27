package ro.bg.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pubs")
public class Pub {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_pub_id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_picture")
    private String picture;

    @OneToMany(mappedBy = "pub",cascade = CascadeType.ALL)
    private Set<GameReservation> gameReservations;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pub_game_play",
            joinColumns = {@JoinColumn(name = "pk_pub_id")},
            inverseJoinColumns = {@JoinColumn(name = "pk_board_game_id")})
    private Set<BoardGame> boardGames;

    @OneToMany(mappedBy = "pub",cascade = CascadeType.ALL)
    private Set<PubPicture> pubPictures;

    @OneToMany(mappedBy = "pub",cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<GameReservation> getGameReservations() {
        return gameReservations;
    }

    public void setGameReservations(Set<GameReservation> gameReservations) {
        this.gameReservations = gameReservations;
    }

    public Set<BoardGame> getBoardGames() {
        return boardGames;
    }

    public void setBoardGames(Set<BoardGame> boardGames) {
        this.boardGames = boardGames;
    }

    public Set<PubPicture> getPubPictures() {
        return pubPictures;
    }

    public void setPubPictures(Set<PubPicture> pubPictures) {
        this.pubPictures = pubPictures;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Pub{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
