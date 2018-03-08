package io.rsocket.rsocketdemoappjava8

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.rsocket.android.RSocketFactory
import io.rsocket.android.util.PayloadImpl
import io.rsocket.transport.okhttp.client.OkhttpWebsocketClientTransport
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.label
import okhttp3.HttpUrl

class MainActivity : AppCompatActivity() {

  private var subscription: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    val mUiHandler = object : Handler(Looper.getMainLooper()) {}

    val client = RSocketFactory.connect().transport {
      OkhttpWebsocketClientTransport.create(
          HttpUrl.parse("https://rsocket-demo.herokuapp.com/ws")!!)
    }.start().blockingGet()

    val trumpTweets = client.requestStream(PayloadImpl.textPayload("trump"))
    subscription = trumpTweets.subscribeOn(Schedulers.io()).subscribe(
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
