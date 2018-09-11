package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.BoardGame;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import co.yellowbricks.bggclient.BGG;
import co.yellowbricks.bggclient.common.ThingType;
import co.yellowbricks.bggclient.fetch.FetchException;
import co.yellowbricks.bggclient.fetch.domain.FetchItem;

public class BoardGameDetailsService extends AsyncTask<String, Void, String> {

    private List<Pair<String, String>> parameters;
    private List<BoardGame> boardGames;
    private Context context;
    private TaskBoardGame taskBoardGame;

    public BoardGameDetailsService(Context context, List<Pair<String, String>> parameters, TaskBoardGame taskBoardGame) {
        this.parameters = parameters;
        this.context = context;
        this.taskBoardGame = taskBoardGame;
    }

    @Override
    protected String doInBackground(String... params) {
        String receiver = "";
        for(Pair<String, String> pair : parameters){
            if(pair.first.equals("id")){
                receiver = pair.second;
            }
        }
        boardGames = new ArrayList<>();
        Collection<FetchItem> boardGame = null;
        try {
            boardGame = BGG.fetch(Arrays.asList(Integer.parseInt(receiver)), ThingType.BOARDGAME);
        } catch (FetchException e) {
            e.printStackTrace();
        }
        for(FetchItem bg : boardGame){
            BoardGame game = new BoardGame();
            game.setId(bg.getId());
            game.setDescription(bg.getDescription());
            game.setPicture(bg.getImageUrl());
            boardGames.add(game);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        taskBoardGame.searchGame(boardGames);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}


}
