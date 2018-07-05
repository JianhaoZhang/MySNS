package project02.csc214.mysns.Fragments;

import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import project02.csc214.mysns.AppDatabase;
import project02.csc214.mysns.Fragments.FeedFragment.OnListFragmentInteractionListener;
import project02.csc214.mysns.R;
import project02.csc214.mysns.model.Feed;

import java.util.List;

import static android.view.View.GONE;


public class MyFeedRecyclerViewAdapter extends RecyclerView.Adapter<MyFeedRecyclerViewAdapter.ViewHolder> {

    private final List<Feed> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static final String DBNAME = "MainDB";

    public MyFeedRecyclerViewAdapter(List<Feed> feeds, OnListFragmentInteractionListener listener) {
        mValues = feeds;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues.get(position)!=null) {
            AppDatabase db = Room.databaseBuilder(holder.mView.getContext().getApplicationContext(),
                    AppDatabase.class, DBNAME).allowMainThreadQueries().build();
            holder.mItem = mValues.get(position);
            holder.mIdView.setText("PID: "+ mValues.get(position).getPid() +
                    " Sent By: "+db.userAccountDao().getById(holder.mItem.getUid()).getUsername());
            holder.mContentView.setText(mValues.get(position).getContent());
            String photo_url = db.profileDao().getById(holder.mItem.getUid()).getPhoto_url();
            String url = holder.mItem.getUrl();
            db.close();
            Bitmap bm = BitmapFactory.decodeFile(photo_url);
            holder.mImageView.setImageBitmap(bm);
            if (url!=null && url.length()>0) {
                Bitmap bmf = BitmapFactory.decodeFile(url);
                holder.mFeedImage.setImageBitmap(bmf);
            }else{
                holder.mFeedImage.setVisibility(View.GONE);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public final ImageView mFeedImage;
        public Feed mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.feed_photo);
            mFeedImage = (ImageView) view.findViewById(R.id.feed_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
