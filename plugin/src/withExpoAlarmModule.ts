import { type ConfigPlugin, withInfoPlist } from '@expo/config-plugins';

const withExpoAlarmModule: ConfigPlugin = (config) => {
  withInfoPlist(config, (config) => {
    const currentBackgroundModes = config.modResults.UIBackgroundModes ?? [];

    // Audio background capability
    if (!currentBackgroundModes.includes('audio')) {
      config.modResults.UIBackgroundModes = [...currentBackgroundModes, 'audio'];
    }

    // Add background capability for "remote-notification"
    if (!currentBackgroundModes.includes('remote-notification')) {
      config.modResults.UIBackgroundModes = [...currentBackgroundModes, 'remote-notification'];
    }

    return config;
  });

  // withAndroidManifest(config, (config) => {
  //   const activity = AndroidConfig.Manifest.getMainActivityOrThrow(config.modResults);
  //   activity.$['android:supportsPictureInPicture'] = 'true';
  //   return config;
  // });
  return config;
};

export default withExpoAlarmModule;
