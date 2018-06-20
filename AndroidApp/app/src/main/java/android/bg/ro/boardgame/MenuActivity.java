package android.bg.ro.boardgame;

import android.app.Fragment;
import android.bg.ro.boardgame.fragments.CreateEventFragment;
import android.bg.ro.boardgame.fragments.HomeFragment;
import android.bg.ro.boardgame.fragments.ProfileFragment;
import android.bg.ro.boardgame.fragments.SearchFragment;
import android.bg.ro.boardgame.models.Client;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.SocketClientConnection;
import android.bg.ro.boardgame.services.TaskClient;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
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

public class MenuActivity extends AppCompatActivity implements TaskDelegate, TaskClient{

    private User user;
    private Client client;
    private TaskDelegate taskDelegate;
    private TaskClient taskClient;
    private GenericHttpService genericHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_menu);
        getSupportActionBar().hide();

        taskDelegate = this;
        taskClient = this;
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

            genericHttpService = (GenericHttpService) new GenericHttpService(MenuActivity.this.getApplicationContext(),"getUser", params,taskDelegate).execute(url);
        }

        ImageButton profileButton = (ImageButton) findViewById(R.id.imageButtonProfile);
        ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearch);
        ImageButton homeButton = (ImageButton) findViewById(R.id.imageButtonHome);
        ImageButton createEventButton = (ImageButton) findViewById(R.id.imageButtonAdd);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentForProfile);

                if(fragment != null){
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragmentForProfile)).commit();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragmentsMenu, new ProfileFragment()).addToBackStack(null).commit();

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentsMenu);
                if(!(fragment instanceof SearchFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.fragmentsMenu, new SearchFragment()).addToBackStack(null).commit();

                }

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

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentsMenu);
                if(!(fragment instanceof CreateEventFragment)){
                    getFragmentManager().beginTransaction().replace(R.id.fragmentsMenu, new CreateEventFragment()).addToBackStack(null).commit();

                }
            }
        });
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
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

                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                params.add(new Pair<>("id",String.valueOf(user.getId())));
                new SocketClientConnection(taskClient,params).execute("");
                break;
            case 401:
                Toast.makeText(MenuActivity.this, "The mail or password is incorrect.",
                        Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void TaskClientResult(Client client) {
        setClient(client);
    }

    @Override
    public void processMessage(String message) {

    }
}