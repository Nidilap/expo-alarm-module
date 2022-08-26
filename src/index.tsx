import uuid from 'react-native-uuid';
import { NativeModules, Platform } from 'react-native';
import Alarm from './models/Alarm'

const LINKING_ERROR =
  `The package 'expo-alarm-module' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

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
	await ExpoAlarmModule.set(alarm.toAndroid());
	console.log('scheduling alarm: ', JSON.stringify(alarm))
}

async function enableAlarm(uid: number) {
	await ExpoAlarmModule.enable(uid);
}

async function disableAlarm(uid: number) {
	await ExpoAlarmModule.disable(uid);
}

async function stopAlarm() {
	await ExpoAlarmModule.stop();
}

async function snoozeAlarm() {
	await ExpoAlarmModule.snooze();
}

async function removeAlarm(uid: number) {
	await ExpoAlarmModule.remove(uid);
}

async function updateAlarm(alarm: Alarm) {
	if (!(alarm instanceof Alarm)) {
		alarm = new Alarm(alarm);
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

async function getAlarm(uid: number) {
	const alarm = await ExpoAlarmModule.get(uid);
	return Alarm.fromAndroid(alarm)
}

async function getAlarmState() {
	return ExpoAlarmModule.getState();
}


export default Alarm;
export { 
	scheduleAlarm,
	enableAlarm,
	disableAlarm,
	stopAlarm,
	snoozeAlarm,
	removeAlarm,
	updateAlarm,
	removeAllAlarms,
	getAllAlarms,
	getAlarm,
	getAlarmState 
};