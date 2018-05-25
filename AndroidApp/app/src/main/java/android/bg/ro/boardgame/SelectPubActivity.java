package android.bg.ro.boardgame;

import android.bg.ro.boardgame.models.Pub;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectPubActivity extends AppCompatActivity {

    private ArrayList<Pub> pubs;
    private int maxPlayers;
    ArrayList<Pub> selectedPubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        getSupportActionBar().hide();

        TextView text = (TextView) findViewById(R.id.text);
        text.setText("It is always a pleasure to play with your friends.");

//        PubAdapter adapter = new PubAdapter(SelectPubActivity.this, 0, pubs);
//        ListView listView = (ListView) findViewById(R.id.listFriends);
//        listView.setAdapter(adapter);
//
//        Button button = findViewById(R.id.button);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                invitedFriends = new ArrayList<>();
//                for(Friend friend : friends){
//                    if(friend.isHasInvited()){
//                        invitedFriends.add(friend);
//                    }
//                }
//                if(invitedFriends.size() > maxPlayers){
//                    Toast.makeText(InviteFriendActivity.this, "You have selected too many friends.",
//                            Toast.LENGTH_LONG).show();
//                }
//                else {
//                    onBackPressed();
//                }
//            }
//        });

    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("invitedFriends",invitedFriends);
//        intent.putExtras(bundle);
//        setResult(20, intent);
//        super.onBackPressed();
//    }

    public ArrayList<Pub> getPubs() {
        return pubs;
    }

    public void setPubs(ArrayList<Pub> pubs) {
        this.pubs = pubs;
    }
}