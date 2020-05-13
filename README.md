# BaseLib

## 说明
一个ui开发框架，基于databinding，对activity，fragment，dialog，popupwindow，recyclerview做了封装，让开发更简单

## 使用
### 依赖
```
implementation 'com.shouzhong:BaseLib:1.0.3'
```
### 代码
首先，你需要在Application中初始化
```
BUtils.init(application)
```
在Activity中使用
```
// 首先创建ViewModel
class YourViewMode : BViewModel()
// 然后创建Activity
class YourActivity : BActivity<YourViewMode>(R.layout.布局id)
// 布局格式，variable一定要以vm命名，且只有这一个
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="vm"
            type="YourViewMode路径" />
    </data>

    <LinearLayout
        ...>
        ...
    </LinearLayout>
</layout>
```
在Fragment中使用
```
// 首先创建ViewModel
class YourViewMode : BViewModel()
// 然后创建Fragment
class YourFragment : BFragment<YourViewMode>(R.layout.布局id)
// 布局格式，variable一定要以vm命名，且只有这一个
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="vm"
            type="YourViewMode路径" />
    </data>

    <LinearLayout
        ...>
        ...
    </LinearLayout>
</layout>
```
在RecyclerView的中使用，关于adapter请参考[MultiType](https://github.com/drakeet/MultiType)
```
// 首先创建数据集
val dataList = DataList().apply {
    adapter.run {
        // 可以注册多个
        register(YourBean::class, YourBinder())
    }
}
// 然后在布局中
<androidx.recyclerview.widget.RecyclerView
    ...
    app:adapter="@{vm.dataList.adapter}"
/>
// 创建子项流程
// 首先创建Holder
class YourHolder(itemView: View, dataList: DataList) : BHolder<YourBean>(itemView, dataList)
// 然后创建Binder
class YourBinder : BBinder<YourBean, YourHolder>(R.layout.布局id)
// 布局格式，variable一定要以holder命名，且只有这一个
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="holder"
            type="YourHolder路径" />
    </data>

    <LinearLayout
        ...>
        ...
    </LinearLayout>
</layout>
```
在Dialog中使用
```
// 首先创建ViewModel
class YourViewModel : BViewModel<YourBean>()
// 然后创建Dialog
class YourDialog : BDialog<YourViewModel>(R.layout.布局id)
// 布局格式，variable一定要以vm命名，且只有这一个
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="vm"
            type="YourViewMode路径" />
    </data>

    <LinearLayout
        ...>
        ...
    </LinearLayout>
</layout>
// 在Activity和Fragment中使用用，在其ViewModel中
// 开关
@DialogSwitch(YourDialog::class)
val yourDialogSwitch = ObservableBoolean(false)
// 数据
@DialogData(YourDialog::class)
val yourDialogData = YourDialogBean()
// 是否点击屏幕外或者返回键可关闭，非必须，默认true
@DialogCancelable(YourDialog::class)
val yourDialogCancelable = ObservableBoolean(false)
// 如果你想在其他地方使用，请在其中调用initDialog(appCompatActivity)
```
在PopupWindow中使用
```
// 首先创建数据
class YourBean : BPopupBean()
// 然后创建ViewModel
class YourViewModel : BViewModel<YourBean>()
// 再创建PopupWindow
class YourPopup : BPopup<YourViewModel>(R.layout.布局id)
// 布局格式，variable一定要以vm命名，且只有这一个
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="vm"
            type="YourViewMode路径" />
    </data>

    <LinearLayout
        ...>
        ...
    </LinearLayout>
</layout>
// 在Activity和Fragment中使用用，在其ViewModel中
@PopupSwitch(YourPopup::class)
val yourPopupSwitch = ObservableBoolean(false)
@PopupData(YourPopup::class)
val yourPopupData = YourPopupBean()
// 是否点击屏幕外或者返回键可关闭，非必须，默认true
@PopupCancelable(YourPopup::class)
val yourPopupCancelable = ObservableBoolean(false)
// 如果你想在其他地方使用，请在其中调用initPopup(appCompatActivity)
```
其他
```
// startActivityForResult
Intent(...).apply {
    ...
}
.startActivity() { resultCode, data ->
}
// permission
PermissionUtils.requestPermission(
    Manifest.permission.CAMERA,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    grantedCallback = { permissionsGranted ->
        ...
    },
    deniedCallback = { permissionsDenied, permissionsDeniedForever, permissionsUndefined ->
        ...
    },
    simpleGrantedCallback = {
        ...
    },
    simpleDeniedCallback = {
        ...
    }
)
// 等等
```
看到这，你可能不是很明白，请参考[demo](https://github.com/shouzhong/BaseLib/tree/master/app/src/main)和[源码](https://github.com/shouzhong/BaseLib/tree/master/lib/src/main)
## 你可能用的上的
#### [Bridge](https://github.com/shouzhong/Bridge)，跨进程管理库，本项目已集成
#### [ScreenHelper](https://github.com/shouzhong/ScreenHelper)，屏幕适配库
#### [Scanner](https://github.com/shouzhong/Scanner)，扫描识别库
