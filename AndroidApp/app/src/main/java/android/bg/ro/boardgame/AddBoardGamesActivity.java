package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.AddBoardGameAdapter;
import android.bg.ro.boardgame.models.BoardGame;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddBoardGamesActivity extends AppCompatActivity {

    private ArrayList<BoardGame> boardGames;
    ArrayList<BoardGame> addedBoardGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();

        boardGames = (ArrayList<BoardGame>) bundle.getSerializable("boardGames");

        TextView title = (TextView) findViewById(R.id.createEvent_title);
        title.setText("Select Board Games");
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("Select the board games you want to play.");

        AddBoardGameAdapter adapter = new AddBoardGameAdapter(AddBoardGamesActivity.this, 0, boardGames);
        ListView listView = (ListView) findViewById(R.id.listFriends);
        listView.setAdapter(adapter);

        for(BoardGame boardGame : boardGames){
            System.out.println(boardGame.getName());
        }

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
}