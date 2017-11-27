package ro.bg.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class EventUserId implements Serializable{

    @ManyToOne
    private Event event;

    @ManyToOne
    private User user;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventUserId that = (EventUserId) o;

        if (getEvent() != null ? !getEvent().equals(that.getEvent()) : that.getEvent() != null) return false;
        return getUser() != null ? getUser().equals(that.getUser()) : that.getUser() == null;

    }

    @Override
    public int hashCode() {
        int result = getEvent() != null ? getEvent().hashCode() : 0;
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
