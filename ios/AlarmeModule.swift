import ExpoModulesCore

public class AlarmeModule: Module {
  public func definition() -> ModuleDefinition {
    Name("AlarmeModule")

    AsyncFunction("helloAsync") { (options: [String: String]) in
      print("Hello 👋")
    }

    ViewManager {
      View {
        AlarmeModuleView()
      }

      Prop("name") { (view: AlarmeModuleView, prop: String) in
        print(prop)
      }
    }
  }
}
