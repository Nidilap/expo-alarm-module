import uuid from 'react-native-uuid';
import { toAndroidDays, fromAndroidDays, getParam } from "../utils";

class Alarm {
	uid?: string | undefined;
	enabled?: boolean | undefined;
	title?: string | undefined;
	description?: string | undefined;
	hour?: number | undefined;
	minutes?: number | undefined;
	snoozeInterval?: number | undefined;
	repeating?: boolean | undefined;
	active?: boolean | undefined;
	day?: Date | number[] | undefined;

	constructor(params: any = null) {
		this.uid = getParam(params, 'uid', uuid.v4());
		this.enabled = getParam(params, 'enabled', true);
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
			days: [],
		});
	}

	toAndroid() {
		return {
			...this,
			days: toAndroidDays(this.day)
		}
	}

	static fromAndroid(alarm: Alarm) {
		alarm.day = fromAndroidDays(alarm.day);
		return new Alarm(alarm);
	}

	getTimeString() {
		if (this.minutes && this.hour) {
			const hour = this.hour < 10 ? '0' + this.hour : this.hour;
			const minutes = this.minutes < 10 ? '0' + this.minutes : this.minutes;
			return { hour, minutes };
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

export default Alarm;