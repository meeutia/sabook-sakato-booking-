package com.example.sebook.data.model

data class ForumReviewResponse(
    val reviews: List<ForumReview>,
    val message: String,
    val success: Boolean
)

data class ForumReview(
    val id_review: String,
    val id_pengajuan: String,
    val review: String,
    val rating: Int,
    val created_at: String,
    val updated_at: String,
    val nama_user: String
)
