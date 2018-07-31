package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class CoverLarge implements Parcelable {

    public CoverLarge(){}

    @SerializedName("uri")
    private String coverLargerUri;

    public String getCoverLargerUri() {
        return coverLargerUri;
    }

    public void setCoverLargerUri(String coverLargerUri) {
        this.coverLargerUri = coverLargerUri;
    }

    public CoverLarge(Parcel parcel){
        coverLargerUri = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(coverLargerUri);
    }

    public static final Parcelable.Creator<CoverLarge> CREATOR = new Parcelable.Creator<CoverLarge>() {

        @Override
        public CoverLarge createFromParcel(Parcel parcel) {
            return new CoverLarge(parcel);
        }

        @Override
        public CoverLarge[] newArray(int size) {
            return new CoverLarge[size];
        }
    };
}
