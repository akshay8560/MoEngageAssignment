package com.example.moengageassignment

data class ArticleData(
    val source: Source,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String?
)

data class ApiResponse(
    val status: String?,
    val articles: List<ArticleData>
)
