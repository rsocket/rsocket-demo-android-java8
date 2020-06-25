package ee.baulsupp.lithokotlinapp.model

data class Media(
    val display_url: String? = null,
    val expanded_url: String? = null,
    val id: Long? = null,
    val id_str: String? = null,
    val indices: List<Int?>? = null,
    val media_url: String? = null,
    val media_url_https: String? = null,
    val sizes: Sizes? = null,
    val type: String? = null,
    val url: String? = null,
    val video_info: VideoInfo? = null
  ) {
    data class Sizes(
        val large: Size? = null,
        val medium: Size? = null,
        val small: Size? = null,
        val thumb: Size? = null
    ) {
      data class Size(
        val h: Int,
        val resize: String? = null,
        val w: Int
      )
    }
  }