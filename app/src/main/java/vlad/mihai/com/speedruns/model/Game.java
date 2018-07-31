package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class Game implements Parcelable {

    @SerializedName("id")
    private String gameID;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("weblink")
    private String webLink;

    @SerializedName("assets")
    private Assets assets;

    public Game(){}

    public Game(Parcel parcel){
        assets = parcel.readParcelable(Assets.class.getClassLoader());
        gameID = parcel.readString();
        abbreviation = parcel.readString();
        webLink = parcel.readString();
    }

    public String getGameID() {
        return gameID;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(assets, i);
        parcel.writeString(gameID);
        parcel.writeString(abbreviation);
        parcel.writeString(webLink);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {

        @Override
        public Game createFromParcel(Parcel parcel) {
            return new Game(parcel);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

}
