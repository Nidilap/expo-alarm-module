import * as React from 'react';

import { StyleSheet, View, Button, Text } from 'react-native';
import { scheduleAlarm, stopAlarm, removeAlarm, enableAlarm, disableAlarm, getAlarm, getAllAlarms, removeAllAlarms } from 'expo-alarm-module';
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

  const enableAlarmCheck = () => {
    if (lastAlarmCreated) {
      enableAlarm(lastAlarmCreated);
    }
  };

  const disableAlarmCheck = () => {
    if (lastAlarmCreated) {
      disableAlarm(lastAlarmCreated);
    }
  };

  const getAlarmCheck = async () => {
    if (lastAlarmCreated) {
      let alarm = await getAlarm(lastAlarmCreated);
      console.log('Alarm got: ', alarm);
    }
  };

  const getAllAlarmsCheck = async () => {
    let resultado = await getAllAlarms();
    console.log(resultado);
  };

  const removeAllAlarmsCheck = async () => {
    removeAllAlarms();
  };

  return (
    <View style={styles.container}>
      <Button title="Create Alarm" onPress={createAlarm} />
      <Button title="Stop Alarm" onPress={stopAlarm} />
      <Button title="Remove Alarm" onPress={removeAlarmCheck} />
      <Button title="Enables Alarm" onPress={enableAlarmCheck} />
      <Button title="Disables Alarm" onPress={disableAlarmCheck} />
      <Button title="Get Alarm" onPress={getAlarmCheck} />
      <Text> -------------- </Text>
      <Button title="Get All Alarms" onPress={getAllAlarmsCheck} />
      <Button title="Remove All Alarms" onPress={removeAllAlarmsCheck} />
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
