package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad
 */

public class Leaderboard implements Parcelable {

    @SerializedName("game")
    private String gameID;
    @SerializedName("category")
    private String category;
    @SerializedName("runs")
    private List<RunPlace> gameRunList;

    public Leaderboard(){}

    public Leaderboard(Parcel parcel){
        this.gameID = parcel.readString();
        this.category = parcel.readString();
        gameRunList = new ArrayList<>();
        parcel.readList(gameRunList, RunPlace.class.getClassLoader());
    }

    public String getGameID() {
        return gameID;
    }

    public String getCategory() {
        return category;
    }

    public List<RunPlace> getGameRunList() {
        return gameRunList;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGameRunList(List<RunPlace> gameRunList) {
        this.gameRunList = gameRunList;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gameID);
        parcel.writeString(category);
        parcel.writeList(gameRunList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Leaderboard> CREATOR = new Parcelable.Creator<Leaderboard>() {

        @Override
        public Leaderboard createFromParcel(Parcel parcel) {
            return new Leaderboard(parcel);
        }

        @Override
        public Leaderboard[] newArray(int size) {
            return new Leaderboard[size];
        }
    };
}
