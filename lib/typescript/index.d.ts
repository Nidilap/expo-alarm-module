export declare function multiply(a: number, b: number): Promise<number>;
export declare function show(text: string): any;
export declare function scheduleAlarm(alarm: Alarm): Promise<void>;
export declare function enableAlarm(uid: number): Promise<void>;
export declare function disableAlarm(uid: number): Promise<void>;
export declare function stopAlarm(): Promise<void>;
export declare function snoozeAlarm(): Promise<void>;
export declare function removeAlarm(uid: number): Promise<void>;
export declare function updateAlarm(alarm: Alarm): Promise<void>;
export declare function removeAllAlarms(): Promise<void>;
export declare function getAllAlarms(): Promise<any>;
export declare function getAlarm(uid: number): Promise<Alarm>;
export declare function getAlarmState(): Promise<any>;
export default class Alarm {
    uid?: number | undefined;
    enabled?: boolean | undefined;
    title?: string | undefined;
    description?: string | undefined;
    hour?: number | undefined;
    minutes?: number | undefined;
    snoozeInterval?: number | undefined;
    repeating?: boolean | undefined;
    active?: boolean | undefined;
    days?: number[] | undefined;
    constructor(params?: any);
    static getEmpty(): Alarm;
    toAndroid(): this & {
        days: number[];
    };
    static fromAndroid(alarm: Alarm): Alarm;
    getTimeString(): {
        hour: string | number;
        minutes: string | number;
    } | {
        hour?: undefined;
        minutes?: undefined;
    };
    getTime(): {};
}
export declare function toAndroidDays(daysArray: number[] | undefined): number[];
export declare function fromAndroidDays(daysArray: number[] | undefined): number[];
