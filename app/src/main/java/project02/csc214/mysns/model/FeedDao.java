package project02.csc214.mysns.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FeedDao {

    @Query("SELECT * FROM feed")
    List<Feed> getAll();

    @Query("SELECT * FROM feed WHERE uid LIKE (:uid)")
    List<Feed> getAllByUid(int uid);

    @Query("SELECT * FROM feed WHERE pid LIKE (:pid) limit 1")
    Feed getByPid(int pid);

    @Insert
    void insert(Feed feed);

    @Delete
    void delete(Feed feed);

    @Update
    void update(Feed feed);

}
