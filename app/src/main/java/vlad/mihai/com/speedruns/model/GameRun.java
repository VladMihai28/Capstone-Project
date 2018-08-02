package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad
 */

public class GameRun implements Parcelable {

    @SerializedName("id")
    private String runID;

    @SerializedName("videos")
    private RunVideo runVideo;

    @SerializedName("players")
    private List<Player> players;

    @SerializedName("times")
    private Time time;

    @SerializedName("comment")
    private String comment;

    @SerializedName("date")
    private String date;

    public GameRun(){}

    public GameRun(Parcel parcel){
        runVideo = parcel.readParcelable(RunVideo.class.getClassLoader());
        this.runID = parcel.readString();
        players = new ArrayList<>();
        parcel.readList(players, Player.class.getClassLoader());
        time = parcel.readParcelable(Time.class.getClassLoader());
        this.comment = parcel.readString();
        this.date = parcel.readString();
    }

    public String getRunID() {
        return runID;
    }

    public RunVideo getRunVideo() {
        return runVideo;
    }

    public void setRunID(String runID) {
        this.runID = runID;
    }

    public void setRunVideo(RunVideo runVideo) {
        this.runVideo = runVideo;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Time getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(runVideo, i);
        parcel.writeString(runID);
        parcel.writeList(players);
        parcel.writeParcelable(time, i);
        parcel.writeString(comment);
        parcel.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<GameRun> CREATOR = new Parcelable.Creator<GameRun>() {

        @Override
        public GameRun createFromParcel(Parcel parcel) {
            return new GameRun(parcel);
        }

        @Override
        public GameRun[] newArray(int size) {
            return new GameRun[size];
        }
    };
}

