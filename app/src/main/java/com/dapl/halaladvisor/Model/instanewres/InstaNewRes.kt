package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class InstaNewRes(
    @SerializedName("always_show_message_button_to_pro_account")
    var alwaysShowMessageButtonToProAccount: Boolean,
    @SerializedName("graphql")
    var graphql: Graphql,
    @SerializedName("logging_page_id")
    var loggingPageId: String,
    @SerializedName("profile_pic_edit_sync_props")
    var profilePicEditSyncProps: Any,
    @SerializedName("seo_category_infos")
    var seoCategoryInfos: List<List<String>>,
    @SerializedName("show_follow_dialog")
    var showFollowDialog: Boolean,
    @SerializedName("show_suggested_profiles")
    var showSuggestedProfiles: Boolean,
    @SerializedName("show_view_shop")
    var showViewShop: Boolean,
    @SerializedName("toast_content_on_load")
    var toastContentOnLoad: Any
)