package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.ImageLoader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BoardgameFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boardgame, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        MenuActivity activity = (MenuActivity) getActivity();
        User user = activity.getUser();

        ImageLoader imageLoader = new ImageLoader(getActivity());

        TextView title = getView().findViewById(R.id.title);
        title.setText(user.getBoardGames().get(position).getName());
        TextView description = getView().findViewById(R.id.description);
        description.setText(user.getBoardGames().get(position).getDescription());
        ImageView imageView = getView().findViewById(R.id.imageBoardGame);
        imageLoader.DisplayImage(user.getBoardGames().get(position).getPicture(), imageView);
    }


}