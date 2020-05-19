package com.sunil.wallyapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photos(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("promoted_at")
    @Expose
    var promotedAt: String? = null,

    @SerializedName("width")
    @Expose
    var width: Int? = null,

    @SerializedName("height")
    @Expose
    var height: Int? = null,

    @SerializedName("color")
    @Expose
    var color: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("alt_description")
    @Expose
    var altDescription: String? = null,

    @SerializedName("urls")
    @Expose
    var urls: Urls? = null,

    @SerializedName("links")
    @Expose
    var links: Links? = null,

    @SerializedName("likes")
    @Expose
    var likes: Int? = null,

    @SerializedName("liked_by_user")
    @Expose
    var likedByUser: Boolean? = null,

    @SerializedName("user")
    @Expose
    var user: User? = null

) : Parcelable