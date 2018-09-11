package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.UserEventAdapter;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.bg.ro.boardgame.models.Event;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.GoogleMapsService;
import android.bg.ro.boardgame.services.ImageLoader;
import android.bg.ro.boardgame.services.TaskChangeStatus;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.services.TaskGoogleMaps;
import android.bg.ro.boardgame.services.UserStatusService;
import android.bg.ro.boardgame.utils.Constant;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends FragmentActivity implements OnMapReadyCallback, TaskDelegate, TaskGoogleMaps, TaskChangeStatus {

    TaskGoogleMaps taskGoogleMaps;
    TaskDelegate taskDelegate;
    GenericHttpService genericHttpService;
    private TaskChangeStatus taskChangeStatus;
    private UserStatusService userStatusService;
    private Event event;
    private GoogleMap mMap;

    private Button buttonOption1;
    private Button buttonOption2;
    private TextView status;
    private TextView friendsInvited;
    private double latitude;
    private double longitude;
    private String title;
    private int userId;
    private int creatorId;
    private ArrayList<User> userParticipants;
    private ArrayList<User> userInvited;
    private ArrayList<User> userRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_no_creator);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");
        final int eventId = bundle.getInt("eventId");
        title = bundle.getString("name");

        taskDelegate = this;
        taskGoogleMaps = this;
        taskChangeStatus = this;

        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/getEvent");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("eventId",String.valueOf(eventId)));

        genericHttpService = (GenericHttpService) new GenericHttpService(this,"getEvent", parameters,taskDelegate).execute(url);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                if(!genericHttpService.getMapping().equals("getUsersStatus")) {
                    CustomParser customParser = new CustomParser();
                    event = customParser.getEvent(genericHttpService.getResponse());

                    if(event.getLatitude() != 0 || event.getLongitude() != 0) {
                        LatLng latLng = new LatLng(event.getLatitude(),event.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        latitude = latLng.latitude ;
                        longitude = latLng.longitude;
                    }

                    TextView pubEvent = findViewById(R.id.pub);

                    if (event.getPub() != null) {
                        pubEvent.setText(event.getPub().getName());
                        taskGoogleMaps = this;
                        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                        parameters.add(new Pair<>("location", event.getAddress()));
                        new GoogleMapsService(this, taskGoogleMaps, parameters).execute("");
                    } else {
                        pubEvent.setText("None");
                    }
                    buttonOption1 = findViewById(R.id.buttonOption1);
                    buttonOption2 = findViewById(R.id.buttonOption2);

                    SquareImageView imageView = (SquareImageView) findViewById(R.id.imageEvent);
                    TextView titleEvent = findViewById(R.id.title);
                    TextView descriptionEvent = findViewById(R.id.description);
                    TextView userCreatorEvent = findViewById(R.id.userCreator);
                    TextView addressEvent = findViewById(R.id.address);
                    TextView dateEvent = findViewById(R.id.date);
                    TextView timeEvent = findViewById(R.id.timeHours);
                    TextView maxSeatsEvent = findViewById(R.id.maxSeats);
                    TextView games = findViewById(R.id.games);
                    status = findViewById(R.id.status);


                    URL url = null;
                    try {
                        url = new URL("http://" + Constant.IP + "/getUsersStatus");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                    params.add(new Pair<>("eventId", String.valueOf(event.getId())));

                    genericHttpService = (GenericHttpService) new GenericHttpService(this, "getUsersStatus", params, taskDelegate).execute(url);
                    creatorId = event.getUserCreator().getId();

                    if (!event.getPicture().equals("null")) {
                        ImageLoader imageLoader = new ImageLoader(this);
                        imageLoader.DisplayImage(event.getPicture(), imageView);
                    }

                    titleEvent.setText(event.getTitle());
                    descriptionEvent.setText(event.getDescription());
                    userCreatorEvent.setText(event.getUserCreator().getName());
                    addressEvent.setText(event.getAddress());
                    dateEvent.setText(event.getDate().substring(6));
                    timeEvent.setText(event.getDate().substring(0, 5));

                    maxSeatsEvent.setText(String.valueOf(event.getMaxSeats()));
                    String gamesText = "";
                    for (int position = 0; position < event.getBoardGames().size(); position++) {
                        if (position == 0) {
                            gamesText = event.getBoardGames().get(position).getName();
                        } else {
                            gamesText = gamesText + ", " + event.getBoardGames().get(position).getName();
                        }
                    }
                    games.setText(gamesText);
                }
                else {
                    CustomParser customParser = new CustomParser();
                    List<User> users = customParser.getEventUsers(genericHttpService.getResponse());
                    userInvited = new ArrayList<>();
                    userParticipants = new ArrayList<>();
                    userRequests = new ArrayList<>();
                    String statusString = "";
                    for(User user : users) {
                        if(user.getStatusEvent().equals("PARTICIPANT")) {
                            userParticipants.add(user);
                        }
                        if(user.getStatusEvent().equals("INVITED")) {
                            userInvited.add(user);
                        }
                        if(user.getStatusEvent().equals("WAITING")) {
                            userRequests.add(user);
                        }
                        if(user.getId() == userId){
                            statusString = user.getStatusEvent();
                        }
                    }
                    UserEventAdapter adapterParticipants = new UserEventAdapter(this, 0, userParticipants,"Participants");
                    ListView listParticipants = (ListView) findViewById(R.id.listParticipants);
                    listParticipants.setAdapter(adapterParticipants);
                    if(userId == event.getUserCreator().getId()){
                        status.setText(statusString.substring(0, 1).toUpperCase() + statusString.substring(1).toLowerCase());
                        LinearLayout linearLayout = findViewById(R.id.layoutOptions);
                        linearLayout.setVisibility(View.GONE);
                        TextView options = findViewById(R.id.textOptions);
                        options.setVisibility(View.GONE);
                    }
                    else {
                        if(statusString.equals("")){
                            status.setText("None");
                        }
                        else {
                            status.setText(statusString.substring(0, 1).toUpperCase() + statusString.substring(1).toLowerCase());
                        }

                        switch (status.getText().toString()) {
                            case "Invited":
                                buttonOption1.setText("Accept");
                                buttonOption2.setText("Decline");
                                break;
                            case "Waiting":
                                buttonOption1.setText("Cancel");
                                buttonOption2.setVisibility(View.GONE);
                                break;
                            case "Participant":
                                buttonOption1.setText("Cancel");
                                buttonOption2.setVisibility(View.GONE);
                                break;
                            case "Interested":
                                buttonOption1.setText("Join");
                                buttonOption2.setText("Unfollow");
                                break;
                            case "None":
                                buttonOption1.setText("Join");
                                buttonOption2.setText("Interested");
                                break;
                        }

                        buttonOption1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                URL url = null;
                                try {
                                    url = new URL("http://" + Constant.IP + "/changeUserStatus");
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                switch (buttonOption1.getText().toString()) {
                                    case "Accept":
                                        List<Pair<String, String>> paramsAccept = new ArrayList<Pair<String, String>>();
                                        paramsAccept.add(new Pair<>("eventId",String.valueOf(event.getId())));
                                        paramsAccept.add(new Pair<>("userId",String.valueOf(userId)));
                                        paramsAccept.add(new Pair<>("option","Accept"));

                                        userStatusService = (UserStatusService) new UserStatusService(EventActivity.this.getApplicationContext(),"changeUserStatus", paramsAccept,taskChangeStatus).execute(url);
                                        break;
                                    case "Cancel":
                                        List<Pair<String, String>> paramsCancel = new ArrayList<Pair<String, String>>();
                                        paramsCancel.add(new Pair<>("eventId",String.valueOf(event.getId())));
                                        paramsCancel.add(new Pair<>("userId",String.valueOf(userId)));
                                        paramsCancel.add(new Pair<>("option","Cancel"));

                                        userStatusService = (UserStatusService) new UserStatusService(EventActivity.this.getApplicationContext(),"changeUserStatus", paramsCancel,taskChangeStatus).execute(url);
                                        break;
                                    case "Join":
                                        List<Pair<String, String>> paramsJoin = new ArrayList<Pair<String, String>>();
                                        paramsJoin.add(new Pair<>("eventId",String.valueOf(event.getId())));
                                        paramsJoin.add(new Pair<>("userId",String.valueOf(userId)));
                                        paramsJoin.add(new Pair<>("option","Join"));

                                        userStatusService = (UserStatusService) new UserStatusService(EventActivity.this.getApplicationContext(),"changeUserStatus", paramsJoin,taskChangeStatus).execute(url);
                                        break;
                                }
                            }
                        });
                        buttonOption2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                URL url = null;
                                try {
                                    url = new URL("http://" + Constant.IP + "/changeUserStatus");
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                switch (buttonOption2.getText().toString()) {
                                    case "Decline":
                                        List<Pair<String, String>> paramsDecline = new ArrayList<Pair<String, String>>();
                                        paramsDecline.add(new Pair<>("eventId",String.valueOf(event.getId())));
                                        paramsDecline.add(new Pair<>("userId",String.valueOf(userId)));
                                        paramsDecline.add(new Pair<>("option","Decline"));

                                        userStatusService = (UserStatusService) new UserStatusService(EventActivity.this.getApplicationContext(),"changeUserStatus", paramsDecline,taskChangeStatus).execute(url);
                                        break;
                                    case "Unfollow":
                                        List<Pair<String, String>> paramsCancel = new ArrayList<Pair<String, String>>();
                                        paramsCancel.add(new Pair<>("eventId",String.valueOf(event.getId())));
                                        paramsCancel.add(new Pair<>("userId",String.valueOf(userId)));
                                        paramsCancel.add(new Pair<>("option","Cancel"));

                                        userStatusService = (UserStatusService) new UserStatusService(EventActivity.this.getApplicationContext(),"changeUserStatus", paramsCancel,taskChangeStatus).execute(url);
                                        break;
                                    case "Interested":
                                        List<Pair<String, String>> paramsInterested = new ArrayList<Pair<String, String>>();
                                        paramsInterested.add(new Pair<>("eventId",String.valueOf(event.getId())));
                                        paramsInterested.add(new Pair<>("userId",String.valueOf(userId)));
                                        paramsInterested.add(new Pair<>("option","Interested"));

                                        userStatusService = (UserStatusService) new UserStatusService(EventActivity.this.getApplicationContext(),"changeUserStatus", paramsInterested,taskChangeStatus).execute(url);
                                        break;
                                }
                            }
                        });

                    }
                }
                break;
        }
    }

    @Override
    public void getLocation(LatLng latLng) {
        if(latLng != null){
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            latitude = latLng.latitude ;
            longitude = latLng.longitude;
        }
    }

    @Override
    public void TaskChangeStatus(String result) {
        switch (userStatusService.getResponseCode()) {
            case 200:
                if(userStatusService.getResponse().equals("Accept Successfully")) {
                    Toast.makeText(EventActivity.this, "You successfully accepted the invitation.",
                            Toast.LENGTH_LONG).show();
                    status.setText("Participant");
                    buttonOption1.setText("Cancel");
                    buttonOption2.setVisibility(View.GONE);

                    TextView listParticipants = findViewById(R.id.textParticipants);
                    listParticipants.setText("Another participants");
                }
                if(userStatusService.getResponse().equals("Cancel Successfully")) {
                    Toast.makeText(EventActivity.this, "You successfully cancelled the event.",
                            Toast.LENGTH_LONG).show();
                    buttonOption1.setText("Join");
                    buttonOption2.setVisibility(View.VISIBLE);
                    buttonOption2.setText("Interested");
                }
                if(userStatusService.getResponse().equals("Join Successfully")) {
                    Toast.makeText(EventActivity.this, "You successfully joined the event. Wait the creator's response.",
                            Toast.LENGTH_LONG).show();
                    status.setText("Waiting");
                    buttonOption1.setText("Cancel");
                    buttonOption2.setVisibility(View.GONE);
                }
                if(userStatusService.getResponse().equals("Decline Successfully")) {
                    Toast.makeText(EventActivity.this, "You successfully decline the invitation.",
                            Toast.LENGTH_LONG).show();
                    buttonOption1.setText("Join");
                    buttonOption2.setText("Interested");
                }
                if(userStatusService.getResponse().equals("Interested Successfully")) {
                    Toast.makeText(EventActivity.this, "The event was added to your favourites.",
                            Toast.LENGTH_LONG).show();
                    buttonOption1.setText("Join");
                    buttonOption2.setText("Unfollow");
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bucharest = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(bucharest).title(title));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( bucharest, 15.0f) );
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public int getUserId() {
        return userId;
    }

    public int getCreatorId() {
        return creatorId;
    }
}
