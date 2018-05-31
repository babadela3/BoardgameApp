package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.AddBoardGamesActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.bg.ro.boardgame.models.Event;
import android.bg.ro.boardgame.services.ImageLoader;
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

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public EventAdapter(Context context, int textViewResourceId,List<Event> events) {
        super(context, textViewResourceId, events);
        try {
            this.context = context;
            this.events = events;
            this.imageLoader = new ImageLoader(context);

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return events.size();
    }

    public Event getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView nameEvent;
        public ImageView pictureEvent;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item_event, null);

            } else {
                holder = (ViewHolder) vi.getTag();
            }
            holder = new ViewHolder();
            vi.setTag(holder);

            holder.nameEvent = (TextView) vi.findViewById(R.id.eventName);
            holder.nameEvent.setText(events.get(position).getTitle());
            SquareImageView imageView = (SquareImageView) vi
                    .findViewById(R.id.eventImg);
            if(!events.get(position).getPicture().equals("null")){

                imageLoader.DisplayImage(events.get(position).getPicture(), imageView);
            }
        } catch (Exception e) {

        }
        return vi;
    }
}
