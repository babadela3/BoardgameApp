package ro.bg.service;


import co.yellowbricks.bggclient.fetch.FetchException;
import org.springframework.transaction.annotation.Transactional;
import ro.bg.model.BoardGame;

import java.util.List;

public interface BoardGameService {

    @Transactional(readOnly = true)
    List<BoardGame> getAllById(int id);

    BoardGame findById(int id);

    void insertBoardGame(List<Integer> ids, String email) throws FetchException;

    void updateBoardGame(BoardGame boardGame);

    void deleteBoardGame(List<Integer> ids);
}
