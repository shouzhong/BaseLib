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
首先创建ViewModel
class YourViewMode : BViewModel()
然后创建Activity
class YourActivity : BActivity<YourViewMode>(R.layout.布局id)
布局格式，variable一定要以vm命名，且只有这一个
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
首先创建ViewModel
class YourViewMode : BViewModel()
然后创建Fragment
class YourFragment : BFragment<YourViewMode>(R.layout.布局id)
然后创建Fragment
class YourFragment : BFragment<YourViewMode>(R.layout.布局id)
布局格式，variable一定要以vm命名，且只有这一个
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
首先创建数据集
val dataList = DataList().apply {
    adapter.run {
        // 可以注册多个
        register(YourBean::class, YourBinder())
    }
}
然后在布局中
<androidx.recyclerview.widget.RecyclerView
    ...
    app:adapter="@{vm.dataList.adapter}"
/>
创建子项流程
首先创建Holder
class YourHolder(itemView: View, dataList: DataList) : BHolder<YourBean>(itemView, dataList)
然后创建Binder
class YourBinder : BBinder<YourBean, YourHolder>(R.layout.布局id)
布局格式，variable一定要以holder命名，且只有这一个
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

[具体请参考demo](https://github.com/shouzhong/BaseLib/tree/master/app/src/main)