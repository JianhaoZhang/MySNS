package project02.csc214.mysns.model;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserAccountDao {

    @Query("SELECT * FROM useraccount")
    List<UserAccount> getAll();

    @Query("SELECT * FROM useraccount WHERE uid LIKE (:id) limit 1")
    UserAccount getById(int id);

    @Query("SELECT * FROM useraccount WHERE username LIKE :username")
    UserAccount findByName(String username);

    @Query("SELECT * FROM useraccount WHERE email LIKE :email")
    List<UserAccount> findByEmail(String email);

    @Insert
    void insert(UserAccount useraccount);

    @Delete
    void delete(UserAccount useraccount);

    @Update
    void update(UserAccount userAccount);
}