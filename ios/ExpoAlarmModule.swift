import EventKit
import AVFoundation

@objc(ExpoAlarmModule)
class ExpoAlarmModule: NSObject, UNUserNotificationCenterDelegate, AVAudioPlayerDelegate  {
    var isEditMode = false
    private var audioPlayer: AVAudioPlayer?
    
    private let notificationScheduler: NotificationSchedulerDelegate = NotificationScheduler()
    private let alarms: Alarms = Store.shared.alarms
    private let manager: Manager = Manager();
    
    public override init() {
        super.init()
        do {
            try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playback)
        } catch let error as NSError{
            print("could not set session. err:\(error.localizedDescription)")
        }
        do {
            try AVAudioSession.sharedInstance().setActive(true)
        } catch let error as NSError{
            print("could not active session. err:\(error.localizedDescription)")
        }
        
        notificationScheduler.requestAuthorization()
        notificationScheduler.registerNotificationCategories()
        UNUserNotificationCenter.current().delegate = self

        // Add observer for applicationDidBecomeActive
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(applicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: nil
        )
    }

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve((a*b))
    }

    @objc(set:withResolver:withRejecter:)
    func set(dateEpochMS: NSNumber, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        let currentDate = Date()
        let date = currentDate.addingTimeInterval(60)
        let alarm = Alarm(
            uuid: UUID(),
            date: date,
            enabled: true,
            snoozeEnabled: false,
            repeatWeekdays: [],
            mediaID: "",
            mediaLabel: "bell",
            label: "tESTE"
        )

        manager.schedule(alarm);
    

        resolve("")
    }  


    // The method will be called on the delegate only if the application is in the foreground. If the method is not implemented or the handler is not called in a timely manner then the notification will not be presented. The application can choose to have the notification presented as a sound, badge, alert and/or in the notification list. This decision should be based on whether the information in the notification is otherwise visible to the user.
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        //show an alert window
        let alertController = UIAlertController(title: "Alarm", message: nil, preferredStyle: .alert)
        let userInfo = notification.request.content.userInfo
        guard
            let snoozeEnabled = userInfo["snooze"] as? Bool,
            let soundName = userInfo["soundName"] as? String,
            let uuidStr = userInfo["uuid"] as? String
        else {return}
        
        playSound(soundName)
        //schedule notification for snooze
        if snoozeEnabled {
            let snoozeOption = UIAlertAction(title: "Snooze", style: .default) {
                (action:UIAlertAction) in
                self.audioPlayer?.stop()
                self.notificationScheduler.setNotificationForSnooze(ringtoneName: soundName, snoozeMinute: 9, uuid: uuidStr)
            }
            alertController.addAction(snoozeOption)
        }
        
        let stopOption = UIAlertAction(title: "OK", style: .default) {
            (action:UIAlertAction) in
            self.audioPlayer?.stop()
            AudioServicesRemoveSystemSoundCompletion(kSystemSoundID_Vibrate)
            let alarms = Store.shared.alarms
            if let alarm = alarms.getAlarm(ByUUIDStr: uuidStr) {
                if alarm.repeatWeekdays.isEmpty {
                    alarm.enabled = false
                    alarms.update(alarm)
                }
            }
        }
        
        alertController.addAction(stopOption)
        if #available(iOS 14.0, *) {
            completionHandler(.list)
        } else {
            completionHandler(.alert)
        }
    }
    

    @objc func applicationDidBecomeActive() {
        notificationScheduler.syncAlarmStateWithNotification()
    }

    // The method will be called on the delegate when the user responded to the notification by opening the application, dismissing the notification or choosing a UNNotificationAction. The delegate must be set before the application returns from application:didFinishLaunchingWithOptions:.
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
        guard
            let soundName = userInfo["soundName"] as? String,
            let uuid = userInfo["uuid"] as? String
        else {return}
        
        switch response.actionIdentifier {
        case Identifier.snoozeActionIdentifier:
            // notification fired when app in background, snooze button clicked
            notificationScheduler.setNotificationForSnooze(ringtoneName: soundName, snoozeMinute: 9, uuid: uuid)
            break
        case Identifier.stopActionIdentifier:
            // notification fired when app in background, ok button clicked
            let alarms = Store.shared.alarms
            if let alarm = alarms.getAlarm(ByUUIDStr: uuid) {
                if alarm.repeatWeekdays.isEmpty {
                    alarm.enabled = false
                    alarms.update(alarm)
                }
            }
            break
        default:
            break
        }

        completionHandler()
    }
    
    //AlarmApplicationDelegate protocol
    func playSound(_ soundName: String) {
        //vibrate phone first
        AudioServicesPlaySystemSound(SystemSoundID(kSystemSoundID_Vibrate))
        //set vibrate callback
        AudioServicesAddSystemSoundCompletion(SystemSoundID(kSystemSoundID_Vibrate),nil,
            nil,
            { (_:SystemSoundID, _:UnsafeMutableRawPointer?) -> Void in
                AudioServicesPlaySystemSound(SystemSoundID(kSystemSoundID_Vibrate))
            },
            nil)
        
        if let files = try? FileManager.default.contentsOfDirectory(atPath: Bundle.main.bundlePath) {
            print(files)
        }
        guard let filePath = Bundle.main.path(forResource: soundName, ofType: "mp3") else {fatalError()}
        let url = URL(fileURLWithPath: filePath)
        
        do {
            audioPlayer = try AVAudioPlayer(contentsOf: url)
        } catch let error as NSError {
            audioPlayer = nil
            print("audioPlayer error \(error.localizedDescription)")
            return
        }
        
        if let player = audioPlayer {
            player.delegate = self
            player.prepareToPlay()
            //negative number means loop infinity
            player.numberOfLoops = -1
            player.play()
        }
    }
}
