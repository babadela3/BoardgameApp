package ro.bg.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ro.bg.model.constants.StatusUserEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event_users")
@AssociationOverrides({
        @AssociationOverride(name = "pk.event",
                joinColumns = @JoinColumn(name = "PK_EVENT_ID")),
        @AssociationOverride(name = "pk.user",
                joinColumns = @JoinColumn(name = "PK_USER_ID")) })
public class EventUser implements Serializable{

    @EmbeddedId
    @JsonUnwrapped
    private EventUserId pk = new EventUserId();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusUserEnum statusUserEnum;

    public StatusUserEnum getStatusUserEnum() {
        return statusUserEnum;
    }

    public void setStatusUserEnum(StatusUserEnum statusUserEnum) {
        this.statusUserEnum = statusUserEnum;
    }

    public EventUserId getPk() {
        return pk;
    }

    public void setPk(EventUserId pk) {
        this.pk = pk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventUser eventUser = (EventUser) o;

        return getPk() != null ? getPk().equals(eventUser.getPk()) : eventUser.getPk() == null;

    }

    @Override
    public int hashCode() {
        return getPk() != null ? getPk().hashCode() : 0;
    }
}
