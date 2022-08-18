
# react-native-expo-alarm-module

## Getting started

`$ npm install react-native-expo-alarm-module --save`

### Mostly automatic installation

`$ react-native link react-native-expo-alarm-module`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-expo-alarm-module` and add `RNExpoAlarmModule.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNExpoAlarmModule.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNExpoAlarmModulePackage;` to the imports at the top of the file
  - Add `new RNExpoAlarmModulePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-expo-alarm-module'
  	project(':react-native-expo-alarm-module').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-expo-alarm-module/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-expo-alarm-module')
  	```


## Usage
```javascript
import RNExpoAlarmModule from 'react-native-expo-alarm-module';

// TODO: What to do with the module?
RNExpoAlarmModule;
```
  