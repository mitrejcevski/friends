package nl.jovmit.friends.app

import android.app.Application
import org.koin.core.context.startKoin

class FriendsApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    startKoin {
      modules(applicationModule)
    }
  }
}