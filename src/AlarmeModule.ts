import { UnavailabilityError } from 'expo-modules-core';

import {
  Alarme
} from './AlarmeModule.types';
import AlarmeModuleExpo from './AlarmeModuleExpo';


export async function set(alarm: Alarme = {}): Promise<boolean | null> {
    if (!AlarmeModuleExpo.setAlarm) {
      throw new UnavailabilityError('AlarmeModule', 'set');
    }
    return await AlarmeModuleExpo.setAlarm(alarm);
  }

export * from './AlarmeModule.types';