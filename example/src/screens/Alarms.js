import { Text, View, Button, TextInput } from 'react-native';
import { getAlarmState, getAllAlarms, disableAlarm, enableAlarm, scheduleAlarm, getAlarm } from 'expo-alarm-module';
import AlarmView from '../components/AlarmView';
import React, { useEffect, useState } from 'react';
import { globalStyles } from '../global';
import { NativeModules } from 'react-native';

export default function ({ navigation }) {
  const [alarms, setAlarms] = useState(null);
  const [scheduler, setScheduler] = useState(null);

  const [idAlarm, setIdAlarm] = useState('1');

  const alarmeTeste = async () => {


    let testeAlarme =
    {
      uid: idAlarm ? idAlarm : '1',
      enabled: true,
      title: "Alarm",
      description: "Wake up",
      snoozeInterval: 1,
      repeating: false,
      active: true,
      day: new Date(new Date().getTime() + 300000)
    }

    await scheduleAlarm(testeAlarme);
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

  async function fetchState() {
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
            days={a.day}
            isActive={a.active}
          />
        ))}
      </View>
      <TextInput title="teste" onChange={texto => setIdAlarm(texto)}/>
      <Button title="teste" onPress={alarmeTeste} />
      <Button title="get All alarms" onPress={async () => {
        console.log("TESTANDOO ASTENTION")
        console.log(await getAllAlarms());
      }} />
      <Button title="get Alarm teste" onPress={async () => {
        console.log("TESTANDOO get")
        console.log(await getAlarm('1'));
      }} />
    </View>
  );
}
