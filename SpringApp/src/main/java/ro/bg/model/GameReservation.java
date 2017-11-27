package ro.bg.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ro.bg.model.constants.ReservationStatusEnum;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game_reservations")
public class GameReservation{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_game_reservation_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_board_game_id")
    private BoardGame boardGame;

    @ManyToOne
    @JoinColumn(name = "fk_pub_id")
    private Pub pub;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "start_date")
    private Date startDate;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "end_date")
    private Date endDate;

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

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public void setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Pub getPub() {
        return pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
