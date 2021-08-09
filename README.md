# ExpandCollapseTextView
展开折叠文本视图
-----------------------------------------------------------

[![](https://jitpack.io/v/FPhoenixCorneaE/ExpandCollapseTextView.svg)](https://jitpack.io/#FPhoenixCorneaE/ExpandCollapseTextView)

How to include it in your project:
--------------
**Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```groovy
dependencies {
	implementation("com.github.FPhoenixCorneaE:ExpandCollapseTextView:$latest")
}
```

属性
----------------------------------------------------------
```kotlin
mViewBinding!!.tvDesc.apply {
    // 设置最大显示行数
    maxLineCount = 3
    // 收起文案
    collapseText = "收起全部"
    // 展开文案
    expandText = "查看全文"
    // 是否支持收起功能
    collapseEnable = false
    // 是否给展开收起添加下划线
    underlineEnable = false
    // 收起文案颜色
    collapseTextColor = Color.BLUE
    // 展开文案颜色
    expandTextColor = Color.RED
    // 设置要显示的文字以及状态
    setText(getString(R.string.desc), false)
}
```
