package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.FriendAdapter;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

public class FriendsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final MenuActivity activity = (MenuActivity) getActivity();
        User user = activity.getUser();

        FriendAdapter adapter = new FriendAdapter(getActivity(), 0, user.getFriends());
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putInt("idFriend",((MenuActivity) getActivity()).getUser().getFriends().get(position).getId());
                bundle.putInt("idUser",((MenuActivity) getActivity()).getUser().getId());
                bundle.putString("nameFriend",((MenuActivity) getActivity()).getUser().getFriends().get(position).getName());
                bundle.putInt("position",position);

                Intent intent = new Intent("android.bg.ro.boardgame.ChatActivity");
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });
    }
}