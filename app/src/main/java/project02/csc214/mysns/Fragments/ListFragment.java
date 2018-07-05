package project02.csc214.mysns.Fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import project02.csc214.mysns.AppDatabase;
import project02.csc214.mysns.R;
import project02.csc214.mysns.model.Feed;
import project02.csc214.mysns.model.UserAccount;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView mRecyclerView;
    private MyListRecyclerViewAdapter mRecyclerViewAdapter;
    private OnListFragmentInteractionListener mListener;
    private static final String STATUS = "IsLogged";
    private static final String USER = "CurrentUser";
    private static final int RC_LOG = 7;
    private static final String DBNAME = "MainDB";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);


            mRecyclerView = (RecyclerView) view.findViewById(R.id.user_list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            OnListFragmentInteractionListener olfil = (OnListFragmentInteractionListener) getActivity();
            AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                    AppDatabase.class, DBNAME).allowMainThreadQueries().build();
            ArrayList<UserAccount> user_list = (ArrayList<UserAccount>) db.userAccountDao().getAll();
            db.close();
            mRecyclerViewAdapter = new MyListRecyclerViewAdapter(user_list, mListener);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);

        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        public int getUid();
        public void setViewId(int vid);
        public void bootFunction(Fragment fragment);
        public void removeFunction();
    }
}
