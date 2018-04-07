package ro.bg.controller;

import co.yellowbricks.bggclient.BGG;
import co.yellowbricks.bggclient.common.ThingType;
import co.yellowbricks.bggclient.fetch.FetchException;
import co.yellowbricks.bggclient.fetch.domain.FetchItem;
import co.yellowbricks.bggclient.search.SearchException;
import co.yellowbricks.bggclient.search.domain.SearchItem;
import co.yellowbricks.bggclient.search.domain.SearchOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;
import ro.bg.service.BoardGameService;
import ro.bg.service.PubService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Controller
public class BoardGameController {

    @Autowired
    BoardGameService boardGameService;

    @Autowired
    PubService pubService;

    @RequestMapping(value = "/addBoardGames", method = RequestMethod.POST)
    @ResponseBody
    public void boardGamesInsert(@RequestParam(value="boardGamesIds[]") List<Integer> boardGamesIds, @RequestParam(value="email") String email) throws FetchException {
        boardGameService.insertBoardGame(boardGamesIds,email);
    }

    @RequestMapping(value = "/deleteBoardGames", method = RequestMethod.POST)
    @ResponseBody
    public void boardGamesDelete(@RequestParam(value="boardGamesIds[]") List<Integer> boardGamesIds, @RequestParam(value="email") String email) throws FetchException {
        boardGameService.deleteBoardGame(boardGamesIds);
    }

    @RequestMapping(value="/updateBoardGame",method = RequestMethod.POST)
    public String boardGameUpdate(@RequestBody BoardGame boardGame) {
        boardGameService.updateBoardGame(boardGame);
        return "";
    }

    @RequestMapping(value = "/getBoardGamesAll", method = RequestMethod.GET, produces="application/json")
    public @ResponseBody  List<FetchItem> getBoardGamesAll(@RequestParam("search") String search, HttpServletResponse response, HttpServletRequest request) throws SearchException, FetchException {
        List<FetchItem> boardGames = new ArrayList<>();
        SearchOutput items = BGG.search(search, ThingType.BOARDGAME);
        for (SearchItem item : items.getItems()) {
            Collection<FetchItem> boardGame = BGG.fetch(Arrays.asList(item.getId()), ThingType.BOARDGAME);
            for(FetchItem bg : boardGame){
                boardGames.add(bg);
            }
        }
        return boardGames;
    }

    @RequestMapping(value = "/getBoardGames", method = RequestMethod.GET, produces="application/json")
    public @ResponseBody  List<BoardGame> getBoardGames(@RequestParam("email") String email, HttpServletResponse response, HttpServletRequest request){
        Pub pub = pubService.getPubByEmail(email);
        List<BoardGame> boardGames = boardGameService.getAllById(pub.getId());
        return boardGames;
    }



    @RequestMapping(value = "/pictureBoardGame", method = RequestMethod.GET)
    public void showImage(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request)
            throws ServletException {

    }
}
