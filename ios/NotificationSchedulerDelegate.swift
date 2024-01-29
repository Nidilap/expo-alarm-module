import Foundation
import UIKit

protocol NotificationSchedulerDelegate {
    func requestAuthorization()
    func registerNotificationCategories()
    func setNotification(date: Date, ringtoneName: String, snoozeEnabled: Bool, onSnooze: Bool, uid: String)
    func setNotificationForSnooze(ringtoneName: String, snoozeMinute: Int, uid: String)
    func cancelNotification(ByUUIDStr uid: String)
    func updateNotification(ByUUIDStr uid: String, date: Date, ringtoneName: String, snoonzeEnabled: Bool)
    func syncAlarmStateWithNotification()
}

