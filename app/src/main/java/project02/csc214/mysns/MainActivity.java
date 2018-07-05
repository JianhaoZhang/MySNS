package project02.csc214.mysns;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

import project02.csc214.mysns.Fragments.FeedFragment;
import project02.csc214.mysns.Fragments.ListFragment;
import project02.csc214.mysns.Fragments.ProfileFragment;
import project02.csc214.mysns.model.Feed;
import project02.csc214.mysns.model.Profile;
import project02.csc214.mysns.model.UserAccount;

public class MainActivity extends AppCompatActivity implements FeedFragment.OnListFragmentInteractionListener,
        ListFragment.OnListFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener {

    private static final String STATUS = "IsLogged";
    private static final String USER = "CurrentUser";
    private static final String USERVIEW = "CurrentViewUser";
    private static final int RC_LOG = 7;
    private static final String DBNAME = "MainDB";
    private static final String FUNC = "CurrentFunction";
    private static final String TRIGGER= "OnEdit";
    private static final String PTEMP= "taking";
    private static final int FEED = 0;
    private static final int LIST = 1;
    private static final int PROFILE = 2;

    boolean mLog = false;
    int mCurrentUser = -1;
    int mCurrentFunc = FEED;
    int mViewUser = -1;
    boolean mEditTriggered = false;
    String mPhotoPath="";

    Button mFeedButton;
    Button mUserListButton;
    Button mProfileButton;
    Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null){
            mLog = savedInstanceState.getBoolean(STATUS);
            mCurrentUser = savedInstanceState.getInt(USER);
            mCurrentFunc = savedInstanceState.getInt(FUNC);
            mViewUser = savedInstanceState.getInt(USERVIEW);
            mEditTriggered = savedInstanceState.getBoolean(TRIGGER);
            mPhotoPath = savedInstanceState.getString(PTEMP);
            Log.d("FUNC: ",mCurrentFunc+"");
        }
        mLog = getIntent().getBooleanExtra(STATUS,false);
        mCurrentUser = getIntent().getIntExtra(USER,-1);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DBNAME).allowMainThreadQueries().build();
        if (db.userAccountDao().findByEmail("@").size()<1){
            UserAccount newuser = new UserAccount("Admin","@","1");
            db.userAccountDao().insert(newuser);
            Gson gson = new Gson();
            ArrayList<Integer> like_list = new ArrayList<Integer>();
            like_list.add(db.userAccountDao().findByEmail("@").get(0).getUid());
            String input = gson.toJson(like_list);
            db.profileDao().insert(new Profile(" "," "," "," "," "," ",input));

        }
        db.close();
        if (mLog == false){
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(login,RC_LOG);
        }else{
            if (mCurrentFunc == FEED) {
                FeedFragment feed = new FeedFragment();
                bootFunction(feed);
            }else if (mCurrentFunc == LIST) {
                ListFragment list = new ListFragment();
                bootFunction(list);
            }else if (mCurrentFunc == PROFILE) {
                ProfileFragment profile = new ProfileFragment();
                bootFunction(profile);
            }
        }
        mFeedButton = (Button) findViewById(R.id.feed_button);
        mUserListButton = (Button) findViewById(R.id.userlist_button);
        mProfileButton = (Button) findViewById(R.id.profile_button);
        mLogout = (Button) findViewById(R.id.log_out_button);



        mFeedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (getSupportFragmentManager().findFragmentByTag("ACTIVE_FUNCTION")!=null){
                    removeFunction();
                }
                    mCurrentFunc = FEED;
                    FeedFragment feed = new FeedFragment();
                    bootFunction(feed);

            }

        });

        mUserListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (getSupportFragmentManager().findFragmentByTag("ACTIVE_FUNCTION")!=null){
                    removeFunction();
                }
                mCurrentFunc = LIST;
                ListFragment list = new ListFragment();
                bootFunction(list);

            }

        });

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("ACTIVE_FUNCTION")!=null){
                    removeFunction();
                }
                mCurrentFunc = PROFILE;
                mViewUser = mCurrentUser;
                ProfileFragment profile = new ProfileFragment();
                bootFunction(profile);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra(STATUS,false);
                intent.putExtra(USER,-1);
                finish();
                startActivity(getIntent());
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == RESULT_OK && requestCode == RC_LOG) {
            mLog = result.getBooleanExtra(STATUS, false);
            mCurrentUser = result.getIntExtra(USER, -1);
            Intent intent= getIntent();
            intent.putExtra(STATUS,mLog);
            intent.putExtra(USER,mCurrentUser);
            intent.putExtra(FUNC,mCurrentFunc);
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState (Bundle Instancestate){
        super.onSaveInstanceState(Instancestate);
        Instancestate.putBoolean(STATUS,mLog);
        Instancestate.putInt(USER,mCurrentUser);
        Instancestate.putInt(FUNC,mCurrentFunc);
        Instancestate.putInt(USERVIEW,mViewUser);
        Instancestate.putBoolean(TRIGGER,mEditTriggered);
        Instancestate.putString(PTEMP,mPhotoPath);
        Log.d("On save FUNC: ",mCurrentFunc+"");
    }

    @Override
    public void bootFunction(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_panel, fragment,"ACTIVE_FUNCTION");
        fragmentTransaction.addToBackStack(null);
        Log.d("Ready: ","replace");
        fragmentTransaction.commit();
    }
    @Override
    public void removeFunction(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment func = getSupportFragmentManager().findFragmentByTag("ACTIVE_FUNCTION");
        fragmentTransaction.remove(func);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public String getPhoto() {
        return mPhotoPath;
    }

    @Override
    public void setPhoto(String path) {
        mPhotoPath = path;
    }

    @Override
    public int getUid(){
        return mCurrentUser;
    }

    @Override
    public void setViewId(int vid) {
        mViewUser = vid;
    }

    @Override
    public int getViewUser(){
        return mViewUser;
    }

    @Override
    public boolean getTriggered() {return mEditTriggered;}

    @Override
    public void setTriggered(boolean triggered) {
        mEditTriggered = triggered;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
