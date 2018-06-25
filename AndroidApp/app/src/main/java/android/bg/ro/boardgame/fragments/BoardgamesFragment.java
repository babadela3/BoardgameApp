package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.BoardGameSelectAdapter;
import android.bg.ro.boardgame.models.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
        final User user = activity.getUser();

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
    }
}