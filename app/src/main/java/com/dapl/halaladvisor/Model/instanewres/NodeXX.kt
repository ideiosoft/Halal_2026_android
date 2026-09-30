package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class NodeXX(
    @SerializedName("accessibility_caption")
    var accessibilityCaption: Any,
    @SerializedName("clips_music_attribution_info")
    var clipsMusicAttributionInfo: ClipsMusicAttributionInfo,
    @SerializedName("coauthor_producers")
    var coauthorProducers: List<Any>,
    @SerializedName("comments_disabled")
    var commentsDisabled: Boolean,
    @SerializedName("dash_info")
    var dashInfo: DashInfoX,
    @SerializedName("dimensions")
    var dimensions: DimensionsX,
    @SerializedName("display_url")
    var displayUrl: String,
    @SerializedName("edge_liked_by")
    var edgeLikedBy: EdgeLikedByX,
    @SerializedName("edge_media_preview_like")
    var edgeMediaPreviewLike: EdgeMediaPreviewLikeX,
    @SerializedName("edge_media_to_caption")
    var edgeMediaToCaption: EdgeMediaToCaptionX,
    @SerializedName("edge_media_to_comment")
    var edgeMediaToComment: EdgeMediaToCommentX,
    @SerializedName("edge_media_to_tagged_user")
    var edgeMediaToTaggedUser: EdgeMediaToTaggedUserX,
    @SerializedName("edge_sidecar_to_children")
    var edgeSidecarToChildren: EdgeSidecarToChildren,
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
    @SerializedName("is_video")
    var isVideo: Boolean,
    @SerializedName("location")
    var location: Location,
    @SerializedName("media_overlay_info")
    var mediaOverlayInfo: Any,
    @SerializedName("media_preview")
    var mediaPreview: Any,
    @SerializedName("owner")
    var owner: OwnerXX,
    @SerializedName("product_type")
    var productType: String,
    @SerializedName("sharing_friction_info")
    var sharingFrictionInfo: SharingFrictionInfoXX,
    @SerializedName("shortcode")
    var shortcode: String,
    @SerializedName("taken_at_timestamp")
    var takenAtTimestamp: Int,
    @SerializedName("thumbnail_resources")
    var thumbnailResources: List<ThumbnailResourceX>,
    @SerializedName("thumbnail_src")
    var thumbnailSrc: String,
    @SerializedName("tracking_token")
    var trackingToken: String,
    @SerializedName("__typename")
    var typename: String,
    @SerializedName("video_url")
    var videoUrl: String,
    @SerializedName("video_view_count")
    var videoViewCount: Int
)