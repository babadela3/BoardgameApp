package ro.bg.service;


import co.yellowbricks.bggclient.fetch.FetchException;
import org.springframework.transaction.annotation.Transactional;
import ro.bg.model.BoardGame;
import ro.bg.model.dto.BoardGameDTO;

import java.util.List;

public interface BoardGameService {

    @Transactional(readOnly = true)
    List<BoardGame> getAllById(int id);

    BoardGame findById(int id);

    void insertBoardGame(List<Integer> ids, String email) throws FetchException;

    void deleteGame(int idGame, int idUser) throws FetchException;

    void addGame(int idGame, int idUser) throws FetchException;

    void updateBoardGame(BoardGame boardGame);

    void deleteBoardGame(List<Integer> ids);

    BoardGameDTO getGamesByUserId(int userId);
}
