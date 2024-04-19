import { AndroidConfig, type ConfigPlugin, createRunOncePlugin, withInfoPlist } from '@expo/config-plugins';

const pkg = require('expo-alarm-module/package.json');

const withIosPermissions: ConfigPlugin = (configProp) => {
  return withInfoPlist(configProp, (config) => {
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
};

const withAndroidPermissions: ConfigPlugin = (config) => {
  return AndroidConfig.Permissions.withPermissions(config, [
    'android.permission.SCHEDULE_EXACT_ALARM',
    'android.permission.VIBRATE',
    'android.permission.RECEIVE_BOOT_COMPLETED',
    'android.permission.FOREGROUND_SERVICE',
    'android.permission.WAKE_LOCK',
    'android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK',
    'android.permission.POST_NOTIFICATIONS',
  ]);
};

const withExpoAlarmModule: ConfigPlugin = (config) => {
  config = withIosPermissions(config);
  config = withAndroidPermissions(config);

  return config;
};

export default createRunOncePlugin(withExpoAlarmModule, pkg.name, pkg.version);
