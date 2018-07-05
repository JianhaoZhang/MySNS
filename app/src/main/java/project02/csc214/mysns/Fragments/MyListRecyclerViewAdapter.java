package project02.csc214.mysns.Fragments;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import project02.csc214.mysns.AppDatabase;
import project02.csc214.mysns.Fragments.ListFragment.OnListFragmentInteractionListener;
import project02.csc214.mysns.MainActivity;
import project02.csc214.mysns.R;
import project02.csc214.mysns.model.Profile;
import project02.csc214.mysns.model.UserAccount;



public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<UserAccount> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static final String DBNAME = "MainDB";

    public MyListRecyclerViewAdapter(List<UserAccount> users, OnListFragmentInteractionListener listener) {
        mValues = users;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AppDatabase db = Room.databaseBuilder(holder.mView.getContext().getApplicationContext(),
                AppDatabase.class, DBNAME).allowMainThreadQueries().build();
        if (mValues.get(position)!=null) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText("UID: "+ mValues.get(position).getUid());
            holder.mContentView.setText(mValues.get(position).getUsername());

            FeedFragment.OnListFragmentInteractionListener olfil = (FeedFragment.OnListFragmentInteractionListener) holder.mView.getContext();
            Profile profile = db.profileDao().getById(olfil.getUid());
            db.close();
            String liked_sequence = profile.getLiked_users();
            Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Integer>  like_list = gson.fromJson(liked_sequence, type);
            for (int i=0;i<like_list.size();i++){
                if (like_list.get(i)==holder.mItem.getUid()){
                    holder.mIdView.setTextColor(Color.MAGENTA);
                }
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment viewuser = new ProfileFragment();
                ListFragment.OnListFragmentInteractionListener olfil = (ListFragment.OnListFragmentInteractionListener) holder.mView.getContext();
                olfil.setViewId(holder.mItem.getUid());
                olfil.removeFunction();
                olfil.bootFunction(viewuser);
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
        public UserAccount mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.user_id);
            mContentView = (TextView) view.findViewById(R.id.user_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
