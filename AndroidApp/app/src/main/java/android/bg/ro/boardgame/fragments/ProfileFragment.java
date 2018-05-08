package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MenuActivity activity = (MenuActivity) getActivity();
        User user = activity.getUser();

        ImageView profilePic = (ImageView) getView().findViewById(R.id.imgView);

        BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        Bitmap songImage = BitmapFactory.decodeByteArray(user.getProfilePicture(),0, user.getProfilePicture().length,options);//Decode image, "thumbnail" is the object of image file
        profilePic.setImageBitmap(songImage);

        //Add info to layout
        TextView name = (TextView) getView().findViewById(R.id.textName);
        name.setText(user.getName());
        TextView email =(TextView) getView().findViewById(R.id.textEmail);
        email.setText(user.getEmail());


        ImageButton eventsButton = (ImageButton) getView().findViewById(R.id.imageButtonEvents);
        ImageButton gamesButton = (ImageButton) getView().findViewById(R.id.imageButtonGames);
        ImageButton friendsButton = (ImageButton) getView().findViewById(R.id.imageButtonFriends);

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentForProfile, new EventsFragment()).addToBackStack(null).commit();
            }
        });

        gamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentForProfile, new BoardgamesFragment()).addToBackStack(null).commit();
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.fragmentForProfile, new FriendsFragment()).addToBackStack(null).commit();
            }
        });
    }

}