import { UnavailabilityError } from 'expo-modules-core';
import AlarmeModuleExpo from './AlarmeModuleExpo';
export async function set(alarm = {}) {
    if (!AlarmeModuleExpo.set) {
        throw new UnavailabilityError('AlarmeModule', 'set');
    }
    return await AlarmeModuleExpo.set(alarm);
}
//# sourceMappingURL=AlarmeModule.js.map