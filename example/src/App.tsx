import * as React from 'react';

import { StyleSheet, View, Button } from 'react-native';
import { scheduleAlarm, stopAlarm, removeAlarm } from 'expo-alarm-module';
import uuid from 'react-native-uuid';

export default function App() {
  const [lastAlarmCreated, setLastAlarmCreated] = React.useState<string>();

  const createAlarm = () => {
    var newDate = new Date();
    newDate.setSeconds(newDate.getSeconds() + 60);

    let uuidToUse: string = uuid.v4() as string;
    setLastAlarmCreated(uuidToUse);

    scheduleAlarm({
      uid: uuidToUse,
      day: newDate,
      title: 'Title of alarm',
      description: 'Alarm Description',
      snoozeInterval: 5,
      repeating: true,
      active: true,
    });
  };

  const removeAlarmCheck = () => {
    if (lastAlarmCreated) {
      removeAlarm(lastAlarmCreated);
    }
  };

  return (
    <View style={styles.container}>
      <Button title="Create Alarm" onPress={createAlarm} />
      <Button title="Stop Alarm" onPress={stopAlarm} />
      <Button title="Remove Alarm" onPress={removeAlarmCheck} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
