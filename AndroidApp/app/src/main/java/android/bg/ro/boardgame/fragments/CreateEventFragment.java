package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.Pub;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateEventFragment extends Fragment implements TaskDelegate{

    TextView numberOfPlayers;
    TextView invitedPeople;
    TextView selectedGames;
    TextView selectedPub;
    TextView selectedAddress;
    double latitude;
    double longitude;
    User user;
    ArrayList<Friend> invitedFriends;
    ArrayList<BoardGame> addedBoardGames;
    Pub pub;

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;

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
        taskDelegate = this;

        Button plusButton = (Button) getView().findViewById(R.id.buttonPlus);
        Button minusButton = (Button) getView().findViewById(R.id.buttonMinus);
        Button inviteButton = (Button) getView().findViewById(R.id.buttonInviteFriends);
        numberOfPlayers = (TextView) getView().findViewById(R.id.numberPlayers);
        invitedPeople = (TextView) getView().findViewById(R.id.invitedPeople);

        Button addGamesButton = (Button) getView().findViewById(R.id.buttonAddBoardGames);
        selectedGames = (TextView) getView().findViewById(R.id.selectedGames);
        selectedPub = (TextView) getView().findViewById(R.id.selectedPub);
        selectedAddress = (TextView) getView().findViewById(R.id.selectedAddress);

        EditText date = (EditText) getView().findViewById(R.id.date);

        Button pubButton = (Button) getView().findViewById(R.id.pubButton);
        Button addressButton = (Button) getView().findViewById(R.id.addressButton);
        Button finishButton = (Button) getView().findViewById(R.id.finishButton);


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

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedPub.getText().toString().equals("None")) {
                    Intent intent = new Intent("android.bg.ro.boardgame.SelectAddressMapsActivity");
                    startActivityForResult(intent,10);
                }
                else {
                    Toast.makeText(getActivity(), "You already selected a pub.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                boolean isOk = true;

                String editName  = ((EditText) getView().findViewById(R.id.editName)).getText().toString();
                String editDescription  = ((EditText) getView().findViewById(R.id.editDescription)).getText().toString();
                String numberPlayers = numberOfPlayers.getText().toString();
                String friends = gson.toJson(invitedFriends);
                String games = gson.toJson(addedBoardGames);
                String pubId = "";
                String editAddrss = selectedAddress.getText().toString();
                String editLatitude = String.valueOf(latitude);
                String editLongitude = String.valueOf(longitude);

                if(editName.equals("")) {
                    isOk = false;
                    Toast.makeText(getActivity(), "Please enter a name.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(editDescription.equals("")) {
                    isOk = false;
                    Toast.makeText(getActivity(), "Please enter a description.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(selectedGames.getText().toString().equals("None")) {
                    isOk = false;
                    Toast.makeText(getActivity(), "Please select games.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                DateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.ENGLISH);
                String editDate = ((EditText) getView().findViewById(R.id.date)).getText().toString();
                Date date = null;
                try {
                    date = format.parse(editDate);
                } catch (ParseException e) {
                    isOk = false;
                    Toast.makeText(getActivity(), "Please respect date format.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Date current = new Date();
                if(date.before(current)){
                    isOk = false;
                    Toast.makeText(getActivity(), "Please enter a future date.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(editAddrss.equals("None")) {
                    isOk = false;
                    Toast.makeText(getActivity(), "Please enter an address.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(pub != null){
                    pubId = String.valueOf(pub.getId());
                }

                if(isOk) {
                    URL url = null;
                    try {
                        url = new URL("http://" + getResources().getString(R.string.localhost) + "/createEvent");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                    params.add(new Pair<>("name",editName));
                    params.add(new Pair<>("description",editDescription));
                    params.add(new Pair<>("numberPlayers",numberPlayers));
                    params.add(new Pair<>("friends",friends));
                    params.add(new Pair<>("games",games));
                    params.add(new Pair<>("date",editDate));
                    params.add(new Pair<>("pubId",pubId));
                    params.add(new Pair<>("address",editAddrss));
                    params.add(new Pair<>("latitude",editLatitude));
                    params.add(new Pair<>("longitude",editLongitude));
                    params.add(new Pair<>("creatorId",String.valueOf(user.getId())));

                    genericHttpService = (GenericHttpService) new GenericHttpService(getActivity(),"createEvent", params,taskDelegate).execute(url);
                }

            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberOfPlayers.getText().toString());
                if(number < 99) {
                    numberOfPlayers.setText(String.valueOf(number + 1));
                }
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
            if(resultCode == 40) {
                Bundle bundle = data.getExtras();
                pub = (Pub) bundle.getSerializable("selectedPub");
                if(pub != null){
                    selectedPub.setText(pub.getName());
                    selectedAddress.setText(pub.getAddress());
                }
                else {
                    selectedPub.setText("None");
                    selectedAddress.setText("None");
                }
            }
            if(resultCode == 50) {
                Bundle bundle = data.getExtras();
                if(bundle.getString("location") != null) {
                    selectedAddress.setText(bundle.getString("location"));
                    latitude = bundle.getDouble("latitude");
                    longitude = bundle.getDouble("longitude");
                }
            }
        }
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                Toast.makeText(getActivity(), "Event successfully created.",
                        Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getActivity(), "Event unsuccessfully created.",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}