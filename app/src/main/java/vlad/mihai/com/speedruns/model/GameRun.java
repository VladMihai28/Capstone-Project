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

    public GameRun(){}

    public GameRun(Parcel parcel){
        runVideo = parcel.readParcelable(RunVideo.class.getClassLoader());
        this.runID = parcel.readString();
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(runVideo, i);
        parcel.writeString(runID);
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

