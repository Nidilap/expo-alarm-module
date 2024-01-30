import { NativeModules, Platform } from 'react-native';
import Alarm from './models/Alarm';

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

async function scheduleAlarm(alarm: Alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }

  if (alarm.day instanceof Date) {
    alarm.day = alarm.day.toJSON();
  }

  if (Platform.OS == 'ios') {
    await ExpoAlarmModule.set(alarm);
  } else if (Platform.OS == 'android') {
    await ExpoAlarmModule.set(alarm.toAndroid());
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

async function updateAlarm(alarm: Alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }

  if (alarm.day instanceof Date) {
    alarm.day = alarm.day.toJSON();
  }

  await ExpoAlarmModule.update(alarm.toAndroid());
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
  return Alarm.fromAndroid(alarm);
}

async function getAlarmState() {
  return ExpoAlarmModule.getState();
}

function multiply(a: number, b: number): Promise<number> {
  return ExpoAlarmModule.multiply(a, b);
}

export default Alarm;
export { scheduleAlarm, enableAlarm, disableAlarm, stopAlarm, snoozeAlarm, removeAlarm, updateAlarm, removeAllAlarms, getAllAlarms, getAlarm, getAlarmState, multiply };
