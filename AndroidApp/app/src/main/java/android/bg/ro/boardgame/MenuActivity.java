package android.bg.ro.boardgame;

import android.app.Fragment;
import android.bg.ro.boardgame.fragments.HomeFragment;
import android.bg.ro.boardgame.fragments.ProfileFragment;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.ReceiveData;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements TaskDelegate{

    private User user;
    private TaskDelegate taskDelegate;
    private ReceiveData receiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_menu);
        getSupportActionBar().hide();

        taskDelegate = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            URL url = null;
            try {
                url = new URL("http://" + getResources().getString(R.string.localhost) + "/getUser");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
            params.add(new Pair<>("email",extras.getString("email")));
            params.add(new Pair<>("password",extras.getString("password")));

            receiveData = (ReceiveData) new ReceiveData(MenuActivity.this.getApplicationContext(),"getUser", params,taskDelegate).execute(url);
        }

        ImageButton profileButton = (ImageButton) findViewById(R.id.imageButtonProfile);
        ImageButton homeButton = (ImageButton) findViewById(R.id.imageButtonHome);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentForProfile);


                if(fragment != null){
                    if(fragment.getChildFragmentManager().findFragmentById(R.id.imageButtonEvents) != null){
                        System.out.println("macar atat: events");
                    }
                    if(fragment.getChildFragmentManager().findFragmentById(R.id.imageButtonGames) != null){
                        System.out.println("macar atat: games");
                    }
                    if(fragment.getChildFragmentManager().findFragmentById(R.id.imageButtonFriends) != null){
                        System.out.println("macar atat: friends");
                    }
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragmentForProfile)).commit();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragmentsMenu, new ProfileFragment()).addToBackStack(null).commit();

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentsMenu);
                if(!(fragment instanceof HomeFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.fragmentsMenu, new HomeFragment()).addToBackStack(null).commit();

                }
            }
        });
    }

    public User getUser() {
        return user;
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (receiveData.getResponseCode()) {
            case 200:
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(receiveData.getResponse());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CustomParser customParser = new CustomParser();
                user = customParser.getUser(json);
                break;
            case 401:
                Toast.makeText(MenuActivity.this, "The mail or password is incorrect.",
                        Toast.LENGTH_LONG).show();
                break;

        }
    }
}