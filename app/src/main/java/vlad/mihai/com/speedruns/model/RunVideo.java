package vlad.mihai.com.speedruns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad
 */

public class RunVideo implements Parcelable {

    @SerializedName("text")
    private String text;

    @SerializedName("links")
    private List<RunLink> runLinksList;

    public RunVideo(){}

    public RunVideo(Parcel parcel){
        this.text = parcel.readString();
        runLinksList = new ArrayList<>();
        parcel.readList(runLinksList, RunLink.class.getClassLoader());
    }

    public String getText() {
        return text;
    }

    public List<RunLink> getRunLinksList() {
        return runLinksList;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRunLinksList(List<RunLink> runLinksList) {
        this.runLinksList = runLinksList;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeList(runLinksList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RunVideo> CREATOR = new Parcelable.Creator<RunVideo>() {

        @Override
        public RunVideo createFromParcel(Parcel parcel) {
            return new RunVideo(parcel);
        }

        @Override
        public RunVideo[] newArray(int size) {
            return new RunVideo[size];
        }
    };


}
