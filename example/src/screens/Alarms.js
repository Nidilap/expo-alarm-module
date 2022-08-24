import { Text, View, Button } from 'react-native';
import { show, getAlarmState, getAllAlarms, disableAlarm, enableAlarm } from 'expo-alarm-module';
import AlarmView from '../components/AlarmView';
import React, { useEffect, useState } from 'react';
import { globalStyles } from '../global';
import { NativeModules } from 'react-native';

export default function ({ navigation }) {
  const [alarms, setAlarms] = useState(null);
  const [scheduler, setScheduler] = useState(null);


  const seeNativeModules = () => {
    show("teste");
  }

  useEffect(() => {
    navigation.addListener('focus', async () => {
      setAlarms(await getAllAlarms());
      setScheduler(setInterval(fetchState, 10000));
    });
    navigation.addListener('blur', async () => {
      clearInterval(scheduler);
    });
    fetchState();
  }, []);

  async function fetchState () {
    const alarmUid = await getAlarmState();
    if (alarmUid) {
      navigation.navigate('Ring', { alarmUid });
    }
  }

  return (
    <View style={globalStyles.container}>
      <View style={globalStyles.innerContainer}>
        {alarms && alarms.length === 0 && (
          <Text>No alarms</Text>
        )}
        {alarms && alarms.map(a => (
          <AlarmView
            key={a.uid}
            uid={a.uid}
            onChange={async active => {
              if (active) await enableAlarm(a.uid);
              else await disableAlarm(a.uid);
            }}
            onPress={() => navigation.navigate('Edit', { alarm: a })}
            title={a.title}
            hour={a.hour}
            minutes={a.minutes}
            days={a.days}
            isActive={a.active}
          />
        ))}
      </View>
      <Button title="teste" onPress={seeNativeModules} />
    </View>
  );
}
