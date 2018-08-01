package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class UserProfile implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("names")
    private UserName userName;

    public String getId() {
        return id;
    }

    public UserName getUserName() {
        return userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public UserProfile(){}

    public UserProfile(Parcel parcel){
        this.id = parcel.readString();
        this.userName = parcel.readParcelable(UserName.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(userName, i);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {

        @Override
        public UserProfile createFromParcel(Parcel parcel) {
            return new UserProfile(parcel);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
