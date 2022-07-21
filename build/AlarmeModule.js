import { UnavailabilityError } from 'expo-modules-core';
import AlarmeModuleExpo from './AlarmeModuleExpo';
export async function set(alarm = {}) {
    if (!AlarmeModuleExpo.setAlarm) {
        throw new UnavailabilityError('AlarmeModule', 'set');
    }
    return await AlarmeModuleExpo.setAlarm(alarm);
}
export * from './AlarmeModule.types';
//# sourceMappingURL=AlarmeModule.js.map