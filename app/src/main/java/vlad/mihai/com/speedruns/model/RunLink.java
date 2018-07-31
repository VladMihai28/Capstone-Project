package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class RunLink implements Parcelable {

    @SerializedName("uri")
    private String linkUri;

    public RunLink(){}

    public RunLink(Parcel parcel){
        this.linkUri = parcel.readString();
    }

    public String getLinkUri() {
        return linkUri;
    }

    public void setLinkUri(String linkUri) {
        this.linkUri = linkUri;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(linkUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RunLink> CREATOR = new Parcelable.Creator<RunLink>() {

        @Override
        public RunLink createFromParcel(Parcel parcel) {
            return new RunLink(parcel);
        }

        @Override
        public RunLink[] newArray(int size) {
            return new RunLink[size];
        }
    };
}
