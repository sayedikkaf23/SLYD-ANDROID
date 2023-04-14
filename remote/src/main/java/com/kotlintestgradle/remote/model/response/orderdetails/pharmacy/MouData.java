package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;


public class MouData implements Parcelable, ValidItem {

    public MouData() {
    }

    protected MouData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MouData> CREATOR = new Creator<MouData>() {
        @Override
        public MouData createFromParcel(Parcel in) {
            return new MouData(in);
        }

        @Override
        public MouData[] newArray(int size) {
            return new MouData[size];
        }
    };

    @Override
    public boolean isValid() {
        return true;
    }
}