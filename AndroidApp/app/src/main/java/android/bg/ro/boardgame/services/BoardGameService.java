package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.BoardGame;
import android.content.Context;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import co.yellowbricks.bggclient.BGG;
import co.yellowbricks.bggclient.common.ThingType;
import co.yellowbricks.bggclient.search.SearchException;
import co.yellowbricks.bggclient.search.domain.SearchItem;
import co.yellowbricks.bggclient.search.domain.SearchOutput;

public class BoardGameService extends AsyncTask<String, Void, String>{

    private List<Pair<String, String>> parameters;
    private Context context;
    private TaskBoardGame taskBoardGame;
    private List<BoardGame> boardGames;

    public BoardGameService(Context context, List<Pair<String, String>> parameters, TaskBoardGame taskBoardGame) {
        this.parameters = parameters;
        this.context = context;
        this.taskBoardGame = taskBoardGame;
    }

    @Override
    protected String doInBackground(String... params) {
        String receiver = "";
        for(Pair<String, String> pair : parameters){
            if(pair.first.equals("search")){
                receiver = pair.second;
            }
        }
        SearchOutput items = null;
        try {
            MultiDex.install(context);
            boardGames = new ArrayList<>();
            items = BGG.search(receiver, ThingType.BOARDGAME);
            if(items != null) {
                for (SearchItem item : items.getItems()) {
                    BoardGame boardGame = new BoardGame();
                    boardGame.setId(item.getId());
                    boardGame.setName(item.getName().getValue());
                    boardGames.add(boardGame);
                }
            }
        } catch (SearchException e) {
            e.printStackTrace();
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