package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class UserName implements Parcelable {

    @SerializedName("international")
    private String internationalName;

    @SerializedName("japanese")
    private String japaneseName;

    public UserName(){}

    public UserName(Parcel parcel){
        this.internationalName = parcel.readString();
        this.japaneseName = parcel.readString();
    }

    public String getInternationalName() {
        return internationalName;
    }

    public String getJapaneseName() {
        return japaneseName;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public void setJapaneseName(String japaneseName) {
        this.japaneseName = japaneseName;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(internationalName);
        parcel.writeString(japaneseName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<UserName> CREATOR = new Parcelable.Creator<UserName>() {

        @Override
        public UserName createFromParcel(Parcel parcel) {
            return new UserName(parcel);
        }

        @Override
        public UserName[] newArray(int size) {
            return new UserName[size];
        }
    };
}
