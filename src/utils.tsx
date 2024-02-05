function getParam(param: any, key: any) {
  if (param && (param[key] !== null || param[key] !== undefined)) {
    return param[key];
  }
}

function toAndroidDays(daysArray: string | Date | number[] | undefined) {
  if (daysArray) {
    if (Array.isArray(daysArray)) {
      return daysArray.map((day) => (day + 1) % 7);
    } else {
      return daysArray;
    }
  } else {
    return [];
  }
}

function fromAndroidDays(daysArray: string | Date | number[] | undefined) {
  if (daysArray) {
    if (Array.isArray(daysArray)) {
      return daysArray.map((d) => (d === 0 ? 6 : d - 1));
    } else {
      return daysArray;
    }
  } else {
    return [];
  }
}

function fromIOSDays(dayUTCSeconds: number): Date | undefined {
  if (dayUTCSeconds) {
    return new Date(new Date(0).setUTCSeconds(dayUTCSeconds));
  } else {
    return;
  }
}

export { getParam, toAndroidDays, fromAndroidDays, fromIOSDays };
