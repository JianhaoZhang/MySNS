package project02.csc214.mysns.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Profile {

    public Profile(String firstname, String lastname, String birth_date, String hometown, String bio, String photo_url, String liked_users){
        this.firstname = firstname;
        this.lastname = lastname;
        this.birth_date = birth_date;
        this.hometown = hometown;
        this.bio = bio;
        this.photo_url = photo_url;
        this.liked_users = liked_users;

    }


    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private String firstname = "?";

    @ColumnInfo
    private String lastname = "?";

    @ColumnInfo
    private String birth_date = "?";

    @ColumnInfo
    private String hometown = "?";

    @ColumnInfo
    private String bio = "?";

    @ColumnInfo
    private String photo_url = "?";

    @ColumnInfo
    private String liked_users = "?";


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getLiked_users() {
        return liked_users;
    }

    public void setLiked_users(String liked_users) {
        this.liked_users = liked_users;
    }





}
