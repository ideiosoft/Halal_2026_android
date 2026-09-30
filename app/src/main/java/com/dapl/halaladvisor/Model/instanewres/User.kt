package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("biography")
    var biography: String,
    @SerializedName("blocked_by_viewer")
    var blockedByViewer: Boolean,
    @SerializedName("business_address_json")
    var businessAddressJson: Any,
    @SerializedName("business_category_name")
    var businessCategoryName: String,
    @SerializedName("business_contact_method")
    var businessContactMethod: Any,
    @SerializedName("business_email")
    var businessEmail: Any,
    @SerializedName("business_phone_number")
    var businessPhoneNumber: Any,
    @SerializedName("category_enum")
    var categoryEnum: String,
    @SerializedName("category_name")
    var categoryName: String,
    @SerializedName("connected_fb_page")
    var connectedFbPage: Any,
    @SerializedName("country_block")
    var countryBlock: Boolean,
    @SerializedName("edge_felix_video_timeline")
    var edgeFelixVideoTimeline: EdgeFelixVideoTimeline,
    @SerializedName("edge_follow")
    var edgeFollow: EdgeFollow,
    @SerializedName("edge_followed_by")
    var edgeFollowedBy: EdgeFollowedBy,
    @SerializedName("edge_media_collections")
    var edgeMediaCollections: EdgeMediaCollections,
    @SerializedName("edge_mutual_followed_by")
    var edgeMutualFollowedBy: EdgeMutualFollowedBy,
    @SerializedName("edge_owner_to_timeline_media")
    var edgeOwnerToTimelineMedia: EdgeOwnerToTimelineMedia,
    @SerializedName("edge_related_profiles")
    var edgeRelatedProfiles: EdgeRelatedProfiles,
    @SerializedName("edge_saved_media")
    var edgeSavedMedia: EdgeSavedMedia,
    @SerializedName("external_url")
    var externalUrl: String,
    @SerializedName("external_url_linkshimmed")
    var externalUrlLinkshimmed: String,
    @SerializedName("fbid")
    var fbid: String,
    @SerializedName("followed_by_viewer")
    var followedByViewer: Boolean,
    @SerializedName("follows_viewer")
    var followsViewer: Boolean,
    @SerializedName("full_name")
    var fullName: String,
    @SerializedName("has_ar_effects")
    var hasArEffects: Boolean,
    @SerializedName("has_blocked_viewer")
    var hasBlockedViewer: Boolean,
    @SerializedName("has_channel")
    var hasChannel: Boolean,
    @SerializedName("has_clips")
    var hasClips: Boolean,
    @SerializedName("has_guides")
    var hasGuides: Boolean,
    @SerializedName("has_requested_viewer")
    var hasRequestedViewer: Boolean,
    @SerializedName("hide_like_and_view_counts")
    var hideLikeAndViewCounts: Boolean,
    @SerializedName("highlight_reel_count")
    var highlightReelCount: Int,
    @SerializedName("id")
    var id: String,
    @SerializedName("is_business_account")
    var isBusinessAccount: Boolean,
    @SerializedName("is_joined_recently")
    var isJoinedRecently: Boolean,
    @SerializedName("is_private")
    var isPrivate: Boolean,
    @SerializedName("is_professional_account")
    var isProfessionalAccount: Boolean,
    @SerializedName("is_verified")
    var isVerified: Boolean,
    @SerializedName("overall_category_name")
    var overallCategoryName: Any,
    @SerializedName("profile_pic_url")
    var profilePicUrl: String,
    @SerializedName("profile_pic_url_hd")
    var profilePicUrlHd: String,
    @SerializedName("pronouns")
    var pronouns: List<Any>,
    @SerializedName("requested_by_viewer")
    var requestedByViewer: Boolean,
    @SerializedName("restricted_by_viewer")
    var restrictedByViewer: Any,
    @SerializedName("should_show_category")
    var shouldShowCategory: Boolean,
    @SerializedName("should_show_public_contacts")
    var shouldShowPublicContacts: Boolean,
    @SerializedName("username")
    var username: String
)