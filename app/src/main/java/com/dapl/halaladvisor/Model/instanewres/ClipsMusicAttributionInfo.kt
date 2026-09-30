package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class ClipsMusicAttributionInfo(
    @SerializedName("artist_name")
    var artistName: String,
    @SerializedName("audio_id")
    var audioId: String,
    @SerializedName("should_mute_audio")
    var shouldMuteAudio: Boolean,
    @SerializedName("should_mute_audio_reason")
    var shouldMuteAudioReason: String,
    @SerializedName("song_name")
    var songName: String,
    @SerializedName("uses_original_audio")
    var usesOriginalAudio: Boolean
)