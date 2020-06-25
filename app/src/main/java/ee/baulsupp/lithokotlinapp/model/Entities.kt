package ee.baulsupp.lithokotlinapp.model

import io.rsocket.demo.twitter.model.*

data class Entities(
  val media: List<Media>? = null,
  val hashtags: List<HashTag>? = null,
  val urls: List<Url>? = null,
  val userMention: List<UserMention>? = null,
  val symbols: List<Symbol>? = null
) {

}