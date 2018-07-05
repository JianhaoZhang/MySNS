package project02.csc214.mysns.Fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import project02.csc214.mysns.AppDatabase;
import project02.csc214.mysns.R;
import project02.csc214.mysns.RegisterActivity;
import project02.csc214.mysns.model.Profile;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER = "CurrentUser";
    private static final String VIEWUSER = "ViewingProfile";
    private static final String DBNAME = "MainDB";


    ImageView mImage;
    TextView mName;
    TextView mBirth;
    TextView mHometown;
    TextView mBio;
    TextView mUid;
    Button mLike;
    Button mEdit;
    Button mUnlike;


    private OnProfileFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

            mImage = (ImageView) view.findViewById(R.id.profile_photo);
            mName = (TextView) view.findViewById(R.id.profile_name);
            mBirth = (TextView) view.findViewById(R.id.profile_birth);
            mHometown = (TextView) view.findViewById(R.id.profile_hometown);
            mBio = (TextView) view.findViewById(R.id.profile_bio);
            mUid = (TextView) view.findViewById(R.id.profile_id);
            mEdit = (Button) view.findViewById(R.id.profile_edit);
            mLike = (Button) view.findViewById(R.id.profile_like);
            mUnlike = (Button) view.findViewById(R.id.profile_unlike);

            mEdit.setVisibility(View.GONE);
            mLike.setVisibility(View.GONE);
            mUnlike.setVisibility(View.GONE);

        ListFragment.OnListFragmentInteractionListener olfil = (ListFragment.OnListFragmentInteractionListener) getActivity();
        final ProfileFragment.OnProfileFragmentInteractionListener opfil = (ProfileFragment.OnProfileFragmentInteractionListener) getActivity();
        if (opfil.getTriggered() == true){
            olfil.removeFunction();
            olfil.bootFunction(new EditProfileFragment());
        }

        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, DBNAME).allowMainThreadQueries().build();

            Log.d("View ID: ",opfil.getViewUser()+"");
            Profile profile = db.profileDao().getById(opfil.getViewUser());
            Log.d("Profile: ",profile.toString());
            db.close();

            if (profile!=null) {
                String image_url = profile.getPhoto_url();
                String name = profile.getFirstname() + " " + profile.getLastname();
                String birth = profile.getBirth_date();
                String hometown = profile.getHometown();
                String bio = profile.getBio();
                int uid = profile.getUid();

                Log.d("Got UID: ",uid+"");

                if (image_url != null) {
                    Log.d("Path:",image_url);
                    Bitmap bm = BitmapFactory.decodeFile(image_url);
                    mImage.setImageBitmap(bm);
                }
                if (name != null) {
                    mName.setText(name);
                }
                if (birth != null) {
                    mBirth.setText("Birthdate: "+birth);
                }
                if (hometown != null) {
                    mHometown.setText("Hometown: "+hometown);
                }
                if (bio != null) {
                    mBio.setText("Bio: "+bio);
                }
                if (uid+"" != null) {
                    mUid.setText("uid="+uid);
                }
            }
            String list="Uninitialized";
            Profile myprofile = db.profileDao().getById(olfil.getUid());
            list = myprofile.getLiked_users();
            boolean isliked = false;
            if (!list.equals("Uninitialized")){
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                ArrayList<Integer>  like_list = gson.fromJson(list, type);
                for (int i = 0;i<like_list.size();i++){
                    if (like_list.get(i)==opfil.getViewUser()){
                        isliked = true;
                    }
                }
            }
            if (olfil.getUid()!=opfil.getViewUser()){
                if (isliked == false) {
                    mLike.setVisibility(View.VISIBLE);
                }
                if (isliked == true){
                    mUnlike.setVisibility(View.VISIBLE);
                }
            }
            if (olfil.getUid()==opfil.getViewUser()){

                mEdit.setVisibility(View.VISIBLE);
            }

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditProfileFragment edit = new EditProfileFragment();
                    ListFragment.OnListFragmentInteractionListener olfil = (ListFragment.OnListFragmentInteractionListener) getActivity();
                    Log.d("Editing ","Profile");
                    opfil.setTriggered(true);
                    olfil.removeFunction();
                    olfil.bootFunction(edit);
                }
            });


            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListFragment.OnListFragmentInteractionListener olfil = (ListFragment.OnListFragmentInteractionListener) getActivity();
                    ProfileFragment.OnProfileFragmentInteractionListener opfil = (ProfileFragment.OnProfileFragmentInteractionListener) getActivity();
                    AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                            AppDatabase.class, DBNAME).allowMainThreadQueries().build();
                    Profile current = db.profileDao().getById(olfil.getUid());
                    String like_sequence = current.getLiked_users();
                    Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                    Gson gson = new Gson();
                    ArrayList<Integer>  like_list = gson.fromJson(like_sequence, type);
                    like_list.add(opfil.getViewUser());
                    Set<Integer> likesWithoutDuplicates = new LinkedHashSet<Integer>(like_list);
                    like_list.clear();
                    like_list.addAll(likesWithoutDuplicates);
                    String edited_sequence = gson.toJson(like_list);
                    current.setLiked_users(edited_sequence);
                    db.profileDao().update(current);
                    db.close();
                    Toast toast = Toast.makeText(getActivity(),"Liked",Toast.LENGTH_SHORT);
                    toast.show();
                    ProfileFragment profile = new ProfileFragment();
                    olfil.removeFunction();
                    olfil.bootFunction(profile);
                }
            });

            mUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListFragment.OnListFragmentInteractionListener olfil = (ListFragment.OnListFragmentInteractionListener) getActivity();
                    ProfileFragment.OnProfileFragmentInteractionListener opfil = (ProfileFragment.OnProfileFragmentInteractionListener) getActivity();
                    AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                            AppDatabase.class, DBNAME).allowMainThreadQueries().build();
                    Profile current = db.profileDao().getById(olfil.getUid());
                    String like_sequence = current.getLiked_users();
                    Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                    Gson gson = new Gson();
                    ArrayList<Integer>  like_list = gson.fromJson(like_sequence, type);
                    ArrayList<Integer>  new_list = new ArrayList<Integer>();
                    for (int i=0;i<like_list.size();i++){
                        if (like_list.get(i)!=opfil.getViewUser()){
                            new_list.add(like_list.get(i));
                        }
                    }
                    String edited_sequence = gson.toJson(new_list);
                    current.setLiked_users(edited_sequence);
                    db.profileDao().update(current);
                    db.close();
                    Toast toast = Toast.makeText(getActivity(),"UnLiked",Toast.LENGTH_SHORT);
                    toast.show();
                    ProfileFragment profile = new ProfileFragment();
                    olfil.removeFunction();
                    olfil.bootFunction(profile);
                }
            });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnProfileFragmentInteractionListener {
        public int getViewUser();
        public boolean getTriggered();
        public void setTriggered(boolean triggered);
    }
}
