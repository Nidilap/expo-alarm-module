'use strict';
Object.defineProperty(exports, '__esModule', { value: true });
const config_plugins_1 = require('@expo/config-plugins');
const pkg = require('expo-alarm-module/package.json');
const withIosPermissions = (configProp) => {
  return (0, config_plugins_1.withInfoPlist)(configProp, (config) => {
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
const withAndroidPermissions = (config) => {
  return config_plugins_1.AndroidConfig.Permissions.withPermissions(config, [
    'android.permission.SCHEDULE_EXACT_ALARM',
    'android.permission.VIBRATE',
    'android.permission.RECEIVE_BOOT_COMPLETED',
    'android.permission.FOREGROUND_SERVICE',
    'android.permission.WAKE_LOCK',
  ]);
};
const withExpoAlarmModule = (config) => {
  config = withIosPermissions(config);
  config = withAndroidPermissions(config);
  return config;
};
exports.default = (0, config_plugins_1.createRunOncePlugin)(withExpoAlarmModule, pkg.name, pkg.version);
