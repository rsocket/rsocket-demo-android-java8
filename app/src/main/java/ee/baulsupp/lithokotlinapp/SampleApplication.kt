package ee.baulsupp.lithokotlinapp

import android.app.Application
import com.facebook.soloader.SoLoader

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        registerFlipper(applicationContext)
    }
}