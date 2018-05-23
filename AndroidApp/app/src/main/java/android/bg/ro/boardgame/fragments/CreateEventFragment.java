package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CreateEventFragment extends Fragment {

    TextView numberOfPlayers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button plusButton = (Button) getView().findViewById(R.id.buttonPlus);
        Button minusButton = (Button) getView().findViewById(R.id.buttonMinus);
        numberOfPlayers = (TextView) getView().findViewById(R.id.numberPlayers);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberOfPlayers.getText().toString());
                if(number < 99) {
                    numberOfPlayers.setText(String.valueOf(number + 1));
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(numberOfPlayers.getText().toString());
                if(number > 0) {
                    numberOfPlayers.setText(String.valueOf(number - 1));
                }
            }
        });
    }

}