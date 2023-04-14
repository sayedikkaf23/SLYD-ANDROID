package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timestamps implements Parcelable, ValidItem {

	@Expose
	@SerializedName("confirm")
	private int confirm;

	@Expose
	@SerializedName("picked")
	private int picked;

	@Expose
	@SerializedName("inDispatch")
	private int inDispatch;

	@Expose
	@SerializedName("created")
	private int created;

	@Expose
	@SerializedName("readyForPickup")
	private int readyForPickup;

	@Expose
	@SerializedName("accepted")
	private int accepted;

	@Expose
	@SerializedName("cancelled")
	private int cancelled;

	@Expose
	@SerializedName("completed")
	private int completed;

	@Expose
	@SerializedName("packed")
	private int packed;

	public Timestamps(int picked, int inDispatch, int created, int readyForPickup, int accepted, int cancelled, int completed, int packed) {
		this.picked = picked;
		this.inDispatch = inDispatch;
		this.created = created;
		this.readyForPickup = readyForPickup;
		this.accepted = accepted;
		this.cancelled = cancelled;
		this.completed = completed;
		this.packed = packed;
	}

	protected Timestamps(Parcel in) {
		picked = in.readInt();
		inDispatch = in.readInt();
		created = in.readInt();
		readyForPickup = in.readInt();
		accepted = in.readInt();
		cancelled = in.readInt();
		completed = in.readInt();
		packed = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(picked);
		dest.writeInt(inDispatch);
		dest.writeInt(created);
		dest.writeInt(readyForPickup);
		dest.writeInt(accepted);
		dest.writeInt(cancelled);
		dest.writeInt(completed);
		dest.writeInt(packed);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Timestamps> CREATOR = new Creator<Timestamps>() {
		@Override
		public Timestamps createFromParcel(Parcel in) {
			return new Timestamps(in);
		}

		@Override
		public Timestamps[] newArray(int size) {
			return new Timestamps[size];
		}
	};

	public int getPicked() {
		return picked;
	}

	public void setPicked(int picked) {
		this.picked = picked;
	}

	public int getInDispatch() {
		return inDispatch;
	}

	public void setInDispatch(int inDispatch) {
		this.inDispatch = inDispatch;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getReadyForPickup() {
		return readyForPickup;
	}

	public void setReadyForPickup(int readyForPickup) {
		this.readyForPickup = readyForPickup;
	}

	public int getAccepted() {
		return accepted;
	}

	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}

	public int getCancelled() {
		return cancelled;
	}

	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getPacked() {
		return packed;
	}

	public void setPacked(int packed) {
		this.packed = packed;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}