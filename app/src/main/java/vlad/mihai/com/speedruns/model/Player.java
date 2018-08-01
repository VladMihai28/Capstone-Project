package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class Player implements Parcelable {

    @SerializedName("rel")
    private String rel;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("uri")
    private String uri;

    public Player(){}

    public Player(Parcel parcel){
        this.rel = parcel.readString();
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.uri = parcel.readString();

    }

    public String getRel() {
        return rel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rel);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(uri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

        @Override
        public Player createFromParcel(Parcel parcel) {
            return new Player(parcel);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

}
