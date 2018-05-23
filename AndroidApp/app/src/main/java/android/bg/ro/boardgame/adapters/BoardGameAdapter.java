package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.services.ImageLoader;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class BoardGameAdapter extends BaseAdapter {

    private final Context context;
    private final List<BoardGame> boardGames;
    public ImageLoader imageLoader;

    public BoardGameAdapter(Context context, List<BoardGame> boardGames) {
        this.context = context;
        this.boardGames = boardGames;
        this.imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return boardGames.size();
    }

    @Override
    public Object getItem(int position) {
        return boardGames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.grid_item_boardgame, null);

            // set value into textview
            //TextView textView = (TextView) gridView
             //       .findViewById(R.id.grid_item_label);
            //textView.setText(boardGames.get(position).getName());

            // set image based on selected text
            SquareImageView imageView = (SquareImageView) gridView
                    .findViewById(R.id.grid_item_image);

            //DisplayImage function from ImageLoader Class
            imageLoader.DisplayImage(boardGames.get(position).getPicture(), imageView);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
