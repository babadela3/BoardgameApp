package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.FriendAdapter;
import android.bg.ro.boardgame.models.Friend;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendActivity  extends AppCompatActivity {

    private ArrayList<Friend> friends;
    private int maxPlayers;
    ArrayList<Friend> invitedFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();

        friends = (ArrayList<Friend>) bundle.getSerializable("friends");
        final int maxPlayers = Integer.parseInt(bundle.getString("friendsNumber"));

        TextView text = (TextView) findViewById(R.id.text);
        text.setText("It is always a pleasure to play with your friends. Do not hesitate to contact them. You can invite just " + String.valueOf(maxPlayers) + " friends.");

        FriendAdapter adapter = new FriendAdapter(InviteFriendActivity.this, 0, friends);
        ListView listView = (ListView) findViewById(R.id.listFriends);
        listView.setAdapter(adapter);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}