package ee.baulsupp.lithokotlinapp

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.URLSpan
import androidx.core.text.set
import com.facebook.litho.*
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.Prop
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge
import com.github.pavlospt.litho.glide.GlideImage
import ee.baulsupp.lithokotlinapp.model.Tweet

@LayoutSpec
object TweetPanelSpec {
    @OnCreateLayout
    fun onCreateLayout(
        c: ComponentContext?,
        @Prop tweet: Tweet
    ): Component {
        val screenName = clickableScreenName(tweet)

        return Column.create(c)
            .paddingDip(YogaEdge.ALL, 16f)
            .backgroundColor(Color.WHITE)
            .child(
                Row.create(c).child(
                    GlideImage.create(c)
                        .imageUrl(tweet.profilePic)
                        .widthPx(73)
                        .heightPx(73)
                ).child(
                    Text.create(c)
                        .text(screenName)
                        .textSizeSp(30f)
                )
            )
            .child(
                Text.create(c)
                    .text(tweet.user?.name)
                    .textSizeSp(20f)
            )
            .child(
                Text.create(c)
                    .text(tweet.text)
                    .textSizeSp(20f)
            )
            .apply {
                tweet.entities?.media?.forEach {media ->
                    media.sizes?.small?.let {size ->
                        child(GlideImage.create(c)
                            .imageUrl(media.media_url_https + "?format=jpg&name=small")
                            .widthPx(size.w)
                            .heightPx(size.h))
                    }
                }
            }
            .build()
    }

    private fun clickableScreenName(tweet: Tweet): SpannableString {
        val handle = "@" + tweet.user?.screen_name
        val screenName = SpannableString(handle)
        screenName.setSpan(
            URLSpan("https://twitter.com/" + tweet.user?.screen_name),
            0,
            handle.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return screenName
    }
}