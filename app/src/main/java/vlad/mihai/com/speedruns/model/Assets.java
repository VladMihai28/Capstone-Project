package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class Assets implements Parcelable {

    public Assets(){}

    @SerializedName("cover-large")
    private CoverLarge coverLarge;

    public Assets(Parcel parcel){
        coverLarge = parcel.readParcelable(CoverLarge.class.getClassLoader());
    }

    public CoverLarge getCoverLarge() {
        return coverLarge;
    }

    public void setCoverLarge(CoverLarge coverLarge) {
        this.coverLarge = coverLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(coverLarge, i);
    }

    public static final Parcelable.Creator<Assets> CREATOR = new Parcelable.Creator<Assets>() {

        @Override
        public Assets createFromParcel(Parcel parcel) {
            return new Assets(parcel);
        }

        @Override
        public Assets[] newArray(int size) {
            return new Assets[size];
        }
    };
}
