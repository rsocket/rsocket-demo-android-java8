package ee.baulsupp.lithokotlinapp.model

data class Place(
  val bounding_box: BoundingBox? = null,
  val country: String? = null,
  val country_code: String? = null,
  val full_name: String? = null,
  val id: String? = null,
  val name: String? = null,
  val place_type: String? = null,
  val url: String? = null
) {
  data class BoundingBox(
    val coordinates: List<List<List<Double>>>,
    val type: String
  )
}