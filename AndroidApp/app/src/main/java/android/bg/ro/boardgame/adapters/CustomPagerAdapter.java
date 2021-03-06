package android.bg.ro.boardgame.adapters;

import android.bg.ro.boardgame.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    int pubId;
    int[] mResources = {
            R.drawable.pub2_0,
            R.drawable.pub2_1,
            R.drawable.pub2_2,
            R.drawable.pub2_4,
    };
    int[] blackAndRed = {
            R.drawable.images,
            R.drawable.pub2_1,
            R.drawable.game_event
    };
    int[] nothing = {};

    public CustomPagerAdapter(Context context, int pubId) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pubId = pubId;
    }

    @Override
    public int getCount() {
        if(pubId == 3) {
            return mResources.length;
        }
        if(pubId == 4) {
            return blackAndRed.length;
        }
        return nothing.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        if(pubId == 3) {
            imageView.setImageResource(mResources[position]);
        }
        else {
            if(pubId == 4) {
                imageView.setImageResource(blackAndRed[position]);
            }
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}