package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.FriendAdapter;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.services.TaskChangeStatus;
import android.bg.ro.boardgame.services.UserStatusService;
import android.bg.ro.boardgame.utils.Constant;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InviteFriendActivity  extends AppCompatActivity implements TaskChangeStatus {

    private TaskChangeStatus taskChangeStatus;
    private UserStatusService userStatusService;
    private ArrayList<Friend> friends;
    private int maxPlayers;
    private ArrayList<Friend> invitedFriends;
    private String option;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        getSupportActionBar().hide();

        taskChangeStatus = this;
        Bundle bundle = getIntent().getExtras();

        friends = (ArrayList<Friend>) bundle.getSerializable("friends");
        final int maxPlayers = Integer.parseInt(bundle.getString("friendsNumber"));
        if(bundle.getString("option") != null){
            option = bundle.getString("option");
            eventId = bundle.getInt("eventId");
        }

        TextView text = (TextView) findViewById(R.id.text);
        text.setText("It is always a pleasure to play with your friends. Do not hesitate to contact them. You can invite just " + String.valueOf(maxPlayers) + " friends.");

        FriendAdapter adapter = new FriendAdapter(InviteFriendActivity.this, 0, friends);
        ListView listView = (ListView) findViewById(R.id.listFriends);
        listView.setAdapter(adapter);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(option == null){
                    invitedFriends = new ArrayList<>();
                    for(Friend friend : friends){
                        if(friend.isHasInvited()){
                            invitedFriends.add(friend);
                        }
                    }
                    if(invitedFriends.size() > maxPlayers){
                        Toast.makeText(InviteFriendActivity.this, "You have selected too many friends.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        onBackPressed();
                    }
                }
                else {
                    if(option.equals("SendInvitations")) {
                        invitedFriends = new ArrayList<>();
                        for(Friend friend : friends){
                            if(friend.isHasInvited()){
                                invitedFriends.add(friend);
                            }
                        }
                        if(invitedFriends.size() > maxPlayers){
                            Toast.makeText(InviteFriendActivity.this, "You have selected too many friends.",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            URL url = null;
                            try {
                                url = new URL("http://" + Constant.IP + "/changeUserStatus");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            for(Friend friend : friends){

                                List<Pair<String, String>> paramsAccept = new ArrayList<Pair<String, String>>();
                                paramsAccept.add(new Pair<>("eventId",String.valueOf(eventId)));
                                paramsAccept.add(new Pair<>("userId",String.valueOf(friend.getId())));
                                paramsAccept.add(new Pair<>("option","Invite"));

                                userStatusService = (UserStatusService) new UserStatusService(InviteFriendActivity.this.getApplicationContext(),"changeUserStatus", paramsAccept,taskChangeStatus).execute(url);
                            }
                            onBackPressed();
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("invitedFriends",invitedFriends);
        intent.putExtras(bundle);
        setResult(20, intent);
        super.onBackPressed();
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public void TaskChangeStatus(String result) {
        System.out.println(result);
    }
}