package ee.baulsupp.lithokotlinapp.model

data class Url(
  val display_url: String? = null,
  val expanded_url: String? = null,
  val indices: List<Int?>? = null,
  val unwound: Unwound? = null,
  val url: String? = null
) {
  data class Unwound(
    val description: String? = null,
    val status: Int? = null,
    val title: String? = null,
    val url: String? = null
  )
}