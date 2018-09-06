package android.bg.ro.boardgame;

import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class UserActivity extends AppCompatActivity implements TaskDelegate{

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private int userId;
    private User searchUser;

    private Button buttonOption2;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");
        final int searchUserId = bundle.getInt("searchUserId");
        linearLayout = findViewById(R.id.layoutOptions);

        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/searchUser");
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
                if(!genericHttpService.getMapping().equals("sendUserRequest")) {

                    CustomParser customParser = new CustomParser();
                    searchUser = customParser.getSearchUser(genericHttpService.getResponse());
                    TextView name = findViewById(R.id.name);
                    name.setText(searchUser.getName());
                    TextView town = findViewById(R.id.town);
                    town.setText(searchUser.getTown());
                    TextView email = findViewById(R.id.email);
                    email.setText(searchUser.getEmail());
                    final Button button = findViewById(R.id.buttonOption1);
                    if (searchUser.getFriends() != null) {
                        button.setText("Remove friend");
                        buttonOption2 = findViewById(R.id.buttonOption2);
                        buttonOption2.setVisibility(View.GONE);
                        linearLayout.setWeightSum(1);

                    } else {
                        button.setText("Add friend");
                        buttonOption2 = findViewById(R.id.buttonOption2);
                        buttonOption2.setVisibility(View.GONE);
                        linearLayout.setWeightSum(1);
                        if(searchUser.getStatusEvent().equals("true")) {
                            buttonOption2.setVisibility(View.VISIBLE);
                            buttonOption2.setText("Delete request");
                            button.setText("Accept request");
                            linearLayout.setWeightSum(2);
                        }
                    }
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            URL url = null;
                            try {
                                url = new URL("http://" + Constant.IP + "/sendUserRequest");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                            parameters.add(new Pair<>("senderId", String.valueOf(userId)));
                            parameters.add(new Pair<>("receiverId", String.valueOf(searchUser.getId())));
                            parameters.add(new Pair<>("option", String.valueOf(button.getText().toString())));

                            genericHttpService = (GenericHttpService) new GenericHttpService(UserActivity.this, "sendUserRequest", parameters, taskDelegate).execute(url);
                        }
                    });
                    buttonOption2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            URL url = null;
                            try {
                                url = new URL("http://" + Constant.IP + "/sendUserRequest");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                            parameters.add(new Pair<>("senderId", String.valueOf(userId)));
                            parameters.add(new Pair<>("receiverId", String.valueOf(searchUser.getId())));
                            parameters.add(new Pair<>("option", String.valueOf(buttonOption2.getText().toString())));

                            genericHttpService = (GenericHttpService) new GenericHttpService(UserActivity.this, "sendUserRequest", parameters, taskDelegate).execute(url);
                        }
                    });


                    ImageView profilePic = (ImageView) findViewById(R.id.imgView);

                    BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
                    options.inPurgeable = true; // inPurgeable is used to free up memory while required
                    Bitmap songImage = BitmapFactory.decodeByteArray(searchUser.getProfilePicture(), 0, searchUser.getProfilePicture().length, options);//Decode image, "thumbnail" is the object of image file
                    profilePic.setImageBitmap(songImage);
                }
                else {
                    final Button button = findViewById(R.id.buttonOption1);
                    switch (genericHttpService.getResponse()) {
                        case "Send request":
                            button.setText("Delete request");
                            Toast.makeText(UserActivity.this, "The request was sent.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case "Request deleted":
                            button.setText("Add friend");
                            Toast.makeText(UserActivity.this, "You deleted your friendship.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case "Send already request":
                            Toast.makeText(UserActivity.this, "The request was already sent.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case "Accept request":
                            button.setText("Remove friend");
                            buttonOption2 = findViewById(R.id.buttonOption2);
                            buttonOption2.setVisibility(View.GONE);
                            linearLayout.setWeightSum(1);
                            Toast.makeText(UserActivity.this, "The request was accepted.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case "Delete request":
                            button.setText("Add friend");
                            buttonOption2 = findViewById(R.id.buttonOption2);
                            buttonOption2.setVisibility(View.GONE);
                            linearLayout.setWeightSum(1);
                            Toast.makeText(UserActivity.this, "The request was deleted.",
                                    Toast.LENGTH_LONG).show();
                            break;

                    }

                }
                break;
        }
    }
}