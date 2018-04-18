package ro.bg.model;

import ro.bg.model.constants.AccountTypeEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_user_id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "town")
    private String town;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountTypeEnum accountType;

    @Column(name="profile_picture", nullable=false, columnDefinition="mediumblob")
    private byte[] profilePicture;

    @OneToMany(mappedBy = "userCreator",cascade = CascadeType.ALL)
    private Set<Event> createdEvents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user")
    private Set<EventUser> eventUserSet;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites",
            joinColumns = {@JoinColumn(name = "pk_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "pk_event_id")})
    private Set<Event> events;

    @OneToMany(mappedBy = "friendOne",cascade = CascadeType.ALL)
    private Set<Friendship> friendshipsSetOne;

    @OneToMany(mappedBy = "friendTwo",cascade = CascadeType.ALL)
    private Set<Friendship> friendshipsSetTwo;

    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private Set<FriendshipRequest> senders;

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL)
    private Set<FriendshipRequest> receivers;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<GameReservation> gameReservations;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_games",
            joinColumns = {@JoinColumn(name = "pk_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "pk_board_game_id")})
    private Set<BoardGame> boardGames;

    public User(String email, String password, String name, String town, AccountTypeEnum accountType, byte[] profilePicture) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.town = town;
        this.accountType = accountType;
        this.profilePicture = profilePicture;
    }

    public User() {
    }

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

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public Set<EventUser> getEventUserSet() {
        return eventUserSet;
    }

    public void setEventUserSet(Set<EventUser> eventUserSet) {
        this.eventUserSet = eventUserSet;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Friendship> getFriendshipsSetOne() {
        return friendshipsSetOne;
    }

    public void setFriendshipsSetOne(Set<Friendship> friendshipsSetOne) {
        this.friendshipsSetOne = friendshipsSetOne;
    }

    public Set<Friendship> getFriendshipsSetTwo() {
        return friendshipsSetTwo;
    }

    public void setFriendshipsSetTwo(Set<Friendship> friendshipsSetTwo) {
        this.friendshipsSetTwo = friendshipsSetTwo;
    }

    public Set<FriendshipRequest> getSenders() {
        return senders;
    }

    public void setSenders(Set<FriendshipRequest> senders) {
        this.senders = senders;
    }

    public Set<FriendshipRequest> getReceivers() {
        return receivers;
    }

    public void setReceivers(Set<FriendshipRequest> receivers) {
        this.receivers = receivers;
    }

    public Set<GameReservation> getGameReservations() {
        return gameReservations;
    }

    public void setGameReservations(Set<GameReservation> gameReservations) {
        this.gameReservations = gameReservations;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<BoardGame> getBoardGames() {
        return boardGames;
    }

    public void setBoardGames(Set<BoardGame> boardGames) {
        this.boardGames = boardGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getId() != user.getId()) return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        if (getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null)
            return false;
        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
        if (getTown() != null ? !getTown().equals(user.getTown()) : user.getTown() != null) return false;
        if (getAccountType() != user.getAccountType()) return false;
        if (getProfilePicture() != null ? !getProfilePicture().equals(user.getProfilePicture()) : user.getProfilePicture() != null)
            return false;
        if (getCreatedEvents() != null ? !getCreatedEvents().equals(user.getCreatedEvents()) : user.getCreatedEvents() != null)
            return false;
        if (getEventUserSet() != null ? !getEventUserSet().equals(user.getEventUserSet()) : user.getEventUserSet() != null)
            return false;
        if (getEvents() != null ? !getEvents().equals(user.getEvents()) : user.getEvents() != null) return false;
        if (getFriendshipsSetOne() != null ? !getFriendshipsSetOne().equals(user.getFriendshipsSetOne()) : user.getFriendshipsSetOne() != null)
            return false;
        return getFriendshipsSetTwo() != null ? getFriendshipsSetTwo().equals(user.getFriendshipsSetTwo()) : user.getFriendshipsSetTwo() == null;

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getTown() != null ? getTown().hashCode() : 0);
        result = 31 * result + (getAccountType() != null ? getAccountType().hashCode() : 0);
        result = 31 * result + (getProfilePicture() != null ? getProfilePicture().hashCode() : 0);
        result = 31 * result + (getCreatedEvents() != null ? getCreatedEvents().hashCode() : 0);
        result = 31 * result + (getEventUserSet() != null ? getEventUserSet().hashCode() : 0);
        result = 31 * result + (getEvents() != null ? getEvents().hashCode() : 0);
        result = 31 * result + (getFriendshipsSetOne() != null ? getFriendshipsSetOne().hashCode() : 0);
        result = 31 * result + (getFriendshipsSetTwo() != null ? getFriendshipsSetTwo().hashCode() : 0);
        return result;
    }
}
