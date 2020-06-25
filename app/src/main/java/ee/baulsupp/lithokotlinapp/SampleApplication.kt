package ee.baulsupp.lithokotlinapp

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.litho.LithoFlipperDescriptors
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader


class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        registerFlipper()
    }

    private fun registerFlipper() {
        if (BuildConfig.DEBUG) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))

            client.addPlugin(
                InspectorFlipperPlugin(
                    applicationContext,
                    DescriptorMapping.withDefaults()
                )
            )

//            OkHttpClient.Builder()
//                .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
//                .build()

            val descriptorMapping = DescriptorMapping.withDefaults()
            LithoFlipperDescriptors.add(descriptorMapping)
            client.addPlugin(InspectorFlipperPlugin(applicationContext, descriptorMapping))

            client.addPlugin(NetworkFlipperPlugin())
            client.start()
        }
    }
}