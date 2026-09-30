package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class Node(
    @SerializedName("accessibility_caption")
    var accessibilityCaption: Any,
    @SerializedName("coauthor_producers")
    var coauthorProducers: List<Any>,
    @SerializedName("comments_disabled")
    var commentsDisabled: Boolean,
    @SerializedName("dash_info")
    var dashInfo: DashInfo,
    @SerializedName("dimensions")
    var dimensions: Dimensions,
    @SerializedName("display_url")
    var displayUrl: String,
    @SerializedName("edge_liked_by")
    var edgeLikedBy: EdgeLikedBy,
    @SerializedName("edge_media_preview_like")
    var edgeMediaPreviewLike: EdgeMediaPreviewLike,
    @SerializedName("edge_media_to_caption")
    var edgeMediaToCaption: EdgeMediaToCaption,
    @SerializedName("edge_media_to_comment")
    var edgeMediaToComment: EdgeMediaToComment,
    @SerializedName("edge_media_to_tagged_user")
    var edgeMediaToTaggedUser: EdgeMediaToTaggedUser,
    @SerializedName("encoding_status")
    var encodingStatus: Any,
    @SerializedName("fact_check_information")
    var factCheckInformation: Any,
    @SerializedName("fact_check_overall_rating")
    var factCheckOverallRating: Any,
    @SerializedName("felix_profile_grid_crop")
    var felixProfileGridCrop: Any,
    @SerializedName("gating_info")
    var gatingInfo: Any,
    @SerializedName("has_audio")
    var hasAudio: Boolean,
    @SerializedName("has_upcoming_event")
    var hasUpcomingEvent: Boolean,
    @SerializedName("id")
    var id: String,
    @SerializedName("is_published")
    var isPublished: Boolean,
    @SerializedName("is_video")
    var isVideo: Boolean,
    @SerializedName("location")
    var location: Any,
    @SerializedName("media_overlay_info")
    var mediaOverlayInfo: Any,
    @SerializedName("media_preview")
    var mediaPreview: String,
    @SerializedName("owner")
    var owner: Owner,
    @SerializedName("product_type")
    var productType: String,
    @SerializedName("sharing_friction_info")
    var sharingFrictionInfo: SharingFrictionInfo,
    @SerializedName("shortcode")
    var shortcode: String,
    @SerializedName("taken_at_timestamp")
    var takenAtTimestamp: Int,
    @SerializedName("thumbnail_resources")
    var thumbnailResources: List<ThumbnailResource>,
    @SerializedName("thumbnail_src")
    var thumbnailSrc: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("tracking_token")
    var trackingToken: String,
    @SerializedName("__typename")
    var typename: String,
    @SerializedName("video_duration")
    var videoDuration: Double,
    @SerializedName("video_url")
    var videoUrl: String,
    @SerializedName("video_view_count")
    var videoViewCount: Int
)