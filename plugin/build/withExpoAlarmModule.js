"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const config_plugins_1 = require("@expo/config-plugins");
// function withExpoLocalizationIos(config: ExpoConfig) {
//   if (config.extra?.supportsRTL == null) return config;
//   if (!config.ios) config.ios = {};
//   if (!config.ios.infoPlist) config.ios.infoPlist = {};
//   config.ios.infoPlist.ExpoLocalization_supportsRTL = config.extra?.supportsRTL || false;
//   return config;
// }
function withExpoAlarmModuleAndroid(config) {
    config = config_plugins_1.AndroidConfig.Permissions.withPermissions(config, [
        'android.permission.SCHEDULE_EXACT_ALARM',
        'android.permission.USE_EXACT_ALARM',
        'android.permission.VIBRATE',
        'android.permission.RECEIVE_BOOT_COMPLETED',
        'android.permission.FOREGROUND_SERVICE',
        'android.permission.WAKE_LOCK'
    ]);
    return (0, config_plugins_1.withStringsXml)(config, (config) => {
        // config.modResults = AndroidConfig.Strings.setStringItem(
        //   [
        //     {
        //       $: { name: 'ExpoLocalization_supportsRTL', translatable: 'false' },
        //       _: String(data.supportsRTL ?? config.extra?.supportsRTL),
        //     },
        //   ],
        //   config.modResults
        // );
        return config;
    });
}
function withExpoAlarmModule(config, data = {}) {
    return (0, config_plugins_1.withPlugins)(config, [
        [withExpoAlarmModuleAndroid, data],
    ]);
}
exports.default = withExpoAlarmModule;
