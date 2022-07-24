package expo.modules.alarmemodule

import com.facebook.react.ReactPackage

class Package : ReactPackage {
    @Override
    fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return Arrays.< NativeModule > asList < NativeModule ? > Module(reactContext)
    }

    @Override
    fun createViewManagers(reactContext: ReactApplicationContext?): List<ViewManager> {
        return Collections.emptyList()
    }
}