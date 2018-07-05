package project02.csc214.mysns.Fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import project02.csc214.mysns.AppDatabase;
import project02.csc214.mysns.R;
import project02.csc214.mysns.model.Feed;
import project02.csc214.mysns.model.Profile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FeedFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    Button mSubmit;
    EditText mInput;
    ImageButton mTake;
    String mPhotoPath;
    private RecyclerView mRecyclerView;
    private MyFeedRecyclerViewAdapter mRecyclerViewAdapter;
    private OnListFragmentInteractionListener mListener;
    private static final String STATUS = "IsLogged";
    private static final String USER = "CurrentUser";
    private static final int RC_LOG = 7;
    private static final String DBNAME = "MainDB";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FeedFragment newInstance(int columnCount) {
        FeedFragment fragment = new FeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);


            mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            OnListFragmentInteractionListener olfil = (OnListFragmentInteractionListener) getActivity();
            AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                    AppDatabase.class, DBNAME).allowMainThreadQueries().build();
            ArrayList<Feed> feed_list = new ArrayList<Feed>();
            Profile profile = db.profileDao().getById(olfil.getUid());
            String liked_sequence = profile.getLiked_users();
            Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Integer>  like_list = gson.fromJson(liked_sequence, type);
            for (int i=0;i<like_list.size();i++){
                int temp = like_list.get(i);
                ArrayList<Feed> list_temp = (ArrayList<Feed>) db.feedDao().getAllByUid(temp);
                if (list_temp.size()>0){
                    feed_list.addAll(list_temp);
                }
            }
            Collections.sort(feed_list);
            Collections.reverse(feed_list);
            db.close();
            //
            mRecyclerViewAdapter = new MyFeedRecyclerViewAdapter(feed_list, mListener);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mSubmit = (Button)view.findViewById(R.id.feed_submit_button);
        mInput = (EditText) view.findViewById(R.id.feed_post);
        mTake = (ImageButton) view.findViewById(R.id.take_photo);

        mSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String post = mInput.getText().toString();
                if (post!=null){
                    AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                            AppDatabase.class, DBNAME).allowMainThreadQueries().build();
                    OnListFragmentInteractionListener olfil = (OnListFragmentInteractionListener) getActivity();
                    String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
                    Log.d("UID: ",String.valueOf(olfil.getUid()));
                    Feed newfeed = new Feed(olfil.getUid(),mInput.getText().toString(),timeStamp);
                    newfeed.setUrl(olfil.getPhoto());
                    olfil.setPhoto("");
                    db.feedDao().insert(newfeed);
                    db.close();
                }
                if (getActivity().getSupportFragmentManager().findFragmentByTag("ACTIVE_FUNCTION")!=null){
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_panel, new FeedFragment(),"ACTIVE_FUNCTION");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        mTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnListFragmentInteractionListener olfil = (OnListFragmentInteractionListener) getActivity();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File mPhotoFile = null;
                    try {
                        mPhotoFile = createImageFile();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Error creating file for picture",
                                Toast.LENGTH_LONG).show();
                    }
                    if (mPhotoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "project02.csc214.mysns.fileprovider", mPhotoFile);
                        olfil.setPhoto(mPhotoFile.getPath()) ;
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 0);
                    }
                } else {
                    Toast.makeText(getActivity(), "No camera app available to take photo!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
        public void bootFunction(Fragment fragment);
        public void removeFunction();
        public String getPhoto();
        public void setPhoto(String path);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "jpg_" + UUID.randomUUID();
        File image = File.createTempFile(imageFileName, ".jpg",getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        return image;
    }
}
