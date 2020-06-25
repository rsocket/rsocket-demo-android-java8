package ee.baulsupp.lithokotlinapp.model

data class Tweet(
    val created_at: String? = null,
    val favorite_count: Int? = null,
    val id_str: String? = null,
    val in_reply_to_screen_name: String? = null,
    val in_reply_to_status_id_str: String? = null,
    val in_reply_to_user_id_str: String? = null,
    val is_quote_status: Boolean? = null,
    val lang: String? = null,
    val quote_count: Int? = null,
    val reply_count: Int? = null,
    val retweet_count: Int? = null,
    val retweeted: Boolean? = null,
    val source: String? = null,
    val text: String? = null,
    val timestamp_ms: String? = null,
    val truncated: Boolean? = null,
    val user: User?,
    val place: Place? = null,
    val entities: Entities? = null,
    val extended_entities: Entities? = null
) {
    val profilePic: String?
        get() = user?.profile_image_url_https?.replace("_normal", "_bigger")
}