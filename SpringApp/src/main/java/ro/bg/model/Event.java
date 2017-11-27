package ro.bg.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_event_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "data")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "fk_event_creator_id")
    private User userCreator;

    @Column(name = "profile_picture")
    private String picture;

    @Column(name = "max_seats")
    private int maxSeats;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_games",
            joinColumns = {@JoinColumn(name = "pk_event_id")},
            inverseJoinColumns = {@JoinColumn(name = "pk_board_game_id")})
    private Set<BoardGame> boardGames;

    @OneToMany(mappedBy = "pk.event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventUser> eventUserSet;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "events",cascade = CascadeType.ALL)
    private Set<User> users;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public Set<BoardGame> getBoardGames() {
        return boardGames;
    }

    public void setBoardGames(Set<BoardGame> boardGames) {
        this.boardGames = boardGames;
    }

    public Set<EventUser> getEventUserSet() {
        return eventUserSet;
    }

    public void setEventUserSet(Set<EventUser> eventUserSet) {
        this.eventUserSet = eventUserSet;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
