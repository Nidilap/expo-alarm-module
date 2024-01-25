import Foundation

class Manager {
    private let scheduler: NotificationSchedulerDelegate = NotificationScheduler()
    private let alarms: Alarms = Store.shared.alarms
    
    func schedule(_ alarm: Alarm) {
        // Creates the notification (the alarm).
        scheduler.setNotification(date: alarm.date, ringtoneName: alarm.mediaLabel, repeatWeekdays: alarm.repeatWeekdays, snoozeEnabled: alarm.snoozeEnabled, onSnooze: false, uuid: alarm.uuid.uuidString)

        // Stores the alarm in the Store for rescheduling or removing later.
        alarms.add(alarm); 
    }
}
