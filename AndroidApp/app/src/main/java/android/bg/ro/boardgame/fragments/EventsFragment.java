package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.EventAdapter;
import android.bg.ro.boardgame.models.Event;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment implements TaskDelegate{

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private List<Event> events;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final MenuActivity activity = (MenuActivity) getActivity();
        User user = activity.getUser();
        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/getUserEvents");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("userId",String.valueOf(user.getId())));

        listView = (ListView) getView().findViewById(R.id.listview);
        genericHttpService = (GenericHttpService) new GenericHttpService(getActivity(),"getUserEvents", parameters,taskDelegate).execute(url);


    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:

                CustomParser customParser = new CustomParser();
                events = customParser.getEvents(genericHttpService.getResponse());
                EventAdapter adapter = new EventAdapter(getActivity(), 0, events);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Bundle b = new Bundle();
                        b.putInt("id",(events.get(position).getId()));
                        b.putString("title",events.get(position).getTitle());
                        b.putString("description",events.get(position).getDescription());
                        b.putString("address",events.get(position).getAddress());
                        b.putString("date",events.get(position).getDate());
                        b.putSerializable("userCreator",events.get(position).getUserCreator());
                        b.putString("picture",events.get(position).getPicture());
                        b.putInt("maxSeats",events.get(position).getMaxSeats());
                        b.putDouble("latitude",events.get(position).getLatitude());
                        b.putDouble("longitude",events.get(position).getLongitude());
                        b.putSerializable("boardGames",events.get(position).getBoardGames());
                        b.putSerializable("pub",events.get(position).getPub());
                        b.putSerializable("friends",new ArrayList<>(((MenuActivity) getActivity()).getUser().getFriends()));
                        b.putInt("userId",((MenuActivity) getActivity()).getUser().getId());

                        Intent intent = new Intent("android.bg.ro.boardgame.EventCreatorMapsActivity");
                        intent.putExtras(b);

                        startActivity(intent);

                    }
                });
        }
    }
}