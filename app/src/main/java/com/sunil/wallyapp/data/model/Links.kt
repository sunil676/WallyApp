package com.sunil.wallyapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links(
    @SerializedName("self")
    @Expose
    var self: String? = null,

    @SerializedName("html")
    @Expose
    var html: String? = null,

    @SerializedName("download")
    @Expose
    var download: String? = null,

    @SerializedName("download_location")
    @Expose
    var downloadLocation: String? = null

) : Parcelable