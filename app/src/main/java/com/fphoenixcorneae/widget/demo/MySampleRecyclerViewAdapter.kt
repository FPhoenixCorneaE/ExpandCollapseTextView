package com.fphoenixcorneae.widget.demo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fphoenixcorneae.widget.demo.databinding.FragmentItemBinding
import com.fphoenixcorneae.widget.demo.dummy.DummyContent.DummyItem

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
        holder.viewBinding.tvDesc.apply {
            // 设置最大显示行数
            maxLineCount = 3
            // 收起文案
            collapseText = "收起全部"
            // 展开文案
            expandText = "查看全文"
            // 是否支持收起功能
            collapseEnable = true
            // 是否给展开收起添加下划线
            underlineEnable = false
            // 收起文案颜色
            collapseTextColor = Color.BLUE
            // 展开文案颜色
            expandTextColor = Color.RED
            setText(context.getString(R.string.desc), false)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewBinding:FragmentItemBinding= FragmentItemBinding.bind(view)
    }
}