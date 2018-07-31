package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vlad
 */

public class Assets implements Parcelable {

    public Assets(){}

    private CoverLarge coverLarge;

    public Assets(Parcel parcel){
        coverLarge = parcel.readParcelable(CoverLarge.class.getClassLoader());
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
