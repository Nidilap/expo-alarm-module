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
    this.uid = getParam(params, 'uid');
    this.title = getParam(params, 'title');
    this.description = getParam(params, 'description');
    this.hour = getParam(params, 'hour');
    this.minutes = getParam(params, 'minutes');
    this.snoozeInterval = getParam(params, 'snoozeInterval');
    this.repeating = getParam(params, 'repeating');
    this.active = getParam(params, 'active');
    this.day = getParam(params, 'day');
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
