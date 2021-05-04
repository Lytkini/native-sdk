package ru.sberdevices.pub.demoapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.sberdevices.common.assert.Asserts
import ru.sberdevices.common.extensions.isMainProcess
import ru.sberdevices.common.logger.AndroidLoggerDelegate
import ru.sberdevices.common.logger.Logger
import ru.sberdevices.messaging.MessagingFactory
import ru.sberdevices.pub.demoapp.ui.assistant.AssistantViewModel
import ru.sberdevices.pub.demoapp.ui.main.MainViewModel
import ru.sberdevices.pub.demoapp.ui.smartapp.SmartAppViewModel
import ru.sberdevices.services.appstate.AppStateManagerFactory
import ru.sberdevices.services.sdk.demoapp.BuildConfig

class DemoApplication : Application() {

    private val logger by Logger.lazy("SdkDemoApplication")

    init {
        Asserts.enabled = BuildConfig.DEBUG
        Logger.setDelegates(AndroidLoggerDelegate())
    }

    private val utilsModule = module {
        single { AppStateManagerFactory.createHolder(context = get()) }
    }

    private val viewModelModule = module {
        viewModel { MainViewModel() }
        viewModel { SmartAppViewModel(messaging = get(), appStateHolder = get()) }
        viewModel { AssistantViewModel() }
    }

    private val sdkModule = module {
        factory { MessagingFactory.create(appContext = get()) }
    }

    override fun onCreate() {
        super.onCreate()
        logger.debug { "onCreate" }

        if (isMainProcess()) {
            initApp()
        }
    }

    private fun initApp() {
        startKoin {
            androidContext(this@DemoApplication)
            modules(utilsModule)
            modules(viewModelModule)
            modules(sdkModule)
        }
    }
}
