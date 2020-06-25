package ee.baulsupp.lithokotlinapp.model

data class VideoInfo(
  val aspect_ratio: List<Int>? = null,
  val duration_millis: Int? = null,
  val variants: List<Variant>? = null
) {
  data class Variant(
    val bitrate: Int? = null,
    val content_type: String? = null,
    val url: String? = null
  )
}