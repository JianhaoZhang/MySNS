package project02.csc214.mysns.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Feed implements Comparable<Feed>{

    public Feed(int uid, String content, String time_stamp){
        this.uid = uid;
        this.content = content;
        this.time_stamp = time_stamp;
    }

    @PrimaryKey(autoGenerate = true)
    private int pid;

    @ColumnInfo
    private int uid;

    @ColumnInfo
    private String content;

    @ColumnInfo
    private String time_stamp;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ColumnInfo
    private String url;

    @Override
    public int compareTo(Feed feed) {
        // usually toString should not be used,
        // instead one of the attributes or more in a comparator chain
        return new Integer(getPid()).compareTo(new Integer(feed.getPid()));
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
