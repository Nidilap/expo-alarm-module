'use strict';
var __spreadArray =
  (this && this.__spreadArray) ||
  function (to, from, pack) {
    if (pack || arguments.length === 2)
      for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
          if (!ar) ar = Array.prototype.slice.call(from, 0, i);
          ar[i] = from[i];
        }
      }
    return to.concat(ar || Array.prototype.slice.call(from));
  };
Object.defineProperty(exports, '__esModule', { value: true });
var config_plugins_1 = require('@expo/config-plugins');
var withExpoAlarmModule = function (config) {
  (0, config_plugins_1.withInfoPlist)(config, function (config) {
    var _a;
    var currentBackgroundModes = (_a = config.modResults.UIBackgroundModes) !== null && _a !== void 0 ? _a : [];
    // Audio background capability
    if (!currentBackgroundModes.includes('audio')) {
      config.modResults.UIBackgroundModes = __spreadArray(__spreadArray([], currentBackgroundModes, true), ['audio'], false);
    }
    // Add background capability for "remote-notification"
    if (!currentBackgroundModes.includes('remote-notification')) {
      config.modResults.UIBackgroundModes = __spreadArray(__spreadArray([], currentBackgroundModes, true), ['remote-notification'], false);
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
exports.default = withExpoAlarmModule;
