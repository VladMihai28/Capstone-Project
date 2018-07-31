package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class RunPlace implements Parcelable {

    @SerializedName("place")
    private String place;

    @SerializedName("run")
    private GameRun gameRun;

    public RunPlace(){}

    public RunPlace(Parcel parcel){
        gameRun = parcel.readParcelable(GameRun.class.getClassLoader());
        place = parcel.readString();
    }

    public String getPlace() {
        return place;
    }

    public GameRun getGameRun() {
        return gameRun;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setGameRun(GameRun gameRun) {
        this.gameRun = gameRun;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(gameRun, i);
        parcel.writeString(place);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RunPlace> CREATOR = new Parcelable.Creator<RunPlace>() {

        @Override
        public RunPlace createFromParcel(Parcel parcel) {
            return new RunPlace(parcel);
        }

        @Override
        public RunPlace[] newArray(int size) {
            return new RunPlace[size];
        }
    };
}
