package com.fphoenixcorneae.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

/**
 * @desc: 展开折叠文字视图
 * @date: 2019-10-18 19:14
 */
class ExpandCollapseTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    /** 省略文字 */
    private var mEllipsizeText = "..."

    /** 展开后混合文字 */
    private var mExpandedMixedText: CharSequence = ""

    /** 折叠后混合文字 */
    private var mCollapsedMixedText: CharSequence = ""

    /** 文本状态 true：展开，false：折叠 */
    private var mExpanded: Boolean = false

    /** 源文字内容 */
    private var mSourceText: String? = ""

    /** 是否正在做折叠动画 */
    private var isCollapsing = false

    /** 是否正在做展开动画 */
    private var isExpanding = false

    /** 最多展示的行数 */
    var maxLineCount = 3

    /** 展开文案文字 */
    var expandText = "全文"

    /** 展开文案文字颜色 */
    var expandTextColor: Int = Color.RED

    /** 折叠文案文字 */
    var collapseText = "收起"

    /** 折叠文案文字颜色 */
    var collapseTextColor: Int = Color.BLUE

    /** 是否支持折叠功能 */
    var collapseEnable = false

    /** 是否添加下划线 */
    var underlineEnable = true

    /** 文字状态改变监听器 */
    var onTextStateChangedListener: ((state: TextState) -> Unit)? = null

    /**
     * 文字状态
     */
    enum class TextState(val state: Int) {
        /** 行数小于最小行数，不满足展开或者折叠条件 */
        None(0),

        /** 正在展开状态 */
        Expanding(1),

        /** 展开状态 */
        Expanded(2),

        /** 正在折叠状态 */
        Collapsing(3),

        /** 折叠状态 */
        Collapsed(4)
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mSourceText.isNullOrEmpty()) {
            setMeasuredDimension(measuredWidth, measuredHeight)
            return
        }
        if (isExpanding || isCollapsing) {
            return
        }
        // StaticLayout对象：文字计算辅助工具
        val staticLayout = StaticLayout(
            mSourceText,
            paint,
            measuredWidth - paddingStart - paddingEnd,
            Layout.Alignment.ALIGN_CENTER,
            lineSpacingMultiplier,
            lineSpacingExtra,
            true
        )

        // 总计行数
        val totalLineCount = staticLayout.lineCount

        // 总行数大于最大行数
        if (totalLineCount > maxLineCount) {
            mExpandedMixedText = kotlin.run {
                // 是否支持折叠功能
                if (collapseEnable) {
                    // 折叠文案和源文字组成的新的文字
                    val newText = mSourceText + collapseText
                    val spannableString = SpannableString(newText)
                    // 给折叠设成监听
                    spannableString.setSpan(
                        object : ClickableSpan() {
                            override fun updateDrawState(ds: TextPaint) {
                                super.updateDrawState(ds)
                                // 给折叠设置颜色
                                ds.color = collapseTextColor
                                // 是否给折叠添加下划线
                                ds.isUnderlineText = underlineEnable
                            }

                            override fun onClick(widget: View) {
                                onCollapsing()
                            }
                        },
                        mSourceText!!.length,
                        newText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString
                } else {
                    mSourceText!!
                }
            }

            mCollapsedMixedText = kotlin.run {
                // 省略文字和展开文案的宽度
                val dotWidth = paint.measureText(mEllipsizeText + expandText)
                // 找出显示最后一行的文字
                val start = staticLayout.getLineStart(maxLineCount - 1)
                val end = staticLayout.getLineEnd(maxLineCount - 1)
                val lineText = mSourceText!!.substring(start, end)
                // 将第最后一行最后的文字替换为 ellipsizeText和expandText
                var endIndex = 0
                for (i in lineText.length - 1 downTo 0) {
                    val str = lineText.substring(i, lineText.length)
                    // 找出文字宽度大于 ellipsizeText 的字符
                    if (paint.measureText(str) >= dotWidth) {
                        endIndex = i
                        break
                    }
                }
                // 新的文字
                val newText = mSourceText!!.substring(0, start) +
                    lineText.substring(0, endIndex) +
                    mEllipsizeText + expandText

                // 全部文字
                val spannableString = SpannableString(newText)
                // 给查看全文设成监听
                spannableString.setSpan(
                    object : ClickableSpan() {
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            // 给查看全文设置颜色
                            ds.color = expandTextColor
                            // 是否给查看全文添加下划线
                            ds.isUnderlineText = underlineEnable
                        }

                        override fun onClick(widget: View) {
                            onExpanding()
                        }
                    },
                    newText.length - expandText.length,
                    newText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString
            }
        }
        // 总行数大于最大行数
        if (totalLineCount > maxLineCount) {
            movementMethod = ClickMovementMethod.getInstance()
            text = kotlin.run {
                val (expandedHeight, collapsedHeight) = getExpandedAndCollapsedHeight()
                if (mExpanded) {
                    setMeasuredDimension(
                        measuredWidth,
                        expandedHeight + paddingTop + paddingBottom
                    )
                    mExpandedMixedText
                } else {
                    setMeasuredDimension(
                        measuredWidth,
                        collapsedHeight + paddingTop + paddingBottom
                    )
                    mCollapsedMixedText
                }
            }
        } else {
            text = mSourceText
            onTextStateChangedListener?.invoke(TextState.None)
            setMeasuredDimension(
                measuredWidth,
                staticLayout.height + paddingTop + paddingBottom
            )
        }
    }

    /**
     * 展开文字
     */
    private fun onExpanding() {
        val (expandedHeight, collapsedHeight) = getExpandedAndCollapsedHeight()
        ValueAnimator.ofFloat(0f, 1f).apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    isExpanding = true
                    movementMethod = null
                    text = mExpandedMixedText
                    onTextStateChangedListener?.invoke(TextState.Expanding)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isExpanding = false
                    movementMethod = LinkMovementMethod.getInstance()
                    toggleTextState()
                    onTextStateChangedListener?.invoke(TextState.Expanded)
                }
            })
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                layoutParams.height =
                    (collapsedHeight + (expandedHeight - collapsedHeight) * animatedValue + paddingTop + paddingBottom).toInt()
                requestLayout()
            }
            start()
        }
    }

    /**
     * 折叠文字
     */
    private fun onCollapsing() {
        val (expandedHeight, collapsedHeight) = getExpandedAndCollapsedHeight()
        ValueAnimator.ofFloat(1f, 0f).apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    isCollapsing = true
                    movementMethod = null
                    onTextStateChangedListener?.invoke(TextState.Collapsing)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isCollapsing = false
                    movementMethod = LinkMovementMethod.getInstance()
                    text = mCollapsedMixedText
                    toggleTextState()
                    onTextStateChangedListener?.invoke(TextState.Collapsed)
                }
            })
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                layoutParams.height =
                    (collapsedHeight + (expandedHeight - collapsedHeight) * animatedValue + paddingTop + paddingBottom).toInt()
                requestLayout()
            }
            start()
        }
    }

    /**
     * 获取展开后和折叠后的高度
     */
    private fun getExpandedAndCollapsedHeight(): Pair<Int, Int> = kotlin.run {
        StaticLayout(
            mExpandedMixedText,
            paint,
            measuredWidth - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_CENTER,
            lineSpacingMultiplier,
            lineSpacingExtra,
            true
        ).height to StaticLayout(
            mCollapsedMixedText,
            paint,
            measuredWidth - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_CENTER,
            lineSpacingMultiplier,
            lineSpacingExtra,
            true
        ).height
    }

    /**
     * 切换文字状态
     */
    private fun toggleTextState() {
        mExpanded = mExpanded.not()
    }

    /**
     * 设置要显示的文字以及状态
     * @param sourceText 要显示的文字
     * @param expanded   true：展开，false：折叠
     */
    fun setText(sourceText: String?, expanded: Boolean = false) {
        mSourceText = sourceText
        mExpanded = expanded
        requestLayout()
    }

    /**
     * 文本状态 true：展开，false：折叠
     */
    fun isExpanded(): Boolean = mExpanded
}