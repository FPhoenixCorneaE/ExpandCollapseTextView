package com.fphoenixcorneae.widget.demo

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fphoenixcorneae.widget.ExpandCollapseTextView

import com.fphoenixcorneae.widget.demo.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.activity_main.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MySampleRecyclerViewAdapter(
        private val values: List<DummyItem>)
    : RecyclerView.Adapter<MySampleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDesc.apply {
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
            setText(context.getString(R.string.desc), false)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDesc: ExpandCollapseTextView = view.findViewById(R.id.tvDesc)
    }
}