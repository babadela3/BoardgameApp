package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.Friend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FriendAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private List<Friend> friends;
    private static LayoutInflater inflater = null;

    public FriendAdapter(Context context, int textViewResourceId,List<Friend> friends) {
        super(context, textViewResourceId, friends);
        try {
            this.context = context;
            this.friends = friends;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return friends.size();
    }

    public Friend getItem(int position) {
        return friends.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView nameFriend;
        public ImageView pictureFriend;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item_friend, null);
                holder = new ViewHolder();

                holder.nameFriend = (TextView) vi.findViewById(R.id.friendName);
                holder.pictureFriend = (ImageView) vi.findViewById(R.id.friendImg);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



            holder.nameFriend.setText(friends.get(position).getName());
            //holder.display_number.setText(lProducts.get(position).number);


        } catch (Exception e) {


        }
        return vi;
    }
}
