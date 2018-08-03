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

    private String categoryName;

    @SerializedName("runs")
    private List<RunPlace> runPlaceList;

    public Leaderboard(){}

    public Leaderboard(Parcel parcel){
        this.gameID = parcel.readString();
        this.category = parcel.readString();
        runPlaceList = new ArrayList<>();
        parcel.readList(runPlaceList, RunPlace.class.getClassLoader());
        categoryName = parcel.readString();
    }

    public String getGameID() {
        return gameID;
    }

    public String getCategory() {
        return category;
    }

    public List<RunPlace> getRunPlaceList() {
        return runPlaceList;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRunPlaceList(List<RunPlace> runPlaceList) {
        this.runPlaceList = runPlaceList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gameID);
        parcel.writeString(category);
        parcel.writeList(runPlaceList);
        parcel.writeString(categoryName);
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
