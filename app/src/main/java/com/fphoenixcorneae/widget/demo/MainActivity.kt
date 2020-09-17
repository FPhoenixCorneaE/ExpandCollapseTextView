package com.fphoenixcorneae.widget.demo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}
