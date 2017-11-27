package ro.bg.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "friendship_requests")
public class FriendshipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_friendship_request_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "fk_receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "friendshipRequest",cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
}
