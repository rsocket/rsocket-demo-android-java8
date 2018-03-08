package io.rsocket.rsocketdemoappjava8

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.rsocket.RSocketFactory
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

    val ws = WebsocketClientTransport.create(URI.create("ws://rsocket-demo.herokuapp.com/ws"));
    val client = RSocketFactory.connect().keepAlive().transport(ws).start()

    val trumpTweets = client.flatMapMany { it.requestStream(DefaultPayload.create("trump")) }
    val onePerSec = trumpTweets.window(ofSeconds(1L)).flatMap { it.take(1L) }
    subscription = onePerSec.subscribeOn(Schedulers.elastic()).subscribe(
        { mUiHandler.post({ label.text = it.dataUtf8 }) },
        { Log.w("MainActivity", "failed", it) })
  }

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
}
