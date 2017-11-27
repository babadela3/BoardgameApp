package ro.bg.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_friendship_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_friend_one_id")
    private User friendOne;

    @ManyToOne
    @JoinColumn(name = "fk_friend_two_id")
    private User friendTwo;

    @OneToMany(mappedBy = "friendship",cascade = CascadeType.ALL)
    private Set<Message> messages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFriendOne() {
        return friendOne;
    }

    public void setFriendOne(User friendOne) {
        this.friendOne = friendOne;
    }

    public User getFriendTwo() {
        return friendTwo;
    }

    public void setFriendTwo(User friendTwo) {
        this.friendTwo = friendTwo;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
