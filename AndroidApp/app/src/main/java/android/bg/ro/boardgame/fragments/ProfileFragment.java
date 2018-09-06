package android.bg.ro.boardgame.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.User;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private User user;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_profile, container, false);

        if(view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null)
            {
                parent.removeView(view);
            }
        }
        try
        {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
        }
        catch(InflateException e){
            // map is already there, just return view as it is
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final MenuActivity activity = (MenuActivity) getActivity();
        user = activity.getUser();

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
        Button buttonEdit = (Button) getView().findViewById(R.id.buttonEdit);
        Button buttonLogout = (Button) getView().findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }
                builder.setTitle("Log out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.bg.ro.boardgame.EditProfileActivity");
                Bundle bundle = new Bundle();
                bundle.putInt("userId",user.getId());
                bundle.putString("email",user.getEmail());
                bundle.putString("town",user.getTown());
                bundle.putString("name",user.getName());
                intent.putExtras(bundle);
                //startActivity(intent);
                startActivityForResult(intent,10);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 20) {
                Bundle bundle = data.getExtras();
                byte[] photoResult = bundle.getByteArray("photo");
                String townResult = bundle.getString("town");
                String nameResult = bundle.getString("name");

                if(townResult != null && nameResult != null) {
                    if(photoResult != null) {
                        user.setProfilePicture(photoResult);

                        ImageView profilePic = (ImageView) getView().findViewById(R.id.imgView);

                        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
                        options.inPurgeable = true; // inPurgeable is used to free up memory while required
                        Bitmap songImage = BitmapFactory.decodeByteArray(photoResult, 0, photoResult.length, options);//Decode image, "thumbnail" is the object of image file
                        profilePic.setImageBitmap(songImage);
                    }

                    user.setTown(townResult);
                    user.setName(nameResult);
                    TextView name = (TextView) getView().findViewById(R.id.textName);
                    name.setText(nameResult);

                }
            }
        }
    }
}