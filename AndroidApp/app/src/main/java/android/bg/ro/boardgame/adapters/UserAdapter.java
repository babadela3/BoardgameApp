package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.InviteFriendActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> users;
    private static LayoutInflater inflater = null;

    public UserAdapter(Context context, int textViewResourceId,List<User> users) {
        super(context, textViewResourceId, users);
        try {
            this.context = context;
            this.users = users;

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
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public static class ViewHolder {
        public TextView nameFriend;
        public ImageView pictureFriend;
        public CheckBox checkBox;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        FriendAdapter.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item_user, null);
            }
            else {
                holder = (FriendAdapter.ViewHolder) vi.getTag();
            }

            if(context instanceof InviteFriendActivity) {
                holder = new FriendAdapter.ViewHolder();

                holder.nameFriend = (TextView) vi.findViewById(R.id.friendName);
                holder.pictureFriend = (ImageView) vi.findViewById(R.id.friendImg);
                holder.checkBox = (CheckBox) vi.findViewById(R.id.checkBox);

                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        if(isChecked) {
                            ((InviteFriendActivity) context).getFriends().get(position).setHasInvited(true);
                        }
                        else {
                            ((InviteFriendActivity) context).getFriends().get(position).setHasInvited(false);

                        }
                    }
                });
            }
            else {
                holder = new FriendAdapter.ViewHolder();

                holder.nameFriend = (TextView) vi.findViewById(R.id.friendName);
                holder.pictureFriend = (ImageView) vi.findViewById(R.id.friendImg);
            }

            vi.setTag(holder);

            holder.nameFriend.setText(users.get(position).getName());
            //holder.display_number.setText(lProducts.get(position).number);

        } catch (Exception e) {


        }
        return vi;
    }
}
