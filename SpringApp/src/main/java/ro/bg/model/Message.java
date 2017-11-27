package ro.bg.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_message_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_friendship_id")
    private Friendship friendship;

    @Column(name = "sender_id")
    private int sender;

    @Column(name = "receiver_id")
    private int receiver;

    @Column(name = "message")
    private String message;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "data")
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", friendship=" + friendship.getId() +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
