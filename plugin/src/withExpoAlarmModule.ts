import { getMainActivityOrThrow } from '@expo/config-plugins/build/android/Manifest';
import type { ExpoConfig } from '@expo/config-types';
import {
  withPlugins,
  withStringsXml,
  AndroidConfig
} from '@expo/config-plugins';



type ConfigPluginProps = {};

// function withExpoLocalizationIos(config: ExpoConfig) {
//   if (config.extra?.supportsRTL == null) return config;
//   if (!config.ios) config.ios = {};
//   if (!config.ios.infoPlist) config.ios.infoPlist = {};
//   config.ios.infoPlist.ExpoLocalization_supportsRTL = config.extra?.supportsRTL || false;
//   return config;
// }

function withExpoAlarmModuleAndroid(config: ExpoConfig, data: ConfigPluginProps) {

  
  config = AndroidConfig.Permissions.withPermissions(config, [
    'android.permission.SCHEDULE_EXACT_ALARM',
    'android.permission.USE_EXACT_ALARM',
    'android.permission.VIBRATE',
    'android.permission.RECEIVE_BOOT_COMPLETED',
    'android.permission.FOREGROUND_SERVICE',
    'android.permission.WAKE_LOCK'
  ]);

  return withStringsXml(config, (config) => {
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

function withExpoAlarmModule(
  config: ExpoConfig,
  data: ConfigPluginProps = {}
) {
  return withPlugins(config, [
    [withExpoAlarmModuleAndroid, data],
  ]);
}

export default withExpoAlarmModule;

