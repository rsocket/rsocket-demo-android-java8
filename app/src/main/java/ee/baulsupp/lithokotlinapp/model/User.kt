package ee.baulsupp.lithokotlinapp.model

data class User(
  val description: String? = null,
  val followers_count: Int? = null,
  val id_str: String,
  val name: String,
  val profile_background_image_url_https: String? = null,
  val profile_image_url_https: String,
  val `protected`: Boolean,
  val screen_name: String,
  val url: String? = null,
  val verified: Boolean
)