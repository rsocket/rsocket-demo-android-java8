package ee.baulsupp.lithokotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.facebook.litho.ComponentContext
import com.facebook.litho.LithoView
import com.facebook.litho.widget.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import reactor.core.Disposable
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@FlowPreview
class MainActivity : AppCompatActivity() {
    private var subscription: Disposable? = null
    private lateinit var lithoView: LithoView

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val c = ComponentContext(this)
        val component = Text.create(c).text("Loading...").build()

        lithoView = LithoView.create(c, component)

        setContentView(lithoView)

        lifecycleScope.launch(Dispatchers.Default) {
            val clientRSocket = connection()

            val tweetQuery = clientRSocket.queryTweets("photo").sample(2.seconds)

            tweetQuery.collect {
                lithoView.setComponent(TweetPanel.create(c).tweet(it).build())
            }
        }
    }
}