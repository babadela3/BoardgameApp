package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.NotificationAdapter;
import android.bg.ro.boardgame.models.Notification;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements TaskDelegate {

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private List<Notification> notifications;
    private ListView listView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final MenuActivity activity = (MenuActivity) getActivity();
        user = activity.getUser();
        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/allNotifications");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("id",String.valueOf(user.getId())));

        listView = (ListView) getView().findViewById(R.id.listview);
        genericHttpService = (GenericHttpService) new GenericHttpService(activity,"allNotifications", parameters,taskDelegate).execute(url);
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                CustomParser customParser = new CustomParser();
                notifications = customParser.getNotifications(genericHttpService.getResponse());
                NotificationAdapter adapter = new NotificationAdapter(getActivity(), 0, notifications);
                listView.setAdapter(adapter);
                break;
        }
    }
}
