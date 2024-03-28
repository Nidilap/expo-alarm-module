# Contributing

For contributing, install the packages with yarn.
After installing and making the changes, build with "bob build".
For commiting, use git add . && git commit -am "comment"

After this, commit the changes with the new version and publish using "npm publish".


Contributions are always welcome, no matter how large or small!

We want this community to be friendly and respectful to each other. Please follow it in all your interactions with the project. Before contributing, please read the [code of conduct](./CODE_OF_CONDUCT.md).

## Development workflow

To run the example for testing, run the following commands:
```sh
yarn example start
```

To run the example app on Android:

```sh
yarn example android
```

To run the example app on iOS:

```sh
yarn example ios
```

After doing changes in the android folder, run .\gradlew clean and .\gradlew build in the example/android.


For testing without publishing to npm, do the following:

- Run npm pack in the package
- Install the package in a managed Expo project ```npx expo install expo-alarm-module@../expo-alarm-module-1.1.10.tgz``` (Here will be the path to the package, in my case it was outside the project).
- Then add "expo-alarm-module" to the plugins array and build the native app locally with expo prebuild and yarn ios, yarn android.