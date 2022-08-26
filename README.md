# expo-alarm-module
Alarm library to use with expo bare workflow.
Tested in versions 0.64 to 0.68 of RN.
## Installation

### NPM

```
npm install expo-alarm-module
```

### Yarn

```
yarn add expo-alarm-module
```

**NOTE: For Android, you will still have to manually update the AndroidManifest.xml (as below) in order to use Scheduled Notifications.**

## Android manual Installation

In your `android/app/src/main/AndroidManifest.xml`

```xml
    .....
    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <application ....>
        <receiver
            android:name="com.expoalarmmodule.receivers.AlarmReceiver"
            android:enabled="true"
            android:exported="true"
            />
        <receiver
            android:name="com.expoalarmmodule.receivers.BootReceiver"
            android:exported="true"
        >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
        <activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />
        <service android:name="com.expoalarmmodule.AlarmService" />
     .....
```
## Usage

```js
import { multiply } from "expo-alarm-module";

// ...

const result = await multiply(3, 7);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
