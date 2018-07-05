package project02.csc214.mysns;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import project02.csc214.mysns.model.Feed;
import project02.csc214.mysns.model.FeedDao;
import project02.csc214.mysns.model.Profile;
import project02.csc214.mysns.model.ProfileDao;
import project02.csc214.mysns.model.UserAccount;
import project02.csc214.mysns.model.UserAccountDao;


@Database(entities = {Feed.class, Profile.class, UserAccount.class},version =1)


public abstract class AppDatabase extends RoomDatabase{

    public abstract FeedDao feedDao();
    public abstract ProfileDao profileDao();
    public abstract UserAccountDao userAccountDao();

}
