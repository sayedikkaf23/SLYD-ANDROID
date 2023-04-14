package com.customer.remote.http.model.response.getcart

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedAddOns(
  @SerializedName("name") @Expose val name: String,
  @SerializedName("id") @Expose val id: String,
  @SerializedName("addOnId") @Expose val addOnId: String,
  @SerializedName("addOnName") @Expose val addOnName: String,
  @SerializedName("price") @Expose val price: String
) : Parcelable