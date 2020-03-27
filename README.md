# BaseLib

## 混淆
```
-keepclassmembers public class * extends androidx.databinding.ViewDataBinding {
    *** setVm(***);
    *** setHolder(***);
}
-keepclassmembers class * extends com.shouzhong.base.rv.BHolder {
    <init>(***,***);
}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
```