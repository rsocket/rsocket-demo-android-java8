package io.rsocket.rsocketdemoappjava8

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.CompositeByteBuf
import io.rsocket.ConnectionSetupPayload
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.metadata.CompositeMetadataCodec
import io.rsocket.metadata.TaggingMetadataCodec
import io.rsocket.metadata.WellKnownMimeType
import io.rsocket.transport.netty.client.WebsocketClientTransport
import io.rsocket.util.DefaultPayload
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.label
import reactor.core.Disposable
import reactor.core.scheduler.Schedulers
import java.net.URI
import java.time.Duration.ofSeconds

class MainActivity : AppCompatActivity() {

  private var subscription: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    val mUiHandler = object : Handler(Looper.getMainLooper()) {}

    val compositeByteBuf =  CompositeByteBuf(ByteBufAllocator.DEFAULT, false, 1);
    val routingMetadata = TaggingMetadataCodec.createRoutingMetadata(ByteBufAllocator.DEFAULT, listOf("searchTweets"))
    CompositeMetadataCodec.encodeAndAddMetadata(compositeByteBuf, ByteBufAllocator.DEFAULT, WellKnownMimeType.MESSAGE_RSOCKET_ROUTING, routingMetadata.content)
    val md = ByteBufUtil.getBytes(compositeByteBuf)

    val query = "Sunday"

    val ws: WebsocketClientTransport =
      WebsocketClientTransport.create(URI.create("wss://rsocket-demo.herokuapp.com/rsocket"))

    val clientRSocket: RSocket = RSocketConnector.create()
      .metadataMimeType(WellKnownMimeType.MESSAGE_RSOCKET_COMPOSITE_METADATA.string)
      .dataMimeType(WellKnownMimeType.APPLICATION_JSON.string)
      .connect(ws)
      .block()!!

    val blmTweets = clientRSocket.requestStream(DefaultPayload.create(query.toByteArray(), md))
    val onePerSec = blmTweets
      .window(ofSeconds(1L)).flatMap { it.take(1L) }
      .map { toTweet(it) }
    subscription = onePerSec.subscribeOn(Schedulers.elastic())
      .subscribe({ mUiHandler.post { label.text = it.text } },
                 { Log.w("MainActivity", "failed", it) })
  }

  private fun toTweet(payload: Payload): Tweet = tweetAdapter.fromJson(payload.dataUtf8)!!

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onDestroy() {
    subscription?.dispose()
    super.onDestroy()
  }

  companion object {
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()!!

    val tweetAdapter = moshi.adapter(Tweet::class.java)
  }
}
