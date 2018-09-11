package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.BoardGameActivity;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.BoardGameSelectAdapter;
import android.bg.ro.boardgame.adapters.EventAdapter;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.User;
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
import android.widget.GridView;
import android.widget.ListView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BoardgamesFragment extends Fragment implements TaskDelegate {

    TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragments_boardgames, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        MenuActivity activity = (MenuActivity) getActivity();
        user = activity.getUser();
        taskDelegate = this;

        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/getUserInformation");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<>("userId",String.valueOf(user.getId())));

        genericHttpService = (GenericHttpService) new GenericHttpService(getActivity(),"getUserInformation", params,taskDelegate).execute(url);
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(genericHttpService.getResponse());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                CustomParser customParser = new CustomParser();
                user = customParser.getUser(json);

                GridView gridView = (GridView)getView().findViewById(R.id.gridview);
                BoardGameSelectAdapter boardGameSelectAdapter = new BoardGameSelectAdapter(getActivity(), user.getBoardGames());
                gridView.setAdapter(boardGameSelectAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent("android.bg.ro.boardgame.BoardGameActivity");
                        Bundle bundle = new Bundle();
                        bundle.putInt("position",position);
                        bundle.putInt("userId",user.getId());
                        bundle.putInt("gameId",user.getBoardGames().get(position).getId());
                        bundle.putString("name",user.getBoardGames().get(position).getName());
                        bundle.putString("description",user.getBoardGames().get(position).getDescription());
                        bundle.putString("picture",user.getBoardGames().get(position).getPicture());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                break;
        }
    }
}