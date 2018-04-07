package ro.bg.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "board_games")
public class BoardGame {

    @Id
    @Column(name = "pk_board_game_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_picture")
    private String picture;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "boardGames",cascade = CascadeType.ALL)
    private Set<Event> events;

    @OneToMany(mappedBy = "boardGame",cascade = CascadeType.ALL)
    private Set<GameReservation> gameReservations;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pub_game_play",
            joinColumns = {@JoinColumn(name = "pk_board_game_id")},
            inverseJoinColumns = {@JoinColumn(name = "pk_pub_id")})
    private Set<Pub> pubs;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "boardGames",cascade = CascadeType.ALL)
    private Set<User> users;

    public BoardGame() {
    }

    public BoardGame(int id, String name, String description, String picture) {
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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<GameReservation> getGameReservations() {
        return gameReservations;
    }

    public void setGameReservations(Set<GameReservation> gameReservations) {
        this.gameReservations = gameReservations;
    }

    public Set<Pub> getPubs() {
        return pubs;
    }

    public void setPubs(Set<Pub> pubs) {
        this.pubs = pubs;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "BoardGame{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
