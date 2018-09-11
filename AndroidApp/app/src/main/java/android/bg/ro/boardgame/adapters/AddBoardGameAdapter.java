package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.AddBoardGamesActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.bg.ro.boardgame.models.BoardGame;
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

public class AddBoardGameAdapter extends ArrayAdapter<BoardGame> {

    private Context context;
    private List<BoardGame> boardGames;
    public ImageLoader imageLoader;
    private static LayoutInflater inflater = null;

    public AddBoardGameAdapter(Context context, int textViewResourceId,List<BoardGame> boardGames) {
        super(context, textViewResourceId, boardGames);
        try {
            this.context = context;
            this.boardGames = boardGames;
            this.imageLoader = new ImageLoader(context);

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return boardGames.size();
    }

    public BoardGame getItem(int position) {
        return boardGames.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView nameGame;
        public SquareImageView pictureGame;
        public CheckBox checkBox;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item_add_boardgame, null);

            } else {
                holder = (ViewHolder) vi.getTag();
            }
            holder = new ViewHolder();

            holder.nameGame = (TextView) vi.findViewById(R.id.gameName);
            holder.pictureGame = (SquareImageView) vi.findViewById(R.id.gameImg);
            holder.checkBox = (CheckBox) vi.findViewById(R.id.checkBox);

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if(isChecked) {
                        ((AddBoardGamesActivity) context).getBoardGames().get(position).setAdded(true);
                    }
                    else {
                        ((AddBoardGamesActivity) context).getBoardGames().get(position).setAdded(false);
                    }
                }
            });
            holder.checkBox.setChecked(((AddBoardGamesActivity) context).getBoardGames().get(position).isAdded());

            vi.setTag(holder);

            imageLoader.DisplayImage(boardGames.get(position).getPicture(), holder.pictureGame);
            holder.nameGame.setText(boardGames.get(position).getName());
            //holder.display_number.setText(lProducts.get(position).number);


        } catch (Exception e) {


        }
        return vi;
    }
}
