package ro.bg.model;

import ro.bg.model.constants.ReservationStatusEnum;

import javax.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_reservation_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "fk_pub_id")
    private Pub pub;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatusEnum reservationStatusEnum;

    @Column(name = "message")
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Pub getPub() {
        return pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    public ReservationStatusEnum getReservationStatusEnum() {
        return reservationStatusEnum;
    }

    public void setReservationStatusEnum(ReservationStatusEnum reservationStatusEnum) {
        this.reservationStatusEnum = reservationStatusEnum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
