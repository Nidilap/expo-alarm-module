import Foundation
import UIKit
import UserNotifications

class NotificationScheduler : NotificationSchedulerDelegate
{
    private let alarms: Alarms = Store.shared.alarms
    // we need to request user for notifiction permission first
    func requestAuthorization() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound]) {
            (authorized, _) in
            if authorized {
                print("notification authorized")
            } else {
                // may need to try other way to make user authorize your app
                print("not authorized")
            }
        }
    }
    
    
    func registerNotificationCategories() {
        // Define the custom actions
        let snoozeAction = UNNotificationAction(identifier: Identifier.snoozeActionIdentifier, title: "Snooze", options: [.foreground])
        let stopAction = UNNotificationAction(identifier: Identifier.stopActionIdentifier, title: "OK", options: [.foreground])
        
        let snoonzeActions = [snoozeAction, stopAction]
        let nonSnoozeActions = [stopAction]
        
        let snoozeAlarmCategory = UNNotificationCategory(identifier: Identifier.snoozeAlarmCategoryIndentifier,
                                                         actions: snoonzeActions,
                                                         intentIdentifiers: [],
                                                         hiddenPreviewsBodyPlaceholder: "",
                                                         options: .customDismissAction)

        let nonSnoozeAlarmCategroy = UNNotificationCategory(identifier: Identifier.alarmCategoryIndentifier,
                                                            actions: nonSnoozeActions,
                                                            intentIdentifiers: [],
                                                            hiddenPreviewsBodyPlaceholder: "",
                                                            options: .customDismissAction)
        // Register the notification category
        UNUserNotificationCenter.current().setNotificationCategories([snoozeAlarmCategory, nonSnoozeAlarmCategroy])
    }
    
    // sync alarm state to scheduled notifications for some situation (app in background and user didn't click notification to bring the app to foreground) that
    // alarm state is not updated correctly
    func syncAlarmStateWithNotification() {
        UNUserNotificationCenter.current().getPendingNotificationRequests(completionHandler: {
            requests in
            print(requests)
            let alarms = Store.shared.alarms
            let uuidNotificationsSet = Set(requests.map({$0.content.userInfo["uid"] as! String}))
            let uuidAlarmsSet = alarms.uids
            let uuidDeltaSet = uuidAlarmsSet.subtracting(uuidNotificationsSet)
            
            for uid in uuidDeltaSet {
                if let alarm = alarms.getAlarm(ByUUIDStr: uid) {
                    if alarm.active {
                        alarm.active = false
                        // since this method will cause UI change, make sure run on main thread
                        DispatchQueue.main.async {
                            alarms.update(alarm)
                        }
                    }
                }
            }
        })
    }
    
    private func getNotificationDates(baseDate date: Date) -> [Date]
    {
        var notificationDates: [Date] = [Date]()
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let now = Date()
        let flags: NSCalendar.Unit = [NSCalendar.Unit.weekday, NSCalendar.Unit.weekdayOrdinal, NSCalendar.Unit.day]
        let dateComponents = (calendar as NSCalendar).components(flags, from: date)
        
        //scheduling date is eariler than current date
        if date < now {
            //plus one day, otherwise the notification will be fired righton
            notificationDates.append((calendar as NSCalendar).date(byAdding: NSCalendar.Unit.day, value: 1, to: date, options:.matchStrictly)!)
        } else {
            notificationDates.append(date)
        }
        
        return notificationDates
    }
    
    static func correctSecondComponent(date: Date, calendar: Calendar = Calendar(identifier: Calendar.Identifier.gregorian)) -> Date {
        let second = calendar.component(.second, from: date)
        let d = (calendar as NSCalendar).date(byAdding: NSCalendar.Unit.second, value: -second, to: date, options:.matchStrictly)!
        return d
    }
    
    func setNotification(alarm: Alarm) {
        let datesForNotification = getNotificationDates(baseDate: alarm.date)
        
        for d in datesForNotification {
            let notificationContent = UNMutableNotificationContent()
            notificationContent.title = alarm.title
            notificationContent.body = alarm.description
            notificationContent.categoryIdentifier = alarm.snoozeEnabled ? Identifier.snoozeAlarmCategoryIndentifier
                                                                   : Identifier.alarmCategoryIndentifier
            notificationContent.sound = UNNotificationSound(named: UNNotificationSoundName("bell.mp3"))
            notificationContent.userInfo = ["snooze" : alarm.snoozeEnabled, "uid": alarm.uid, "soundName": "bell"]
            
            // make dataComponents only contain [weekday, hour, minute] component to make it repeat weakly
            let dateComponents = Calendar.current.dateComponents([.weekday,.hour,.minute], from: d)
            let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: false)
            let request = UNNotificationRequest(identifier: alarm.uid,
                                                content: notificationContent,
                                                trigger: trigger)

            // schedule notification by adding request to notification center
            UNUserNotificationCenter.current().add(request) { error in
                if let e = error {
                    print(e.localizedDescription)
                }
            }
        }
    }
    
    func setNotificationForSnooze(ringtoneName: String, snoozeMinute: Int, uid: String) {
        let currentAlarm = alarms.getAlarm(ByUUIDStr: uid);
        if(currentAlarm != nil) {
            let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
            let now = Date()
            let snoozeDate = (calendar as NSCalendar).date(byAdding: NSCalendar.Unit.minute, value: snoozeMinute, to: now, options:.matchStrictly)!
            setNotification(alarm: currentAlarm!)
        } else {
            print("Error when setting notification for snooze")
        }
    }
    
    func cancelNotification(ByUUIDStr uid: String) {
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: [uid])
    }
    
    func updateNotification(ByUUIDStr uid: String, date: Date, ringtoneName: String, snoonzeEnabled: Bool) {
        cancelNotification(ByUUIDStr: uid)
        let currentAlarm = alarms.getAlarm(ByUUIDStr: uid);
        if(currentAlarm != nil) {
            setNotification(alarm: currentAlarm!)
        } else {
            print("Error updating notification")
        }
    }
    
    enum weekdaysComparisonResult {
        case before
        case same
        case after
    }
    
    // 1 == Sunday, 2 == Monday and so on
    func compare(weekday w1: Int, with w2: Int) -> weekdaysComparisonResult
    {
        if w1 != 1 && (w1 < w2 || w2 == 1) {return .before}
        else if w1 == w2 {return .same}
        else {return .after}
    }
}
