package ro.bg.model.dto;

import java.util.List;

public class BoardGameDTO {

    private List<Integer> boardGamesIds;

    private String email;

    public List<Integer> getBoardGamesIds() {
        return boardGamesIds;
    }

    public void setBoardGamesIds(List<Integer> boardGamesIds) {
        this.boardGamesIds = boardGamesIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
