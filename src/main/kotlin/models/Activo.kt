package org.example.models


import kotlinx.serialization.Serializable

@Serializable
data class Activo(
    val activoFijo: String,
    val nombreActivo: String,
    val nombreCC: String,
    val fechaAlta: String?
)