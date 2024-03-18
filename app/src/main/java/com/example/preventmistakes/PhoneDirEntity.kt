package com.example.preventmistakes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_phone")
data class PhoneDirEntity (
    @PrimaryKey
    val phoneNumber: String,
    val name: String
)