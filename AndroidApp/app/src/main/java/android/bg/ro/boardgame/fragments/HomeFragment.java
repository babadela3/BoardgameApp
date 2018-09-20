package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.EventAdapter;
import android.bg.ro.boardgame.models.Event;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TaskDelegate {

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private List<Event> events;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final MenuActivity activity = (MenuActivity) getActivity();
        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/allEvents");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();

        listView = (ListView) getView().findViewById(R.id.listview);
        genericHttpService = (GenericHttpService) new GenericHttpService(activity,"allEvents", parameters,taskDelegate).execute(url);
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

                        Intent intent = new Intent("android.bg.ro.boardgame.EventActivity");
                        Bundle bundle = new Bundle();
                        bundle.putInt("userId",((MenuActivity) getActivity()).getUser().getId());
                        bundle.putInt("eventId",events.get(position).getId());
                        bundle.putString("name",events.get(position).getTitle());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                break;
        }
    }


}
