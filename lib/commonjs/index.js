"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.disableAlarm = disableAlarm;
exports.enableAlarm = enableAlarm;
exports.getAlarm = getAlarm;
exports.getAlarmState = getAlarmState;
exports.getAllAlarms = getAllAlarms;
exports.multiply = multiply;
exports.removeAlarm = removeAlarm;
exports.removeAllAlarms = removeAllAlarms;
exports.scheduleAlarm = scheduleAlarm;
exports.snoozeAlarm = snoozeAlarm;
exports.stopAlarm = stopAlarm;
exports.updateAlarm = updateAlarm;
var _reactNative = require("react-native");
var _Alarm = _interopRequireDefault(require("./models/Alarm"));
function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
const LINKING_ERROR = `The package 'expo-alarm-module' doesn't seem to be linked. Make sure: \n\n` + _reactNative.Platform.select({
  ios: "- You have run 'pod install'\n",
  default: ''
}) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
const ExpoAlarmModule = _reactNative.NativeModules.ExpoAlarmModule ? _reactNative.NativeModules.ExpoAlarmModule : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
async function scheduleAlarm(alarm) {
  if (!(alarm instanceof _Alarm.default)) {
    alarm = new _Alarm.default(alarm);
  }
  if (alarm.day instanceof Date) {
    alarm.day = alarm.day.toJSON();
  }
  if (_reactNative.Platform.OS == 'ios') {
    await ExpoAlarmModule.set(alarm);
  } else if (_reactNative.Platform.OS == 'android') {
    await ExpoAlarmModule.set(alarm.toAndroid());
  }
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
  if (!(alarm instanceof _Alarm.default)) {
    alarm = new _Alarm.default(alarm);
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
  return alarms.map(a => _Alarm.default.fromAndroid(a));
}
async function getAlarm(uid) {
  const alarm = await ExpoAlarmModule.get(uid);
  return _Alarm.default.fromAndroid(alarm);
}
async function getAlarmState() {
  return ExpoAlarmModule.getState();
}
function multiply(a, b) {
  return ExpoAlarmModule.multiply(a, b);
}
var _default = exports.default = _Alarm.default;
//# sourceMappingURL=index.js.map