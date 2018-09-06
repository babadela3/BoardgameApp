package android.bg.ro.boardgame;

import android.app.Activity;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.BoardGameDetailsService;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.ImageLoader;
import android.bg.ro.boardgame.services.TaskBoardGame;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BoardGameActivity extends AppCompatActivity implements TaskBoardGame,TaskDelegate {

    TaskDelegate taskDelegate;
    TaskBoardGame taskBoardGame;
    private Activity activity;
    private GenericHttpService genericHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_boardgame);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        final int userId = bundle.getInt("userId");
        final int gameId = bundle.getInt("gameId");
        String name = bundle.getString("name");
        String description = bundle.getString("description");
        String urlPicture = bundle.getString("picture");

        ImageLoader imageLoader = new ImageLoader(this);
        taskBoardGame = this;
        taskDelegate = this;
        activity = this;

        TextView title = findViewById(R.id.title);
        title.setText(name);

        if(description != null) {
            TextView textOptions = findViewById(R.id.textOptions);
            textOptions.setVisibility(View.GONE);

            LinearLayout linearLayout = findViewById(R.id.layoutOptions);
            linearLayout.setVisibility(View.GONE);

            TextView descriptionGame = findViewById(R.id.description);
            descriptionGame.setText(description);
            ImageView imageView = findViewById(R.id.imageBoardGame);
            imageLoader.DisplayImage(urlPicture, imageView);
        }
        else {
            URL url = null;
            try {
                url = new URL("http://" + Constant.IP + "/hasGame");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
            parameters.add(new Pair<>("id", String.valueOf(gameId).toString()));
            genericHttpService = (GenericHttpService) new GenericHttpService(activity,"hasGame", parameters,taskDelegate).execute(url);

            List<Pair<String, String>> params = new ArrayList<>();
            params.add(new Pair<>("id", String.valueOf(gameId).toString()));
            BoardGameDetailsService boardGameService = (BoardGameDetailsService) new BoardGameDetailsService(activity, params,taskBoardGame).execute();

            final Button buttonOption = findViewById(R.id.buttonOption1);
            buttonOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    URL url = null;
                    try {
                        url = new URL("http://" + Constant.IP + "/modifyGame");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                    parameters.add(new Pair<>("idGame", String.valueOf(gameId).toString()));
                    parameters.add(new Pair<>("idUser", String.valueOf(userId).toString()));
                    parameters.add(new Pair<>("option", buttonOption.getText().toString()));
                    genericHttpService = (GenericHttpService) new GenericHttpService(activity,"modifyGame", parameters,taskDelegate).execute(url);
                }
            });

        }

    }

    @Override
    public void searchGame(List<BoardGame> boardGames) {
        for(BoardGame boardGame : boardGames) {
            ImageLoader imageLoader = new ImageLoader(this);
            ImageView imageView = findViewById(R.id.imageBoardGame);
            imageLoader.DisplayImage(boardGame.getPicture(), imageView);
            TextView description = findViewById(R.id.description);
            description.setText(boardGame.getDescription());
        }
    }

    @Override
    public void TaskCompletionResult(String result) {
        if(genericHttpService.getResponse().equals("Yes")){
            Button buttonOption = findViewById(R.id.buttonOption1);
            buttonOption.setText("Delete");
        }
        if(genericHttpService.getResponse().equals("No")){
            Button buttonOption = findViewById(R.id.buttonOption1);
            buttonOption.setText("Add");
        }
        if(genericHttpService.getResponse().equals("Add")){
            Toast.makeText(BoardGameActivity.this, "The game was added to your collection.",
                    Toast.LENGTH_LONG).show();
            Button buttonOption = findViewById(R.id.buttonOption1);
            buttonOption.setText("Delete");
        }
        if(genericHttpService.getResponse().equals("Delete")){
            Toast.makeText(BoardGameActivity.this, "The game was removed to your collection.",
                    Toast.LENGTH_LONG).show();
            Button buttonOption = findViewById(R.id.buttonOption1);
            buttonOption.setText("Add");
        }
    }
}
