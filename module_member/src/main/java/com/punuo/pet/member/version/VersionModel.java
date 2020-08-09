package com.punuo.pet.member.version;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.punuo.sys.sdk.model.BaseModel;

/**
 * Created by han.chen.
 * Date on 2020/8/3.
 **/
public class VersionModel extends BaseModel implements Parcelable {

    @SerializedName("versionCode")
    public int versionCode;

    @SerializedName("versionName")
    public String versionName;

    @SerializedName("url")
    public String downloadUrl;

    public String file;

    protected VersionModel(Parcel in) {
        versionCode = in.readInt();
        versionName = in.readString();
        downloadUrl = in.readString();
    }

    public static final Creator<VersionModel> CREATOR = new Creator<VersionModel>() {
        @Override
        public VersionModel createFromParcel(Parcel in) {
            return new VersionModel(in);
        }

        @Override
        public VersionModel[] newArray(int size) {
            return new VersionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(downloadUrl);
    }
}
