export type AlarmSettings = {
  /**
   * Id of the Alarm
   */
  uid: string;

  /**
   * Day the alarm will be triggered. It can be a string, a Date object or an array of numbers.
   */
  day: string | Date | number[];

  /**
   * Title of the alarm
   */
  title: string;

  /**
   * If the alarm is active. Inactive alarms will not be triggered.
   */
  active: boolean;

  /**
   * Description of the alarm.
   */
  description?: string;

  /**
   * Hour the alarm will be triggered. Not required if day is a Date object.
   */
  hour?: number | undefined;

  /**
   * Minutes the alarm will be triggered. Not required if day is a Date object.
   */
  minutes?: number | undefined;

  /**
   * If the dismiss button should be shown in the notification. It is false by default
   */
  showDismiss?: boolean | undefined;

  /**
   * Custom dismiss text for the button of the notification.
   */
  dismissText?: string | undefined;

  /**
   * If the snooze of the alarm is active. False by default
   */
  showSnooze?: boolean | undefined;

  /**
   * Interval in minutes to snooze the alarm. It is 5 minutes by default.
   */
  snoozeInterval?: number | undefined;

  /**
   * Custom snooze text for the button of the notification.
   */
  snoozeText?: string | undefined;

  /**
   * If the alarm needs to repeat.
   */
  repeating?: boolean | undefined;
};
