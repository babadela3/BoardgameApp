package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.BoardGameDAO;
import ro.bg.model.BoardGame;

import java.util.*;

@Service
public class BoardGameServiceImpl implements BoardGameService {

    @Autowired
    BoardGameDAO boardGameDAO;

    @Override
    public List<BoardGame> getAll() {
        List<BoardGame> boardGames = (List<BoardGame>) boardGameDAO.findAll();
        Collections.sort(boardGames,new Comparator<BoardGame>() {
            @Override
            public int compare(BoardGame bg1, BoardGame bg2) {
                return bg1.getName().compareTo(bg1.getName());
            }
        });
        return boardGames;
    }

    @Override
    public void insertBoardGame(BoardGame boardGame) {
        boardGameDAO.saveAndFlush(boardGame);
    }

    @Override
    public void updateBoardGame(BoardGame boardGame) {
        BoardGame oldBoardGame = boardGameDAO.findOne(boardGame.getId());
        oldBoardGame.setName(boardGame.getName());
        oldBoardGame.setDescription(boardGame.getDescription());
        oldBoardGame.setPicture(boardGame.getPicture());
        boardGameDAO.save(oldBoardGame);
    }

    @Override
    public void deleteBoardGame(BoardGame boardGame) {
        boardGameDAO.delete(boardGame.getId());
    }
}
