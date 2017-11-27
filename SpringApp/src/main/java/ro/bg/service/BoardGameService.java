package ro.bg.service;


import org.springframework.transaction.annotation.Transactional;
import ro.bg.model.BoardGame;

import java.util.List;

public interface BoardGameService {

    @Transactional(readOnly = true)
    List<BoardGame> getAll();

    void insertBoardGame(BoardGame boardGame);

    void updateBoardGame(BoardGame boardGame);

    void deleteBoardGame(BoardGame boardGame);
}
