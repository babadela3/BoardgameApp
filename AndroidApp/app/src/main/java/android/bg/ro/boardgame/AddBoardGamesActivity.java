package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.AddBoardGameAdapter;
import android.bg.ro.boardgame.adapters.BoardGameSelectAdapter;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddBoardGamesActivity extends AppCompatActivity implements TaskDelegate {

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private User user;
    private ArrayList<BoardGame> boardGames;
    ArrayList<BoardGame> addedBoardGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();

        int userId = (int) bundle.getSerializable("userId");

        TextView title = (TextView) findViewById(R.id.createEvent_title);
        title.setText("Select Board Games");
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("Select the board games you want to play.");

        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/getUserInformation");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<>("userId",String.valueOf(userId)));

        genericHttpService = (GenericHttpService) new GenericHttpService(AddBoardGamesActivity.this,"getUserInformation", params,taskDelegate).execute(url);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("addedBoardGames",addedBoardGames);
        intent.putExtras(bundle);
        setResult(30, intent);
        super.onBackPressed();
    }

    public ArrayList<BoardGame> getBoardGames() {
        return boardGames;
    }

    public void setBoardGames(ArrayList<BoardGame> boardGames) {
        this.boardGames = boardGames;
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(genericHttpService.getResponse());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CustomParser customParser = new CustomParser();
                user = customParser.getUser(json);

                boardGames = (ArrayList<BoardGame>) user.getBoardGames();
                AddBoardGameAdapter adapter = new AddBoardGameAdapter(AddBoardGamesActivity.this, 0, boardGames);
                ListView listView = (ListView) findViewById(R.id.listFriends);
                listView.setAdapter(adapter);

                Button button = findViewById(R.id.button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addedBoardGames = new ArrayList<>();
                        for(BoardGame boardGame : boardGames){
                            if(boardGame.isAdded()){
                                addedBoardGames.add(boardGame);
                            }
                        }
                        onBackPressed();
                    }
                });
                break;
        }
    }
}