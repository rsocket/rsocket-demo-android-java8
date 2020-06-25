package io.rsocket.demo.twitter.model

import ee.baulsupp.lithokotlinapp.model.Media

data class ExtendedEntities(
  val media: List<Media>? = null
) {

}