# ExpandCollapseTextView
展开折叠文本视图


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
	implementation 'com.github.FPhoenixCorneaE:ExpandCollapseTextView:1.0.0'
}
```

#### 属性
```kotlin
tvDesc.apply {
            // 设置最大显示行数
            mMaxLineCount = 3
            // 收起文案
            mCollapseText = "收起全部"
            // 展开文案
            mExpandText = "查看全文"
            // 是否支持收起功能
            mCollapseEnable = true
            // 是否给展开收起添加下划线
            mUnderlineEnable = false
            // 收起文案颜色
            mCollapseTextColor = Color.BLUE
            // 展开文案颜色
            mExpandTextColor = Color.RED
            // 设置要显示的文字以及状态
            setText(getString(R.string.desc), false)
        }
```
