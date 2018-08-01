package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vlad
 */

public class Time implements Parcelable {

    @SerializedName("primary_t")
    private String primary_t;

    public Time(){}

    public Time(Parcel parcel){
        this.primary_t = parcel.readString();
    }

    public String getPrimary_t() {
        return primary_t;
    }

    public void setPrimary_t(String primary_t) {
        this.primary_t = primary_t;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(primary_t);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Time> CREATOR = new Parcelable.Creator<Time>() {

        @Override
        public Time createFromParcel(Parcel parcel) {
            return new Time(parcel);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };
}
