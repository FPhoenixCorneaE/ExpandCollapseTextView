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
        defStyleAttr: Int = 0
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

    /** 最多展示的行数 */
    var mMaxLineCount = 3

    /** 展开文案文字 */
    var mExpandText = "全文"

    /** 展开文案文字颜色 */
    var mExpandTextColor: Int = Color.RED

    /** 折叠文案文字 */
    var mCollapseText = "收起"

    /** 折叠文案文字颜色 */
    var mCollapseTextColor: Int = Color.BLUE

    /** 是否支持折叠功能 */
    var mCollapseEnable = false

    /** 是否添加下划线 */
    var mUnderlineEnable = true

    /** 文字状态改变监听器 */
    var mOnTextStateChangedListener: ((state: TextState) -> Unit)? = null

    var isCollapsing = false
    var isExpanding = false

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

        when {
            // 总行数大于最大行数
            totalLineCount > mMaxLineCount -> {
                mExpandedMixedText = kotlin.run {
                    when {
                        // 是否支持折叠功能
                        mCollapseEnable -> {
                            // 折叠文案和源文字组成的新的文字
                            val newText = mSourceText + mCollapseText
                            val spannableString = SpannableString(newText)
                            // 给折叠设成监听
                            spannableString.setSpan(
                                    object : ClickableSpan() {
                                        override fun updateDrawState(ds: TextPaint) {
                                            super.updateDrawState(ds)
                                            // 给折叠设置颜色
                                            ds.color = mCollapseTextColor
                                            // 是否给折叠添加下划线
                                            ds.isUnderlineText = mUnderlineEnable
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
                        }
                        else -> {
                            mSourceText!!
                        }
                    }
                }

                mCollapsedMixedText = kotlin.run {
                    // 省略文字和展开文案的宽度
                    val dotWidth = paint.measureText(mEllipsizeText + mExpandText)
                    // 找出显示最后一行的文字
                    val start = staticLayout.getLineStart(mMaxLineCount - 1)
                    val end = staticLayout.getLineEnd(mMaxLineCount - 1)
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
                            mEllipsizeText + mExpandText

                    // 全部文字
                    val spannableString = SpannableString(newText)
                    // 给查看全文设成监听
                    spannableString.setSpan(
                            object : ClickableSpan() {
                                override fun updateDrawState(ds: TextPaint) {
                                    super.updateDrawState(ds)
                                    // 给查看全文设置颜色
                                    ds.color = mExpandTextColor
                                    // 是否给查看全文添加下划线
                                    ds.isUnderlineText = mUnderlineEnable
                                }

                                override fun onClick(widget: View) {
                                    onExpanding()
                                }
                            },
                            newText.length - mExpandText.length,
                            newText.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString
                }
            }
        }
        when {
            // 总行数大于最大行数
            totalLineCount > mMaxLineCount -> {
                movementMethod = LinkMovementMethod.getInstance()
                text = kotlin.run {
                    val (expandedHeight, collapsedHeight) = getExpandedAndCollapsedHeight()
                    when {
                        mExpanded -> {
                            setMeasuredDimension(
                                    measuredWidth,
                                    expandedHeight + paddingTop + paddingBottom
                            )
                            mExpandedMixedText
                        }
                        else -> {
                            setMeasuredDimension(
                                    measuredWidth,
                                    collapsedHeight + paddingTop + paddingBottom
                            )
                            mCollapsedMixedText
                        }
                    }
                }
            }
            else -> {
                text = mSourceText
                mOnTextStateChangedListener?.invoke(TextState.None)
                setMeasuredDimension(
                        measuredWidth,
                        staticLayout.height + paddingTop + paddingBottom
                )
            }
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
                    mOnTextStateChangedListener?.invoke(TextState.Expanding)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isExpanding = false
                    movementMethod = LinkMovementMethod.getInstance()
                    toggleTextState()
                    mOnTextStateChangedListener?.invoke(TextState.Expanded)
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
                    mOnTextStateChangedListener?.invoke(TextState.Collapsing)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isCollapsing = false
                    movementMethod = LinkMovementMethod.getInstance()
                    text = mCollapsedMixedText
                    toggleTextState()
                    mOnTextStateChangedListener?.invoke(TextState.Collapsed)
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
    private fun getExpandedAndCollapsedHeight(): Pair<Int, Int> {
        return Pair(
                StaticLayout(
                        mExpandedMixedText,
                        paint,
                        measuredWidth - paddingLeft - paddingRight,
                        Layout.Alignment.ALIGN_CENTER,
                        lineSpacingMultiplier,
                        lineSpacingExtra,
                        true
                ).height,
                StaticLayout(
                        mCollapsedMixedText,
                        paint,
                        measuredWidth - paddingLeft - paddingRight,
                        Layout.Alignment.ALIGN_CENTER,
                        lineSpacingMultiplier,
                        lineSpacingExtra,
                        true
                ).height
        )
    }

    /**
     * 切换文字状态
     */
    private fun toggleTextState() {
        mExpanded = !mExpanded
    }

    /**
     * 设置要显示的文字以及状态
     * @param sourceText 要显示的文字
     * @param expanded   true：展开，false：折叠
     */
    fun setText(sourceText: String?, expanded: Boolean) {
        mSourceText = sourceText
        mExpanded = expanded
        requestLayout()
    }

    /**
     * 文本状态 true：展开，false：折叠
     */
    fun isExpanded(): Boolean = mExpanded
}