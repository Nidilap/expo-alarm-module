import EventKit

@objc(ExpoAlarmModule)
class ExpoAlarmModule: NSObject {
    var alarms: Alarms?
    var currentAlarm: Alarm?
    var isEditMode = false

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve((a*b))
    }

    @objc(set:withResolver:withRejecter:)
    func set(dateEpochMS: NSNumber, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {

        // Set the start date of the event to the current date + 10 seconds
        let dataAlarme = Date().addingTimeInterval(10)
        
        let alarm: Alarm = {
            let newAlarm = Alarm()
            newAlarm.date = dataAlarme
            newAlarm.enabled = true
            newAlarm.snoozeEnabled = true
            newAlarm.label = "teste"
            newAlarm.mediaID = ""
            newAlarm.mediaLabel = "bell"
            newAlarm.repeatWeekdays = [0]
            return newAlarm
        }()
        
        alarms?.add(alarm)
        
        resolve("")
    }  
}
