import AlarmeModuleExpo from './AlarmeModuleExpo';
export async function set() {
    // if (!AlarmeModuleExpo.set) {
    //   throw new UnavailabilityError('expo-cellular', 'allowsVoipAsync');
    // }
    return await AlarmeModuleExpo.set();
}
//# sourceMappingURL=AlarmeModule.js.map