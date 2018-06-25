package ro.bg.service;

import co.yellowbricks.bggclient.BGG;
import co.yellowbricks.bggclient.common.ThingType;
import co.yellowbricks.bggclient.fetch.FetchException;
import co.yellowbricks.bggclient.fetch.domain.FetchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ro.bg.dao.BoardGameDAO;
import ro.bg.dao.PubDAO;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;

import java.util.*;

@Service
public class BoardGameServiceImpl implements BoardGameService {

    @Autowired
    BoardGameDAO boardGameDAO;

    @Autowired
    PubDAO pubDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<BoardGame> getAllById(int id) {
        List<BoardGame> boardGames = boardGameDAO.getAllById(id);
        Collections.sort(boardGames,new Comparator<BoardGame>() {
            @Override
            public int compare(BoardGame bg1, BoardGame bg2) {
                return bg1.getName().compareTo(bg1.getName());
            }
        });
        for(BoardGame boardGame: boardGames){
            boardGame.setPubs(null);
            boardGame.setEvents(null);
            boardGame.setGameReservations(null);
        }
        return boardGames;
    }

    @Override
    public BoardGame findById(int id) {
        return boardGameDAO.findOne(id);
    }

    @Override
    public void insertBoardGame(List<Integer> ids, String email) throws FetchException {
        Pub pub = pubDAO.findByEmail(email);
        for(int id: ids){
            if(findById(id) == null){
                Collection<FetchItem> boardGames = BGG.fetch(Arrays.asList(id), ThingType.BOARDGAME);
                for(FetchItem bg : boardGames){
                    BoardGame boardGame = new BoardGame(bg.getId(),bg.getName(),bg.getDescription(),bg.getImageUrl());
                    List<Pub> pubs = new ArrayList<>();
                    pubs.add(pub);
                    Set<Pub> pubSet = new HashSet<>(pubs);
                    boardGame.setPubs(pubSet);
                    boardGameDAO.saveAndFlush(boardGame);
                }
            }
            else{
                BoardGame boardGame = boardGameDAO.findOne(id);
                Set<Pub> pubs = boardGame.getPubs();

                boolean isPub = false;
                for (Iterator<Pub> it = pubs.iterator(); it.hasNext(); ) {
                    Pub pb = it.next();
                    if (pb.equals(pub))
                        isPub = true;
                        break;
                }
                if(!isPub){
                    pubs.add(pub);
                    boardGame.setPubs(pubs);
                    boardGameDAO.saveAndFlush(boardGame);
                }
            }
        }
    }

    @Override
    public void deleteGame(int idGame, int idUser) throws FetchException {
        jdbcTemplate.update(
                "delete from user_games where pk_board_game_id = ? and pk_user_id = ?",
                idGame,idUser);
    }

    @Override
    public void addGame(int idGame, int idUser) throws FetchException {
        if(findById(idGame) == null){
            Collection<FetchItem> boardGames = BGG.fetch(Arrays.asList(idGame), ThingType.BOARDGAME);
            for(FetchItem bg : boardGames){
                BoardGame boardGame = new BoardGame(bg.getId(),bg.getName(),bg.getDescription(),bg.getImageUrl());
                boardGameDAO.saveAndFlush(boardGame);
            }
        }
        jdbcTemplate.update(
                "insert into user_games values (?,?)",
                idGame, idUser);
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
    public void deleteBoardGame(List<Integer> ids) {
        for(int id : ids){
            boardGameDAO.delete(id);
        }
    }
}
