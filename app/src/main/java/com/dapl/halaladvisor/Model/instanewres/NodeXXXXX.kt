package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class NodeXXXXX(
    @SerializedName("accessibility_caption")
    var accessibilityCaption: Any,
    @SerializedName("dash_info")
    var dashInfo: DashInfoXX,
    @SerializedName("dimensions")
    var dimensions: DimensionsXX,
    @SerializedName("display_url")
    var displayUrl: String,
    @SerializedName("edge_media_to_tagged_user")
    var edgeMediaToTaggedUser: EdgeMediaToTaggedUserXX,
    @SerializedName("fact_check_information")
    var factCheckInformation: Any,
    @SerializedName("fact_check_overall_rating")
    var factCheckOverallRating: Any,
    @SerializedName("gating_info")
    var gatingInfo: Any,
    @SerializedName("has_audio")
    var hasAudio: Boolean,
    @SerializedName("has_upcoming_event")
    var hasUpcomingEvent: Boolean,
    @SerializedName("id")
    var id: String,
    @SerializedName("is_video")
    var isVideo: Boolean,
    @SerializedName("media_overlay_info")
    var mediaOverlayInfo: Any,
    @SerializedName("media_preview")
    var mediaPreview: String,
    @SerializedName("owner")
    var owner: OwnerX,
    @SerializedName("sharing_friction_info")
    var sharingFrictionInfo: SharingFrictionInfoX,
    @SerializedName("shortcode")
    var shortcode: String,
    @SerializedName("tracking_token")
    var trackingToken: String,
    @SerializedName("__typename")
    var typename: String,
    @SerializedName("video_url")
    var videoUrl: String,
    @SerializedName("video_view_count")
    var videoViewCount: Int
)