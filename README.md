# BaseLib

## 说明
一个ui开发框架，基于databinding，对activity，fragment，dialog，popupwindow，recyclerview做了封装，让开发更简单

## 使用
### 依赖（暂不可用）
```
implementation 'com.shouzhong:BaseLib:1.0.0'
```
### 代码
[请参考demo](https://github.com/shouzhong/BaseLib/tree/master/app/src/main)

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