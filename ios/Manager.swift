import Foundation
import AVFoundation

class Manager {
    private let scheduler: NotificationSchedulerDelegate = NotificationScheduler()
    private let alarms: Alarms = Store.shared.alarms
    
    func schedule(_ alarm: Alarm) {
        // Creates the notification (the alarm).
        scheduler.setNotification(date: alarm.date, ringtoneName: "bell", snoozeEnabled: alarm.snoozeEnabled, onSnooze: false, uid: alarm.uid)

        // Stores the alarm in the Store for rescheduling or removing later.
        alarms.add(alarm); 
    }
    
    func stop() {
        ExpoAlarmModule.audioPlayer?.stop()
        AudioServicesRemoveSystemSoundCompletion(kSystemSoundID_Vibrate)
    }

    func remove(_ uid: String) {
        // Stops any sound of alarm that is playing.
        self.stop()
        
        // Cancels the notification
        scheduler.cancelNotification(ByUUIDStr: uid)
        
        // Removes the alarm from the Store.
        alarms.remove(uid)
    }
    
}
