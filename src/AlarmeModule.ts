import { UnavailabilityError } from 'expo-modules-core';

import {
  Alarme
} from './AlarmeModule.types';
import AlarmeModuleExpo from './AlarmeModuleExpo';

export async function set(alarm: Alarme = {} as Alarme): Promise<boolean | null> {
    if (!AlarmeModuleExpo.set) {
      throw new UnavailabilityError('AlarmeModule', 'set');
    }
    return await AlarmeModuleExpo.set(alarm);
  }