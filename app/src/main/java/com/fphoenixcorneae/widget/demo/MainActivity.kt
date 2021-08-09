package com.fphoenixcorneae.widget.demo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fphoenixcorneae.widget.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mViewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding!!.root)

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
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding = null
    }
}
