package android.bg.ro.boardgame.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.BoardGameAdapter;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.User;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BoardgamesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragments_boardgames, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        MenuActivity activity = (MenuActivity) getActivity();
        User user = activity.getUser();

        GridView gridView = (GridView)getView().findViewById(R.id.gridview);
        BoardGameAdapter boardGameAdapter = new BoardGameAdapter(getActivity(), user.getBoardGames());
        gridView.setAdapter(boardGameAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                BoardgameFragment boardgameFragment = new BoardgameFragment();
                boardgameFragment.setArguments(bundle);

                ProfileFragment parentFrag = ((ProfileFragment)getParentFragment());
                parentFrag.getFragmentManager().beginTransaction().replace(R.id.fragmentsMenu, boardgameFragment).addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Selected Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}