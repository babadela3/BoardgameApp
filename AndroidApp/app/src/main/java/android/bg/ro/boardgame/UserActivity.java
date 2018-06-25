package android.bg.ro.boardgame;

import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements TaskDelegate{

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private int userId;
    private User searchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");
        final int searchUserId = bundle.getInt("searchUserId");

        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/searchUser");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("userId",String.valueOf(userId)));
        parameters.add(new Pair<>("searchUserId",String.valueOf(searchUserId)));

        genericHttpService = (GenericHttpService) new GenericHttpService(this,"searchUser", parameters,taskDelegate).execute(url);

    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                CustomParser customParser = new CustomParser();
                searchUser = customParser.getSearchUser(genericHttpService.getResponse());
                TextView name = findViewById(R.id.name);
                name.setText(searchUser.getName());
                TextView town = findViewById(R.id.town);
                town.setText(searchUser.getTown());
                TextView email = findViewById(R.id.email);
                email.setText(searchUser.getEmail());
                if(searchUser.getFriends() != null){
                    Button button = findViewById(R.id.buttonOption1);
                    button.setText("Remove friend");
                }
                else {
                    Button button = findViewById(R.id.buttonOption1);
                    button.setText("Add friend");
                }
                ImageView profilePic = (ImageView) findViewById(R.id.imgView);

                BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
                options.inPurgeable = true; // inPurgeable is used to free up memory while required
                Bitmap songImage = BitmapFactory.decodeByteArray(searchUser.getProfilePicture(),0, searchUser.getProfilePicture().length,options);//Decode image, "thumbnail" is the object of image file
                profilePic.setImageBitmap(songImage);
                break;
        }
    }
}