# DymMeter
UI走查库
 
### 使用方法
```java
public class ExampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    // Normal app init code...
    DymMeter.install(this);
    // Normal app init code...
  }
}
```