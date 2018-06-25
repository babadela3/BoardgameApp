package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.forms.SquareImageView;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.services.BoardGameDetailsService;
import android.bg.ro.boardgame.services.ImageLoader;
import android.bg.ro.boardgame.services.TaskBoardGame;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BoardGameAdapter extends ArrayAdapter<BoardGame> implements TaskBoardGame{

    TaskBoardGame taskBoardGame;
    private Context context;
    private List<BoardGame> boardGames;
    private static LayoutInflater inflater = null;

    public BoardGameAdapter(Context context, int textViewResourceId,List<BoardGame> boardGames) {
        super(context, textViewResourceId, boardGames);
        try {
            this.context = context;
            this.boardGames = boardGames;
            this.taskBoardGame = this;
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
        public ImageView pictureGame;
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

            holder.nameGame = (TextView) vi.findViewById(R.id.eventName);
            holder.nameGame.setText(boardGames.get(position).getName());
            SquareImageView imageView = (SquareImageView) vi
                    .findViewById(R.id.eventImg);

            ImageLoader imageLoader = new ImageLoader(context,imageView);
            imageLoader.Display(context,boardGames.get(position).getId(), imageView);


        } catch (Exception e) {

        }
        return vi;
    }

    @Override
    public void searchGame(List<BoardGame> games) {
        for(BoardGame game : games) {
            for(BoardGame boardGame: boardGames) {
                if(game.getId() == boardGame.getId()){
                    boardGame.setPicture(game.getPicture());
                    this.notify();
                    break;
                }
            }
        }
    }
}
