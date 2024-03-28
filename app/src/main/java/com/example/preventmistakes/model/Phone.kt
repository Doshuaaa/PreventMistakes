package com.example.preventmistakes.model

import java.io.Serializable

data class Phone (
    val name: String,
    val number: String,
    var blocked: Boolean
) : Serializable