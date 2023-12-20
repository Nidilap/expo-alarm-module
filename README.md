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
// App.js
import React, { useEffect } from 'react';
import { View, Text, Button } from 'react-native';

import AlarmModule, { removeAlarm, scheduleAlarm, stopAlarm } from "expo-alarm-module";

const App = () => {
    const alarmIn10 = () => {
        var newDate = new Date();
        newDate.setSeconds(newDate.getSeconds() + 10);

        scheduleAlarm(
        {
            uid: "alarm1",
            enabled: true,
            day: newDate,
            title: "Title of alarm",
            description: "Alarm Description",        
            snoozeInterval: 5,
            repeating: true,
            active: true
        } as any
        );

    };

    /* Create a new alarm 10 seconds after the current date.*/
    const onStopAlarmButton = () => {
        var newDate = new Date();
        newDate.setSeconds(newDate.getSeconds() + 10);

        //@ts-ignore
        stopAlarm("alarm1");
        
        //@ts-ignore
        removeAlarm("alarm1");

    };

    return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
            <Text>React Native Alarm Test</Text>
            <Button title="Alarm in 10 seconds" onPress={alarmIn10} />
            <Button title="Stop Alarm" onPress={onStopAlarmButton} />
        </View>
    );
};

export default App;
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

## Build

---

To build, it is necessary to run .\gradlew clean & .\gradlew build in the android folder.
Afterwards, run "npx bob build" in the root directory. 
After that, just publish the app.
