package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class GameName implements Parcelable {

    @SerializedName("international")
    private String internationalName;

    public String getInternationalName() {
        return internationalName;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public GameName(){}

    public GameName(Parcel parcel){
        this.internationalName = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(internationalName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<GameName> CREATOR = new Parcelable.Creator<GameName>() {

        @Override
        public GameName createFromParcel(Parcel parcel) {
            return new GameName(parcel);
        }

        @Override
        public GameName[] newArray(int size) {
            return new GameName[size];
        }
    };
}
