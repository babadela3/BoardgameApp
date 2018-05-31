package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.EventCreatorMapsActivity;
import android.bg.ro.boardgame.InviteFriendActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.TaskChangeStatus;
import android.bg.ro.boardgame.services.UserStatusService;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserEventAdapter extends ArrayAdapter<User> implements TaskChangeStatus {

    private Context context;
    private List<User> users;
    private String typeUsers;
    private static LayoutInflater inflater = null;
    private TaskChangeStatus taskChangeStatus;
    private UserStatusService userStatusService;

    public UserEventAdapter(Context context, int textViewResourceId,List<User> users, String typeUsers) {
        super(context, textViewResourceId, users);
        try {
            this.context = context;
            this.users = users;
            this.typeUsers = typeUsers;
            this.taskChangeStatus = this;


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return users.size();
    }

    public User getItem(int position) {
        return users.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void TaskChangeStatus(String result) {
        switch (userStatusService.getResponseCode()) {
            case 200:
                if(userStatusService.getResponse().contains("Accept Join Request")){
                    int idUser = Integer.parseInt(userStatusService.getResponse().substring(20));
                    int position = -1;
                    for(int index = 0; index < users.size(); index++) {
                        if(users.get(index).getId() == idUser) {
                            position = index;
                            break;
                        }
                    }
                    Toast.makeText(context, users.get(position).getName() + " was added to event.",
                            Toast.LENGTH_LONG).show();
                    ((EventCreatorMapsActivity) context).getUserParticipants().add(users.get(position));
                    ((EventCreatorMapsActivity) context).getAdapterParticipants().notifyDataSetChanged();
                    users.remove(position);
                    notifyDataSetChanged();
                }
                if(userStatusService.getResponse().equals("Decline Join Request")){
                    Toast.makeText(context, "There are enough players.",
                            Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public static class ViewHolder {
        public TextView nameUser;
        public ImageView pictureUser;
        public Button acceptButton;
        public Button rejectButton;

    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        try {
            if(typeUsers.equals("Participants")) {
                if (convertView == null)
                    vi = inflater.inflate(R.layout.list_item_user_participant, null);
                else {
                    holder = (ViewHolder) vi.getTag();
                }

                holder = new ViewHolder();

                holder.nameUser = (TextView) vi.findViewById(R.id.userName);
                holder.pictureUser = (ImageView) vi.findViewById(R.id.userImg);
                holder.rejectButton = (Button) vi.findViewById(R.id.buttonEliminate);

                holder.nameUser.setText(users.get(position).getName());

                vi.setTag(holder);

                if(((EventCreatorMapsActivity) context).getUserId() != ((EventCreatorMapsActivity) context).getUserCreator().getId()){
                    holder.rejectButton.setVisibility(View.GONE);
                }
                else {
                    if(users.get(position).getId() == ((EventCreatorMapsActivity) context).getUserId()){
                        holder.rejectButton.setVisibility(View.GONE);
                    }
                    else {
                        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                URL url = null;
                                try {
                                    url = new URL("http://192.168.1.104:8182/changeUserStatus");
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                List<Pair<String, String>> paramsAccept = new ArrayList<Pair<String, String>>();
                                paramsAccept.add(new Pair<>("eventId",String.valueOf(((EventCreatorMapsActivity) context).getId())));
                                paramsAccept.add(new Pair<>("userId",String.valueOf(users.get(position).getId())));
                                paramsAccept.add(new Pair<>("option","Reject"));

                                userStatusService = (UserStatusService) new UserStatusService(context,"changeUserStatus", paramsAccept,taskChangeStatus).execute(url);


                                Toast.makeText(context, "You removed " + users.get(position).getName() + ".",
                                        Toast.LENGTH_LONG).show();
                                users.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
            if(typeUsers.equals("Requests")) {
                if (convertView == null)
                    vi = inflater.inflate(R.layout.list_item_user_event_request, null);
                else {
                    holder = (ViewHolder) vi.getTag();
                }

                holder = new ViewHolder();

                holder.nameUser = (TextView) vi.findViewById(R.id.userName);
                holder.pictureUser = (ImageView) vi.findViewById(R.id.userImg);
                holder.acceptButton = (Button) vi.findViewById(R.id.buttonAccept);
                holder.rejectButton = (Button) vi.findViewById(R.id.buttonEliminate);

                holder.nameUser.setText(users.get(position).getName());

                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        URL url = null;
                        try {
                            url = new URL("http://192.168.1.104:8182/changeUserStatus");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        List<Pair<String, String>> paramsAccept = new ArrayList<Pair<String, String>>();
                        paramsAccept.add(new Pair<>("eventId",String.valueOf(((EventCreatorMapsActivity) context).getId())));
                        paramsAccept.add(new Pair<>("userId",String.valueOf(users.get(position).getId())));
                        paramsAccept.add(new Pair<>("option","Accept Join"));

                        userStatusService = (UserStatusService) new UserStatusService(context,"changeUserStatus", paramsAccept,taskChangeStatus).execute(url);
                    }
                });

                holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        URL url = null;
                        try {
                            url = new URL("http://192.168.1.104:8182/changeUserStatus");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        List<Pair<String, String>> paramsAccept = new ArrayList<Pair<String, String>>();
                        paramsAccept.add(new Pair<>("eventId",String.valueOf(((EventCreatorMapsActivity) context).getId())));
                        paramsAccept.add(new Pair<>("userId",String.valueOf(users.get(position).getId())));
                        paramsAccept.add(new Pair<>("option","Reject"));

                        userStatusService = (UserStatusService) new UserStatusService(context,"changeUserStatus", paramsAccept,taskChangeStatus).execute(url);


                        Toast.makeText(context, "You removed " + users.get(position).getName() + ".",
                                Toast.LENGTH_LONG).show();
                        users.remove(position);
                        notifyDataSetChanged();
                    }
                });

                vi.setTag(holder);
            }


        } catch (Exception e) {


        }
        return vi;
    }
}
