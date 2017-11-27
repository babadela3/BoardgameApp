package ro.bg.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "board_games")
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "boardGames",cascade = CascadeType.ALL)
    private Set<Pub> pubs;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "boardGames",cascade = CascadeType.ALL)
    private Set<User> users;

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
