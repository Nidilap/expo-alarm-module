import { NativeModules, Platform } from 'react-native';
import Alarm from './models/Alarm';
import { AlarmSettings } from './types/Alarm.types';

const LINKING_ERROR =
  `The package 'expo-alarm-module' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ExpoAlarmModule = NativeModules.ExpoAlarmModule
  ? NativeModules.ExpoAlarmModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

async function scheduleAlarm(alarm: AlarmSettings) {
  let alarmToUse: Alarm = new Alarm(alarm);

  if (alarmToUse.day instanceof Date) {
    alarmToUse.day = alarmToUse.day.toJSON();
  }

  if (Platform.OS == 'ios') {
    await ExpoAlarmModule.set(alarmToUse);
  } else if (Platform.OS == 'android') {
    await ExpoAlarmModule.set(alarmToUse.toAndroid());
  }
}

async function enableAlarm(uid: string) {
  await ExpoAlarmModule.enable(uid);
}

async function disableAlarm(uid: string) {
  await ExpoAlarmModule.disable(uid);
}

async function stopAlarm() {
  await ExpoAlarmModule.stop();
}

async function snoozeAlarm() {
  await ExpoAlarmModule.snooze();
}

async function removeAlarm(uid: string) {
  await ExpoAlarmModule.remove(uid);
}

async function updateAlarm(alarm: AlarmSettings) {
  let alarmToUse: Alarm = new Alarm(alarm);

  if (alarmToUse.day instanceof Date) {
    alarmToUse.day = alarmToUse.day.toJSON();
  }

  await ExpoAlarmModule.update(alarmToUse.toAndroid());
}

async function removeAllAlarms() {
  await ExpoAlarmModule.removeAll();
}

async function getAllAlarms() {
  const alarms = await ExpoAlarmModule.getAll();
  return alarms.map((a: any) => Alarm.fromAndroid(a));
}

async function getAlarm(uid: string) {
  const alarm = await ExpoAlarmModule.get(uid);

  if (alarm?.uid) {
    if (Platform.OS == 'ios') {
      return new Alarm(alarm);
    } else if (Platform.OS == 'android') {
      return Alarm.fromAndroid(alarm);
    }
  }
  return;
}

async function getAlarmState() {
  return ExpoAlarmModule.getState();
}

function multiply(a: number, b: number): Promise<number> {
  return ExpoAlarmModule.multiply(a, b);
}

export default Alarm;
export { scheduleAlarm, enableAlarm, disableAlarm, stopAlarm, snoozeAlarm, removeAlarm, updateAlarm, removeAllAlarms, getAllAlarms, getAlarm, getAlarmState, multiply };
