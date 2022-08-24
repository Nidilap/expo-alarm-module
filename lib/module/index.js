function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

import uuid from 'react-native-uuid';
import { NativeModules, Platform } from 'react-native';
const LINKING_ERROR = `The package 'expo-alarm-module' doesn't seem to be linked. Make sure: \n\n` + Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo managed workflow\n';
const ExpoAlarmModule = NativeModules.ExpoAlarmModule ? NativeModules.ExpoAlarmModule : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }

});
export function multiply(a, b) {
  return ExpoAlarmModule.multiply(a, b);
}
export function show(text) {
  return ExpoAlarmModule.show(text);
}
export async function scheduleAlarm(alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }

  await ExpoAlarmModule.set(alarm.toAndroid());
  console.log('scheduling alarm: ', JSON.stringify(alarm));
}
export async function enableAlarm(uid) {
  await ExpoAlarmModule.enable(uid);
}
export async function disableAlarm(uid) {
  await ExpoAlarmModule.disable(uid);
}
export async function stopAlarm() {
  await ExpoAlarmModule.stop();
}
export async function snoozeAlarm() {
  await ExpoAlarmModule.snooze();
}
export async function removeAlarm(uid) {
  await ExpoAlarmModule.remove(uid);
}
export async function updateAlarm(alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }

  await ExpoAlarmModule.update(alarm.toAndroid());
}
export async function removeAllAlarms() {
  await ExpoAlarmModule.removeAll();
}
export async function getAllAlarms() {
  const alarms = await ExpoAlarmModule.getAll();
  return alarms.map(a => Alarm.fromAndroid(a));
}
export async function getAlarm(uid) {
  const alarm = await ExpoAlarmModule.get(uid);
  return Alarm.fromAndroid(alarm);
}
export async function getAlarmState() {
  return ExpoAlarmModule.getState();
}
export default class Alarm {
  constructor() {
    let params = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : null;

    _defineProperty(this, "uid", void 0);

    _defineProperty(this, "enabled", void 0);

    _defineProperty(this, "title", void 0);

    _defineProperty(this, "description", void 0);

    _defineProperty(this, "hour", void 0);

    _defineProperty(this, "minutes", void 0);

    _defineProperty(this, "snoozeInterval", void 0);

    _defineProperty(this, "repeating", void 0);

    _defineProperty(this, "active", void 0);

    _defineProperty(this, "days", void 0);

    this.uid = getParam(params, 'uid', uuid.v4());
    this.enabled = getParam(params, 'enabled', true);
    this.title = getParam(params, 'title', 'Alarm');
    this.description = getParam(params, 'description', 'Wake up');
    this.hour = getParam(params, 'hour', new Date().getHours());
    this.minutes = getParam(params, 'minutes', new Date().getMinutes() + 2);
    this.snoozeInterval = getParam(params, 'snoozeInterval', 1);
    this.repeating = getParam(params, 'repeating', false);
    this.active = getParam(params, 'active', true);
    this.days = getParam(params, 'days', [new Date().getDay()]);
  }

  static getEmpty() {
    return new Alarm({
      title: '',
      description: '',
      hour: 0,
      minutes: 0,
      repeating: false,
      days: []
    });
  }

  toAndroid() {
    return { ...this,
      days: toAndroidDays(this.days)
    };
  }

  static fromAndroid(alarm) {
    alarm.days = fromAndroidDays(alarm.days);
    return new Alarm(alarm);
  }

  getTimeString() {
    if (this.minutes && this.hour) {
      const hour = this.hour < 10 ? '0' + this.hour : this.hour;
      const minutes = this.minutes < 10 ? '0' + this.minutes : this.minutes;
      return {
        hour,
        minutes
      };
    } else {
      return {};
    }
  }

  getTime() {
    const timeDate = new Date();

    if (this.minutes && this.hour) {
      timeDate.setMinutes(this.minutes);
      timeDate.setHours(this.hour);
    } else {
      return {};
    }

    return timeDate;
  }

}

function getParam(param, key, defaultValue) {
  try {
    if (param && (param[key] !== null || param[key] !== undefined)) {
      return param[key];
    } else {
      return defaultValue;
    }
  } catch (e) {
    return defaultValue;
  }
}

export function toAndroidDays(daysArray) {
  if (daysArray) {
    return daysArray.map(day => (day + 1) % 7);
  } else {
    return [];
  }
}
export function fromAndroidDays(daysArray) {
  if (daysArray) {
    return daysArray.map(d => d === 0 ? 6 : d - 1);
  } else {
    return [];
  }
}
//# sourceMappingURL=index.js.map