export type AlarmSettings = {
  uid: string;
  day: string | Date | number[];
  title: string;
  active: boolean;
  description?: string;
  hour?: number | undefined;
  minutes?: number | undefined;
  snoozeInterval?: number | undefined;
  repeating?: boolean | undefined;
};
