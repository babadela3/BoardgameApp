package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.InviteFriendActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.SelectPubActivity;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.Pub;
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

public class PubAdapter extends ArrayAdapter<Pub> {

    private Context context;
    private List<Pub> pubs;
    private static LayoutInflater inflater = null;

    public PubAdapter(Context context, int textViewResourceId, List<Pub> pubs) {
        super(context, textViewResourceId, pubs);
        try {
            this.context = context;
            this.pubs = pubs;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return pubs.size();
    }

    public Pub getItem(int position) {
        return pubs.get(position);
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
        public TextView namePub;
        public ImageView picturePub;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item_pub, null);
            }
            else {
                holder = (ViewHolder) vi.getTag();
            }

            holder = new ViewHolder();
            holder.namePub = (TextView) vi.findViewById(R.id.pubName);
            holder.picturePub = (ImageView) vi.findViewById(R.id.pubImg);

            vi.setTag(holder);

            holder.namePub.setText(pubs.get(position).getName());

        } catch (Exception e) {


        }
        return vi;
    }
}
