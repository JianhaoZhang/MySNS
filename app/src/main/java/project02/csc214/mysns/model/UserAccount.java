package project02.csc214.mysns.model;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserAccount {

    public UserAccount(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;

    }


    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private String username = "?";

    @ColumnInfo
    private String email = "?";

    @ColumnInfo
    private String password = "?";

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
