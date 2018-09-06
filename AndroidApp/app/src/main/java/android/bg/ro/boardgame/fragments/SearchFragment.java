package android.bg.ro.boardgame.fragments;

import android.Manifest;
import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.BoardGameAdapter;
import android.bg.ro.boardgame.adapters.EventAdapter;
import android.bg.ro.boardgame.adapters.PubAdapter;
import android.bg.ro.boardgame.adapters.UserAdapter;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Event;
import android.bg.ro.boardgame.models.Pub;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.BoardGameService;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskBoardGame;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements OnMapReadyCallback, TaskDelegate, TaskBoardGame{

    TaskBoardGame taskBoardGame;
    TaskDelegate taskDelegate;
    private GoogleMap mMap;
    private GenericHttpService genericHttpService;
    private List<Event> events;
    private List<User> users;
    private List<Pub> pubs;
    private ListView listView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        taskDelegate = this;
        taskBoardGame = this;
        MenuActivity activity = (MenuActivity) getActivity();
        user = activity.getUser();

        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/allEvents");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        genericHttpService = (GenericHttpService) new GenericHttpService(getActivity(),"allEvents", parameters,taskDelegate).execute(url);

        Button buttonSearch = (Button) getView().findViewById(R.id.buttonSearch);
        listView = (ListView) getView().findViewById(R.id.listview);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mapping = "";
                EditText search = getView().findViewById(R.id.textSearch);
                CheckBox events = getView().findViewById(R.id.checkBoxEvents);
                CheckBox users = getView().findViewById(R.id.checkBoxUsers);
                CheckBox pubs = getView().findViewById(R.id.checkBoxPubs);
                CheckBox games = getView().findViewById(R.id.checkBoxGames);

                if(events.isChecked()){
                    mapping = "/getEventsByName";
                }
                if(users.isChecked()){
                    mapping = "/getUsersByName";
                }
                if(pubs.isChecked()){
                    mapping = "/getPubsByName";
                }
                if(games.isChecked()){
                    List<Pair<String, String>> parameters = new ArrayList<>();
                    parameters.add(new Pair<>("search", search.getText().toString()));
                    BoardGameService boardGameService = (BoardGameService) new BoardGameService(getActivity(), parameters,taskBoardGame).execute();
                }
                else {

                    URL url = null;
                    try {
                        url = new URL("http://" + Constant.IP + mapping);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> parameters = new ArrayList<>();
                    parameters.add(new Pair<>("name", search.getText().toString()));
                    genericHttpService = (GenericHttpService) new GenericHttpService(getActivity(), mapping.substring(1), parameters, taskDelegate).execute(url);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Bucharest and move the camera
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mMap.setMyLocationEnabled(true);
        LatLng bucharest = new LatLng(44.426767,26.102538);
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( bucharest, 14.0f) );
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                if(genericHttpService.getMapping().equals("allEvents")) {
                    CustomParser customParser = new CustomParser();
                    events = customParser.getEvents(genericHttpService.getResponse());
                    for(Event event : events) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).title(event.getTitle()));
                    }
                }
                if(genericHttpService.getMapping().equals("getEventsByName")) {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.fragment_map);
                    mapFragment.getView().setVisibility(View.GONE);
                    CustomParser customParser = new CustomParser();
                    events = customParser.getEvents(genericHttpService.getResponse());
                    EventAdapter adapter = new EventAdapter(getActivity(), 0, events);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Intent intent = new Intent("android.bg.ro.boardgame.EventActivity");
                            Bundle bundle = new Bundle();
                            bundle.putInt("userId",user.getId());
                            bundle.putInt("eventId",events.get(position).getId());
                            bundle.putString("name",events.get(position).getTitle());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                if(genericHttpService.getMapping().equals("getUsersByName")) {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.fragment_map);
                    mapFragment.getView().setVisibility(View.GONE);
                    CustomParser customParser = new CustomParser();
                    users = customParser.getUsers(genericHttpService.getResponse());
                    UserAdapter adapter = new UserAdapter(getActivity(),0,users);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Intent intent = new Intent("android.bg.ro.boardgame.UserActivity");
                            Bundle bundle = new Bundle();
                            bundle.putInt("userId",user.getId());
                            bundle.putInt("searchUserId",users.get(position).getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                if(genericHttpService.getMapping().equals("getPubsByName")) {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.fragment_map);
                    mapFragment.getView().setVisibility(View.GONE);
                    CustomParser customParser = new CustomParser();
                    pubs = customParser.getPubs(genericHttpService.getResponse());
                    PubAdapter adapter = new PubAdapter(getActivity(),0,pubs);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Intent intent = new Intent("android.bg.ro.boardgame.PubActivity");
                            Bundle bundle = new Bundle();
                            bundle.putInt("pubId",pubs.get(position).getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void searchGame(final List<BoardGame> boardGames) {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getView().setVisibility(View.GONE);
        BoardGameAdapter adapter = new BoardGameAdapter(getActivity(),0,boardGames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent("android.bg.ro.boardgame.BoardGameActivity");
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                bundle.putInt("userId",user.getId());
                bundle.putInt("gameId",boardGames.get(position).getId());
                bundle.putString("name",boardGames.get(position).getName());
                bundle.putString("description",boardGames.get(position).getDescription());
                bundle.putString("picture",boardGames.get(position).getPicture());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
