package pl.matiu.pantrytrack.configuration

import pl.matiu.pantrytrack.BuildConfig


object FlavorConfig {
    val isLocalServer: Boolean
        get() = BuildConfig.FLAVOR == "local"

    val isExternalServer: Boolean
        get() = BuildConfig.FLAVOR == "external"
}