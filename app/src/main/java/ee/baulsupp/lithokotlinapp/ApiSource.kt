package ee.baulsupp.lithokotlinapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ee.baulsupp.lithokotlinapp.model.Tweet
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.CompositeByteBuf
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.metadata.CompositeMetadataCodec
import io.rsocket.metadata.TaggingMetadataCodec
import io.rsocket.metadata.WellKnownMimeType
import io.rsocket.transport.netty.client.WebsocketClientTransport
import io.rsocket.util.DefaultPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import java.net.URI

suspend fun connection(): RSocket {
    val ws: WebsocketClientTransport =
        WebsocketClientTransport.create(URI.create("wss://rsocket-demo.herokuapp.com/rsocket"))

    return RSocketConnector.create()
        .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.string)
        .dataMimeType(WellKnownMimeType.APPLICATION_JSON.string)
        .connect(ws)
        .awaitFirst()
}

fun RSocket.queryTweets(query: String): Flow<Tweet> {
    val compositeByteBuf = CompositeByteBuf(ByteBufAllocator.DEFAULT, false, 1);
    val routingMetadata =
        TaggingMetadataCodec.createRoutingMetadata(
            ByteBufAllocator.DEFAULT,
            listOf("searchTweets")
        )
    CompositeMetadataCodec.encodeAndAddMetadata(
        compositeByteBuf,
        ByteBufAllocator.DEFAULT,
        WellKnownMimeType.MESSAGE_RSOCKET_ROUTING,
        routingMetadata.content
    )

    val md = ByteBufUtil.getBytes(compositeByteBuf)

    return requestStream(DefaultPayload.create(query.toByteArray(), md))
        .asFlow()
        .map { toTweet(it) }
}

private fun toTweet(payload: Payload): Tweet = ApiSource.tweetAdapter.fromJson(payload.dataUtf8)!!

object ApiSource {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()!!

    val tweetAdapter = moshi.adapter(Tweet::class.java)
}