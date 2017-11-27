package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ro.bg.model.BoardGame;
import ro.bg.service.BoardGameService;

import java.util.List;


@Controller
public class BoardGameController {

    @Autowired
    BoardGameService boardGameService;

    @RequestMapping(value = "/boardgames", method = RequestMethod.GET)
    public String boardGameUpdate(){
        List<BoardGame> boardGames = boardGameService.getAll();
        return "";
    }

    @RequestMapping(value = "/addBoardGame", method = RequestMethod.POST)
    public String boardGameInsert(@RequestBody BoardGame boardGame) {
        boardGameService.insertBoardGame(boardGame);
        return "";
    }

    @RequestMapping(value = "/deleteBoardGame", method = RequestMethod.POST)
    public String boardGameDelete(@RequestBody BoardGame boardGame) {
        boardGameService.deleteBoardGame(boardGame);
        return "";
    }

    @RequestMapping(value="/updateBoardGame",method = RequestMethod.POST)
    public String boardGameUpdate(@RequestBody BoardGame boardGame) {
        boardGameService.updateBoardGame(boardGame);
        return "";
    }
}
