package com.sunil.wallyapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileImage(
    @SerializedName("small")
    @Expose
    var small: String? = null,

    @SerializedName("medium")
    @Expose
    var medium: String? = null,

    @SerializedName("large")
    @Expose
    var large: String? = null

) : Parcelable