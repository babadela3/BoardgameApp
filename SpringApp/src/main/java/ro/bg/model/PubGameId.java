package ro.bg.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class PubGameId implements Serializable{

    @ManyToOne
    private Pub pub;

    @ManyToOne
    private BoardGame boardGame;

    public Pub getPub() {
        return pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public void setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PubGameId pubGameId = (PubGameId) o;

        if (getPub() != null ? !getPub().equals(pubGameId.getPub()) : pubGameId.getPub() != null) return false;
        return getBoardGame() != null ? getBoardGame().equals(pubGameId.getBoardGame()) : pubGameId.getBoardGame() == null;

    }

    @Override
    public int hashCode() {
        int result = getPub() != null ? getPub().hashCode() : 0;
        result = 31 * result + (getBoardGame() != null ? getBoardGame().hashCode() : 0);
        return result;
    }
}
