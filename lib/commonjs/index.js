"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.disableAlarm = disableAlarm;
exports.enableAlarm = enableAlarm;
exports.fromAndroidDays = fromAndroidDays;
exports.getAlarm = getAlarm;
exports.getAlarmState = getAlarmState;
exports.getAllAlarms = getAllAlarms;
exports.multiply = multiply;
exports.removeAlarm = removeAlarm;
exports.removeAllAlarms = removeAllAlarms;
exports.scheduleAlarm = scheduleAlarm;
exports.show = show;
exports.snoozeAlarm = snoozeAlarm;
exports.stopAlarm = stopAlarm;
exports.toAndroidDays = toAndroidDays;
exports.updateAlarm = updateAlarm;

var _reactNativeUuid = _interopRequireDefault(require("react-native-uuid"));

var _reactNative = require("react-native");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

const LINKING_ERROR = `The package 'expo-alarm-module' doesn't seem to be linked. Make sure: \n\n` + _reactNative.Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo managed workflow\n';
const ExpoAlarmModule = _reactNative.NativeModules.ExpoAlarmModule ? _reactNative.NativeModules.ExpoAlarmModule : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }

});

function multiply(a, b) {
  return ExpoAlarmModule.multiply(a, b);
}

function show(text) {
  return ExpoAlarmModule.show(text);
}

async function scheduleAlarm(alarm) {
  if (!(alarm instanceof Alarm)) {
    alarm = new Alarm(alarm);
  }

  await ExpoAlarmModule.set(alarm.toAndroid());
  console.log('scheduling alarm: ', JSON.stringify(alarm));
}

async function enableAlarm(uid) {
  await ExpoAlarmModule.enable(uid);
}

async function disableAlarm(uid) {
  await ExpoAlarmModule.disable(uid);
}

async function stopAlarm() {
  await ExpoAlarmModule.stop();
}

async function snoozeAlarm() {
  await ExpoAlarmModule.snooze();
}

async function removeAlarm(uid) {
  await ExpoAlarmModule.remove(uid);
}

async function updateAlarm(alarm) {
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
  return alarms.map(a => Alarm.fromAndroid(a));
}

async function getAlarm(uid) {
  const alarm = await ExpoAlarmModule.get(uid);
  return Alarm.fromAndroid(alarm);
}

async function getAlarmState() {
  return ExpoAlarmModule.getState();
}

class Alarm {
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

    this.uid = getParam(params, 'uid', _reactNativeUuid.default.v4());
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

exports.default = Alarm;

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

function toAndroidDays(daysArray) {
  if (daysArray) {
    return daysArray.map(day => (day + 1) % 7);
  } else {
    return [];
  }
}

function fromAndroidDays(daysArray) {
  if (daysArray) {
    return daysArray.map(d => d === 0 ? 6 : d - 1);
  } else {
    return [];
  }
}
//# sourceMappingURL=index.js.map