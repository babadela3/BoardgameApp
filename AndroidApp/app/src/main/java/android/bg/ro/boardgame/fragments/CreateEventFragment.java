package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.User;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateEventFragment extends Fragment {

    TextView numberOfPlayers;
    TextView invitedPeople;
    TextView selectedGames;
    User user;
    ArrayList<Friend> invitedFriends;
    ArrayList<BoardGame> addedBoardGames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final MenuActivity activity = (MenuActivity) getActivity();
        user = activity.getUser();

        Button plusButton = (Button) getView().findViewById(R.id.buttonPlus);
        Button minusButton = (Button) getView().findViewById(R.id.buttonMinus);
        Button inviteButton = (Button) getView().findViewById(R.id.buttonInviteFriends);
        numberOfPlayers = (TextView) getView().findViewById(R.id.numberPlayers);
        invitedPeople = (TextView) getView().findViewById(R.id.invitedPeople);

        Button addGamesButton = (Button) getView().findViewById(R.id.buttonAddBoardGames);
        selectedGames = (TextView) getView().findViewById(R.id.selectedGames);

        EditText date = (EditText) getView().findViewById(R.id.date);

        Button pubButton = (Button) getView().findViewById(R.id.pubButton);


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberOfPlayers.getText().toString());
                if(number < 99) {
                    numberOfPlayers.setText(String.valueOf(number + 1));
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberOfPlayers.getText().toString());
                if(number > 0) {
                    numberOfPlayers.setText(String.valueOf(number - 1));
                }
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.bg.ro.boardgame.InviteFriendActivity");
                Bundle bundle = new Bundle();
                ArrayList<Friend> friends = new ArrayList<>(user.getFriends());
                bundle.putSerializable("friends",friends);
                bundle.putString("friendsNumber",numberOfPlayers.getText().toString());
                intent.putExtras(bundle);

                startActivityForResult(intent,10);
            }
        });

        addGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.bg.ro.boardgame.AddBoardGamesActivity");
                Bundle bundle = new Bundle();
                ArrayList<BoardGame> boardGames = new ArrayList<>(user.getBoardGames());
                bundle.putSerializable("boardGames",boardGames);
                intent.putExtras(bundle);

                startActivityForResult(intent,10);
            }
        });

        pubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.bg.ro.boardgame.SelectPubActivity");
                startActivityForResult(intent,10);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if(resultCode == 20) {
                Bundle bundle = data.getExtras();
                invitedFriends = (ArrayList<Friend>) bundle.getSerializable("invitedFriends");
                if(invitedFriends.size() != 0){
                    String textFriends = "";
                    for(int i = 0; i < invitedFriends.size(); i++) {
                        if(i == 0) {
                            textFriends = textFriends + invitedFriends.get(i).getName();
                        }
                        else {
                            textFriends = textFriends + ", " + invitedFriends.get(i).getName();
                        }
                    }
                    invitedPeople.setText(textFriends);
                }
            }
            if(resultCode == 30) {
                Bundle bundle = data.getExtras();
                addedBoardGames = (ArrayList<BoardGame>) bundle.getSerializable("addedBoardGames");
                if(addedBoardGames.size() != 0){
                    String textGames = "";
                    for(int i = 0; i < addedBoardGames.size(); i++) {
                        if(i == 0) {
                            textGames = textGames + addedBoardGames.get(i).getName();
                        }
                        else {
                            textGames = textGames + ", " + addedBoardGames.get(i).getName();
                        }
                    }
                    selectedGames.setText(textGames);
                }
            }
        }
    }

}