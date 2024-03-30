package com.yanuar.githubliteandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserFav(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var avatarUrl: String? = null,
)
