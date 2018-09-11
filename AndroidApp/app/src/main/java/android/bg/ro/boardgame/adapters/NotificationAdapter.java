package android.bg.ro.boardgame.adapters;

import android.annotation.SuppressLint;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.bg.ro.boardgame.models.Event;
import android.bg.ro.boardgame.models.Notification;
import android.bg.ro.boardgame.services.ImageLoader;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private Context context;
    private List<Notification> notifications;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public NotificationAdapter(Context context, int textViewResourceId,List<Notification> notifications) {
        super(context, textViewResourceId, notifications);
        try {
            this.context = context;
            this.notifications = notifications;
            this.imageLoader = new ImageLoader(context);

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return notifications.size();
    }

    public Notification getItem(int position) {
        return notifications.get(position);
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
        EventAdapter.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item_notification, null);

            } else {
                holder = (EventAdapter.ViewHolder) vi.getTag();
            }
            holder = new EventAdapter.ViewHolder();
            vi.setTag(holder);

            holder.nameEvent = (TextView) vi.findViewById(R.id.eventName);
            holder.nameEvent.setText(notifications.get(position).getMessage());
            SquareImageView imageView = (SquareImageView) vi
                    .findViewById(R.id.eventImg);
            if(notifications.get(position).getEventId() != 0){
                Resources resources = context.getResources();
                imageView.setImageDrawable(resources.getDrawable(R.drawable.game_event));
            }

        } catch (Exception e) {

        }
        return vi;
    }
}
