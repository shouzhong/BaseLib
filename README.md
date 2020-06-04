# BaseLib

## 说明
一个ui开发框架，基于databinding，对activity，fragment，dialog，popupwindow，recyclerview做了封装，让开发更简单

## 使用
### 依赖
```
implementation 'com.shouzhong:BaseLib:1.0.5'
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
val dataList = DataList()
override fun init() {
    // 可以注册多个
    dataList.adapter.register(YourBean::class, YourBinder(getLifecycleOwner()))
}
// 然后在布局中
<androidx.recyclerview.widget.RecyclerView
    ...
    app:adapter="@{vm.dataList.adapter}"
/>
// 创建子项流程
// 首先创建Holder
class YourHolder(itemView: View) : BHolder<YourBean>(itemView)
// 然后创建Binder
class YourBinder(lifecycleOwner: LifecycleOwner) : BBinder<YourBean, YourHolder>(lifecycleOwner, R.layout.布局id)
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
// 在Activity、Fragment和RecyclerView中使用，在其ViewModel或holder中
// 开关
@DialogSwitch(YourDialog::class)
val yourDialogSwitch = MutableLiveData<Boolean>()
// 数据，非必须，根据实际情况
@DialogData(YourDialog::class)
val yourDialogData = YourDialogBean()
// 是否点击屏幕外或者返回键可关闭，非必须，默认true
@DialogCancelable(YourDialog::class)
val yourDialogCancelable = MutableLiveData<Boolean>()
// 如果你想在其他地方使用，请在其中调用initDialog
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
// 在Activity、Fragment和RecyclerView中使用，在其ViewModel或holder中
// 开关
@PopupSwitch(YourPopup::class)
val yourPopupSwitch = MutableLiveData<Boolean>()
// 数据
@PopupData(YourPopup::class)
val yourPopupData = YourPopupBean()
// 是否点击屏幕外或者返回键可关闭，非必须，默认true
@PopupCancelable(YourPopup::class)
val yourPopupCancelable = MutableLiveData<Boolean>()
// 如果你想在其他地方使用，请在其中调用initPopup
```
其他
```
// startActivity或startActivityForResult
Intent.startActivity
// permission
String.permissionRequest
Array<String>.permissionRequest
ArrayList<String>.permissionRequest
// 获取资源
Int.resToXxx
// 黑白化
View.gray
// 调用三方应用打开文件（包括常用的安装apk包）
File.openByOtherApp
// 获取MIME类型
String.toMimeType
// toast
toastShort
toastLong
// 在主线程运行
runOnUiThread
// 等等
```
看到这，你可能不是很明白，请参考[demo](https://github.com/shouzhong/BaseLib/tree/master/app/src/main)和[源码](https://github.com/shouzhong/BaseLib/tree/master/lib/src/main)
## 求star
#### [Bridge](https://github.com/shouzhong/Bridge)，跨进程管理库，本项目已集成
#### [ScreenHelper](https://github.com/shouzhong/ScreenHelper)，屏幕适配库
#### [Scanner](https://github.com/shouzhong/Scanner)，扫描识别库
