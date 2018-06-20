package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.UserEventAdapter;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.Pub;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GoogleMapsService;
import android.bg.ro.boardgame.services.ImageLoader;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskChangeStatus;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.services.TaskGoogleMaps;
import android.bg.ro.boardgame.services.UserStatusService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
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

public class EventCreatorMapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskDelegate, TaskGoogleMaps, TaskChangeStatus {

    private GoogleMap mMap;
    private TaskDelegate taskDelegate;
    private TaskGoogleMaps taskGoogleMaps;
    private TaskChangeStatus taskChangeStatus;
    private GenericHttpService genericHttpService;
    private UserStatusService userStatusService;

    private int id;
    private String title;
    private String description;
    private String address;
    private String date;
    private String picture;
    private int maxSeats;
    private double latitude;
    private double longitude;
    private User userCreator;
    private ArrayList<User> userParticipants;
    private ArrayList<User> userInvited;
    private ArrayList<User> userRequests;
    private ArrayList<BoardGame> boardGames;
    private ArrayList<Friend> friends;
    private Pub pub;
    private int userId;
    private TextView status;
    private TextView friendsInvited;
    private Button buttonOption1;
    private Button buttonOption2;

    private UserEventAdapter adapterParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        ImageLoader imageLoader = new ImageLoader(EventCreatorMapsActivity.this);
        taskDelegate = this;

        id = bundle.getInt("id");
        userId = bundle.getInt("userId");
        maxSeats = bundle.getInt("maxSeats");
        title = bundle.getString("title");
        description = bundle.getString("description");
        address = bundle.getString("address");
        date = bundle.getString("date");
        picture = bundle.getString("picture");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        userCreator = (User) bundle.getSerializable("userCreator");
        boardGames = (ArrayList<BoardGame>) bundle.getSerializable("boardGames");
        friends = (ArrayList<Friend>) bundle.getSerializable("friends");
        pub = (Pub) bundle.getSerializable("pub");



        if(userId == userCreator.getId()) {
            setContentView(R.layout.activity_event_creator);
            TextView pubEvent = findViewById(R.id.pub);
            Button reservationButton = (Button) findViewById(R.id.reservationButton);

            if(pub != null){
                pubEvent.setText(pub.getName());
                taskGoogleMaps = this;
                List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                parameters.add(new Pair<>("location",address));
                new GoogleMapsService(EventCreatorMapsActivity.this,taskGoogleMaps,parameters).execute("");
            }
            else {
                pubEvent.setText("None");
                reservationButton.setVisibility(View.GONE);
            }

            friendsInvited = (TextView) findViewById(R.id.invited);
            Button inviteButton = (Button) findViewById(R.id.inviteFriendsButton);
            inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Friend> friendsList = friends;
                    for(int i = 0; i < friends.size(); i++){
                        for(int j = 0; j < userInvited.size(); j++) {
                            if(friends.get(i).getId() == userInvited.get(j).getId()){
                                friendsList.remove(i);
                            }
                        }
                        for(int j = 0; j < userParticipants.size(); j++) {
                            if(friends.get(i).getId() == userParticipants.get(j).getId()){
                                friendsList.remove(i);
                            }
                        }
                    }

                    Intent intent = new Intent("android.bg.ro.boardgame.InviteFriendActivity");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friends",friendsList);
                    bundle.putString("friendsNumber",String.valueOf(maxSeats - userParticipants.size() - userInvited.size()));
                    bundle.putString("option","SendInvitations");
                    bundle.putInt("eventId",id);
                    intent.putExtras(bundle);

                    startActivityForResult(intent,10);
                }
            });

            reservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    URL url = null;
                    try {
                        url = new URL("http://" + getResources().getString(R.string.localhost) + "/createReservation");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                    params.add(new Pair<>("eventId",String.valueOf(id)));
                    params.add(new Pair<>("pubId",String.valueOf(pub.getId())));

                    genericHttpService = (GenericHttpService) new GenericHttpService(EventCreatorMapsActivity.this.getApplicationContext(),"createReservation", params,taskDelegate).execute(url);

                }
            });
        }
        else {
            setContentView(R.layout.activity_event_no_creator);
            TextView pubEvent = findViewById(R.id.pub);
            taskChangeStatus = this;

            if(pub != null){
                pubEvent.setText(pub.getName());
                taskGoogleMaps = this;
                List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                parameters.add(new Pair<>("location",address));
                new GoogleMapsService(EventCreatorMapsActivity.this,taskGoogleMaps,parameters).execute("");
            }
            else {
                pubEvent.setText("None");
            }
            buttonOption1 = findViewById(R.id.buttonOption1);
            buttonOption2 = findViewById(R.id.buttonOption2);
        }
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/getUsersStatus");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<>("eventId",String.valueOf(id)));

        genericHttpService = (GenericHttpService) new GenericHttpService(EventCreatorMapsActivity.this.getApplicationContext(),"getUsersStatus", params,taskDelegate).execute(url);


        if(!picture.equals("null")){

            imageLoader.DisplayImage(picture, imageView);
        }

        titleEvent.setText(title);
        descriptionEvent.setText(description);
        userCreatorEvent.setText(userCreator.getName());
        addressEvent.setText(address);
        dateEvent.setText(date.substring(6));
        timeEvent.setText(date.substring(0,5));

        maxSeatsEvent.setText(String.valueOf(maxSeats));
        String gamesText = "";
        for(int position = 0; position < boardGames.size(); position++){
            if(position == 0){
                gamesText = boardGames.get(position).getName();
            }
            else {
                gamesText = gamesText + ", " + boardGames.get(position).getName();
            }
        }
        games.setText(gamesText);

    }

    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<User> getUserParticipants() {
        return userParticipants;
    }

    public void setUserParticipants(ArrayList<User> userParticipants) {
        this.userParticipants = userParticipants;
    }

    public int getId() {
        return id;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public UserEventAdapter getAdapterParticipants() {
        return adapterParticipants;
    }

    public void setAdapterParticipants(UserEventAdapter adapterParticipants) {
        this.adapterParticipants = adapterParticipants;
    }

    public void setId(int id) {
        this.id = id;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bucharest = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(bucharest).title(title));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( bucharest, 15.0f) );
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                switch (genericHttpService.getResponse()) {
                    case "Reservation successfully":
                        Toast.makeText(EventCreatorMapsActivity.this, "Reservation created.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case "Reservation failure" :
                        Toast.makeText(EventCreatorMapsActivity.this, "There are not enough players for the reservation.",
                                Toast.LENGTH_LONG).show();
                        break;
                    case "Reservation already exists" :
                        Toast.makeText(EventCreatorMapsActivity.this, "You already made the reservation.",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
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
                        if(userId == userCreator.getId()){
                            adapterParticipants = new UserEventAdapter(EventCreatorMapsActivity.this, 0, userParticipants,"Participants");
                            ListView listParticipants = (ListView) findViewById(R.id.listParticipants);
                            listParticipants.setAdapter(adapterParticipants);

                            UserEventAdapter adapterRequests = new UserEventAdapter(EventCreatorMapsActivity.this, 0, userRequests,"Requests");
                            ListView listRequests = (ListView) findViewById(R.id.listRequests);
                            listRequests.setAdapter(adapterRequests);

                            String friends = "";
                            for(int index = 0; index < userInvited.size(); index++) {
                                if(index == 0) {
                                    friends = userInvited.get(index).getName();
                                }
                                else {
                                    friends = friends + ", " + userInvited.get(index).getName();
                                }
                            }
                            if(friends.equals("")){
                                friendsInvited.setText("None");
                            }
                            else {
                                friendsInvited.setText(friends);
                            }
                        }
                        else {


                            UserEventAdapter adapterParticipants = new UserEventAdapter(EventCreatorMapsActivity.this, 0, userParticipants,"Participants");
                            ListView listParticipants = (ListView) findViewById(R.id.listParticipants);
                            listParticipants.setAdapter(adapterParticipants);
                            status.setText(statusString.substring(0, 1).toUpperCase() + statusString.substring(1).toLowerCase());

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
                            }

                            buttonOption1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    URL url = null;
                                    try {
                                        url = new URL("http://" + getResources().getString(R.string.localhost) + "/changeUserStatus");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    switch (buttonOption1.getText().toString()) {
                                        case "Accept":
                                            List<Pair<String, String>> paramsAccept = new ArrayList<Pair<String, String>>();
                                            paramsAccept.add(new Pair<>("eventId",String.valueOf(id)));
                                            paramsAccept.add(new Pair<>("userId",String.valueOf(userId)));
                                            paramsAccept.add(new Pair<>("option","Accept"));

                                            userStatusService = (UserStatusService) new UserStatusService(EventCreatorMapsActivity.this.getApplicationContext(),"changeUserStatus", paramsAccept,taskChangeStatus).execute(url);
                                            break;
                                        case "Cancel":
                                            List<Pair<String, String>> paramsCancel = new ArrayList<Pair<String, String>>();
                                            paramsCancel.add(new Pair<>("eventId",String.valueOf(id)));
                                            paramsCancel.add(new Pair<>("userId",String.valueOf(userId)));
                                            paramsCancel.add(new Pair<>("option","Cancel"));

                                            userStatusService = (UserStatusService) new UserStatusService(EventCreatorMapsActivity.this.getApplicationContext(),"changeUserStatus", paramsCancel,taskChangeStatus).execute(url);
                                            break;
                                        case "Join":
                                            List<Pair<String, String>> paramsJoin = new ArrayList<Pair<String, String>>();
                                            paramsJoin.add(new Pair<>("eventId",String.valueOf(id)));
                                            paramsJoin.add(new Pair<>("userId",String.valueOf(userId)));
                                            paramsJoin.add(new Pair<>("option","Join"));

                                            userStatusService = (UserStatusService) new UserStatusService(EventCreatorMapsActivity.this.getApplicationContext(),"changeUserStatus", paramsJoin,taskChangeStatus).execute(url);
                                            break;
                                    }
                                }
                            });
                            buttonOption2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    URL url = null;
                                    try {
                                        url = new URL("http://" + getResources().getString(R.string.localhost) + "/changeUserStatus");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    switch (buttonOption2.getText().toString()) {
                                        case "Decline":
                                            List<Pair<String, String>> paramsDecline = new ArrayList<Pair<String, String>>();
                                            paramsDecline.add(new Pair<>("eventId",String.valueOf(id)));
                                            paramsDecline.add(new Pair<>("userId",String.valueOf(userId)));
                                            paramsDecline.add(new Pair<>("option","Decline"));

                                            userStatusService = (UserStatusService) new UserStatusService(EventCreatorMapsActivity.this.getApplicationContext(),"changeUserStatus", paramsDecline,taskChangeStatus).execute(url);
                                            break;
                                        case "Unfollow":
                                            onBackPressed();
                                            break;
                                    }
                                }
                            });

                        }
                        break;
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void TaskChangeStatus(String result) {
        switch (userStatusService.getResponseCode()) {
            case 200:
                if(userStatusService.getResponse().equals("Accept Successfully")) {
                    Toast.makeText(EventCreatorMapsActivity.this, "You successfully accepted the invitation.",
                            Toast.LENGTH_LONG).show();
                    status.setText("Participant");
                    buttonOption1.setText("Cancel");
                    buttonOption2.setVisibility(View.GONE);

                    TextView listParticipants = findViewById(R.id.textParticipants);
                    listParticipants.setText("Another participants");
                }
                if(userStatusService.getResponse().equals("Cancel Successfully")) {
                    Toast.makeText(EventCreatorMapsActivity.this, "You successfully cancelled the event.",
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                if(userStatusService.getResponse().equals("Join Successfully")) {
                    Toast.makeText(EventCreatorMapsActivity.this, "You successfully joined the event. Wait the creator's response.",
                            Toast.LENGTH_LONG).show();
                    status.setText("Waiting");
                    buttonOption1.setText("Cancel");
                    buttonOption2.setVisibility(View.GONE);
                }
                if(userStatusService.getResponse().equals("Decline Successfully")) {
                    Toast.makeText(EventCreatorMapsActivity.this, "You successfully decline the invitation.",
                            Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if(resultCode == 20) {
                Bundle bundle = data.getExtras();
                ArrayList<Friend> invitedFriends = (ArrayList<Friend>) bundle.getSerializable("invitedFriends");
                if(invitedFriends != null){
                    if(invitedFriends.size() != 0) {
                        String textFriends = "";
                        for (int i = 0; i < invitedFriends.size(); i++) {
                            if (i == 0) {
                                textFriends = textFriends + invitedFriends.get(i).getName();
                            } else {
                                textFriends = textFriends + ", " + invitedFriends.get(i).getName();
                            }
                        }
                        if (friendsInvited.getText().toString().equals("None")) {
                            friendsInvited.setText(textFriends);
                        } else {
                            friendsInvited.setText(friendsInvited.getText().toString() + ", " + textFriends);
                        }
                    }
                }
            }
        }
    }
}
