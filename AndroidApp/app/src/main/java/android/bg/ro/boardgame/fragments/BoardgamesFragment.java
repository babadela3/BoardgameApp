package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoardgamesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragments_boardgames, container, false);
    }
}