import uuid from 'react-native-uuid';
import { toAndroidDays, fromAndroidDays, getParam } from '../utils';

class Alarm {
  uid?: string | undefined;
  title?: string | undefined;
  description?: string | undefined;
  hour?: number | undefined;
  minutes?: number | undefined;
  snoozeInterval?: number | undefined;
  repeating?: boolean | undefined;
  active?: boolean | undefined;
  day?: string | Date | number[] | undefined;

  constructor(params: any = null) {
    this.uid = getParam(params, 'uid', uuid.v4());
    this.title = getParam(params, 'title', 'Alarm');
    this.description = getParam(params, 'description', 'Wake up');
    this.hour = getParam(params, 'hour', new Date().getHours());
    this.minutes = getParam(params, 'minutes', new Date().getMinutes() + 2);
    this.snoozeInterval = getParam(params, 'snoozeInterval', 1);
    this.repeating = getParam(params, 'repeating', false);
    this.active = getParam(params, 'active', true);
    this.day = getParam(params, 'day', [new Date().getDay()]);
  }

  static getEmpty() {
    return new Alarm({
      title: '',
      description: '',
      hour: 0,
      minutes: 0,
      repeating: false,
      day: [],
    });
  }

  toAndroid() {
    return {
      ...this,
      day: toAndroidDays(this.day),
    };
  }

  static fromAndroid(alarm: Alarm) {
    alarm.day = fromAndroidDays(alarm.day);
    return new Alarm(alarm);
  }
}

export default Alarm;
