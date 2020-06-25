package ee.baulsupp.lithokotlinapp

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.litho.LithoFlipperDescriptors
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin


fun registerFlipper(applicationContext: Context) {
    if (BuildConfig.DEBUG) {
        val client = AndroidFlipperClient.getInstance(applicationContext)
        client.addPlugin(
            InspectorFlipperPlugin(
                applicationContext,
                DescriptorMapping.withDefaults()
            )
        )

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