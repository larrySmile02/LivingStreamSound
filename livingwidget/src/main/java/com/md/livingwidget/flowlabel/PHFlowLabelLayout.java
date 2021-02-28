package com.md.livingwidget.flowlabel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.md.basedpc.PHScreenUtils;
import com.md.livingwidget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 朱大可 on 2019年04月17日13:32:37
 * 流式布局
 */
public class PHFlowLabelLayout extends ViewGroup {
    private List<ChildPos> mChildPos = new ArrayList<ChildPos>();//记录每个View的位置
    private float textSize;//文字大小
    private int textColor;//文字颜色
    private int textColorSelector;//选中文字颜色
    private float shapeRadius;//圆角大小
    private int shapeRadiusType;//圆角位置 0 所有 1左上 2 右上 4 左下 8 右下
    private int shapeLineColor;//描边颜色
    private int shapeBackgroundColor;//背景颜色
    private int shapeBackgroundColorSelector;//选中背景颜色
    private float shapeLineWidth;//描边大小
    private List<CharSequence> mAllSelectedWords = new ArrayList<>();//记录所有选中着的词

    private boolean isLabelPaddingSet = false;
    private int labelPaddingLeft = -1;
    private int labelPaddingTop = -1;
    private int labelPaddingRight = -1;
    private int labelPaddingBottom = -1;

    private class ChildPos {//子view间距
        int left, top, right, bottom;

        public ChildPos(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public PHFlowLabelLayout(Context context) {
        this(context, null);
    }

    public PHFlowLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initAttributes(context, attrs);
    }

    /**
     * 最终调用这个构造方法
     *
     * @param context  上下文
     * @param attrs    xml属性集合
     * @param defStyle Theme中定义的style
     */
    public PHFlowLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 流式布局属性设置
     *
     * @param context
     * @param attrs
     */
    @SuppressLint("ResourceAsColor")
    private void initAttributes(Context context, AttributeSet attrs) {
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KingoitFlowLayout);
        textSize = typedArray.getDimension(R.styleable.KingoitFlowLayout_flowLayoutTextSize, 16);
        textColor = typedArray.getColor(R.styleable.KingoitFlowLayout_flowLayoutTextColor, Color.parseColor("#FF4081"));
        textColorSelector = typedArray.getResourceId(R.styleable.KingoitFlowLayout_flowLayoutTextColorSelector, 0);
        shapeRadius = typedArray.getDimension(R.styleable.KingoitFlowLayout_flowLayoutRadius, 40f);
        shapeRadiusType = typedArray.getInt(R.styleable.KingoitFlowLayout_flowLayoutRadiusType, 0);
        shapeLineColor = typedArray.getColor(R.styleable.KingoitFlowLayout_flowLayoutLineColor, Color.parseColor("#ADADAD"));
        shapeBackgroundColor = typedArray.getColor(R.styleable.KingoitFlowLayout_flowLayoutBackgroundColor, Color.parseColor("#c5cae9"));
        shapeBackgroundColorSelector = typedArray.getResourceId(R.styleable.KingoitFlowLayout_flowLayoutBackgroundColorSelector, 0);
        shapeLineWidth = typedArray.getDimension(R.styleable.KingoitFlowLayout_flowLayoutLineWidth, 0f);
    }

    /**
     * 测量宽度和高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取流式布局的宽度和模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取流式布局的高度和模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //使用wrap_content的流式布局的最终宽度和高度
        int width = 0, height = 0;
        //记录每一行的宽度和高度
        int lineWidth = 0, lineHeight = 0;
        //得到内部元素的个数
        int count = getChildCount();
        mChildPos.clear();
        for (int i = 0; i < count; i++) {
            //获取对应索引的view
            View child = getChildAt(i);
            //测量子view的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子view占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子view占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //换行
            if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                //取最大的行宽为流式布局宽度
                width = Math.max(width, lineWidth);
                //叠加行高得到流式布局高度
                height += lineHeight;
                //重置行宽度为第一个View的宽度
                lineWidth = childWidth;
                //重置行高度为第一个View的高度
                lineHeight = childHeight;
                //记录位置
                mChildPos.add(new ChildPos(
                        getPaddingLeft() + lp.leftMargin,
                        getPaddingTop() + height + lp.topMargin,
                        getPaddingLeft() + childWidth - lp.rightMargin,
                        getPaddingTop() + height + childHeight - lp.bottomMargin));
            } else {  //不换行
                //记录位置
                mChildPos.add(new ChildPos(
                        getPaddingLeft() + lineWidth + lp.leftMargin,
                        getPaddingTop() + height + lp.topMargin,
                        getPaddingLeft() + lineWidth + childWidth - lp.rightMargin,
                        getPaddingTop() + height + childHeight - lp.bottomMargin));
                //叠加子View宽度得到新行宽度
                lineWidth += childWidth;
                //取当前行子View最大高度作为行高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //最后一个控件
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        // 得到最终的宽高
        // 宽度：如果是AT_MOST模式，则使用我们计算得到的宽度值，否则遵循测量值
        // 高度：只要布局中内容的高度大于测量高度，就使用内容高度（无视测量模式）；否则才使用测量高度
        // Fix: heightMode可能为 MeasureSpec.UNSPECIFIED, 此时使用测量高度
        int flowLayoutWidth = widthMode == MeasureSpec.AT_MOST ? width + getPaddingLeft() + getPaddingRight() : widthSize;
        int flowLayoutHeight = heightMode != MeasureSpec.EXACTLY ? height + getPaddingTop() + getPaddingBottom() : heightSize;
        //真实高度
        realHeight = height + getPaddingTop() + getPaddingBottom();
        //测量高度
        measuredHeight = heightSize;
        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = Math.max(measuredHeight, realHeight);
        }
        scrollable = realHeight > measuredHeight;
        // 设置最终的宽高
        if (maxLines > 0) {
            if ((flowLayoutHeight / lineHeight) > maxLines) {
                flowLayoutHeight = lineHeight * maxLines;
            }
        }
        setMeasuredDimension(flowLayoutWidth, flowLayoutHeight - (getPercentWidthSizeBigger(21)));
    }

    public int getPercentWidthSizeBigger(int val) {
        int screenWidth = PHScreenUtils.getScreenWidth(getContext());
        int designWidth = 1080;
        int res = val * screenWidth;
        return res % designWidth == 0 ? res / designWidth : res / designWidth + 1;
    }

    /**
     * 让ViewGroup能够支持margin属性
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 设置每个View的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ChildPos pos = mChildPos.get(i);
            //设置View的左边、上边、右边底边位置
            child.layout(pos.left, pos.top, pos.right, pos.bottom);
        }
    }

    /**
     * 添加子view方法
     *
     * @param inflater
     * @param tvName   view名称
     * @param isSelect 是否选中
     */
    @SuppressLint("WrongConstant")
    public void addItemView(LayoutInflater inflater, CharSequence tvName, boolean isSelect) {
        //加载 ItemView并设置名称，并设置名称
        View view = inflater.inflate(R.layout.flow_lable_layout, this, false);
        TextView textView = view.findViewById(R.id.value);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if (textColorSelector != 0) {
            ColorStateList csl = getResources().getColorStateList(textColorSelector);
            textView.setTextColor(csl);
        } else {
            textView.setTextColor(textColor);
        }
        GradientDrawable drawable = new GradientDrawable();
        if (shapeRadiusType > 0) {
            float[] yuan = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
            switch (shapeRadiusType) {
                case 1://左上
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    break;
                case 2://右上
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    break;
                case 4://右下
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    break;
                case 8://左下
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;

                case 9://左上右下
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;
                case 3://左上右上
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    break;
                case 5: //左上左下
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    break;
                case 6://右上左下
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    break;
                case 10://右上右下
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;
                case 12://左下右下
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;

                case 7://左上右上左下
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    break;
                case 13://左上左下右下
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;
                case 14://右上左下右下
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    yuan[4] = shapeRadius;
                    yuan[5] = shapeRadius;
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;
                case 11://左上右上右下
                    yuan[0] = shapeRadius;
                    yuan[1] = shapeRadius;
                    yuan[2] = shapeRadius;
                    yuan[3] = shapeRadius;
                    yuan[6] = shapeRadius;
                    yuan[7] = shapeRadius;
                    break;
            }
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setGradientType(GradientDrawable.RECTANGLE);
            drawable.setCornerRadii(yuan);
        } else {
            drawable.setCornerRadius(shapeRadius);
        }

        textView.setTextColor(textColor);
        drawable.setColor(shapeBackgroundColor);
        if (shapeLineWidth > 0)
            drawable.setStroke((int) shapeLineWidth, shapeLineColor);
        textView.setBackgroundDrawable(drawable);
        textView.setText(tvName);

        updatePaddingOnView(textView);
        //把 ItemView加入流式布局
        this.addView(view);
    }

    private boolean scrollable; // 是否可以滚动
    private int measuredHeight; // 测量得到的高度
    private int realHeight; // 整个流式布局控件的实际高度
    private int scrolledHeight = 0; // 已经滚动过的高度
    private int startY; // 本次滑动开始的Y坐标位置
    private int offsetY; // 本次滑动的偏移量
    private boolean pointerDown; // 在ACTION_MOVE中，视第一次触发为手指按下，从第二次触发开始计入正式滑动

    /**
     * 滚动事件的处理，当布局可以滚动（内容高度大于测量高度）时，对手势操作进行处理
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 只有当布局可以滚动的时候（内容高度大于测量高度的时候），且处于拦截模式，才会对手势操作进行处理
        if (scrollable && isInterceptedTouch) {
            int currY = (int) event.getY();
            switch (event.getAction()) {
                // 因为ACTION_DOWN手势可能是为了点击布局中的某个子元素，因此在onInterceptTouchEvent()方法中没有拦截这个手势
                // 因此，在这个事件中不能获取到startY，也因此才将startY的获取移动到第一次滚动的时候进行
                case MotionEvent.ACTION_DOWN:
                    break;
                // 当第一次触发ACTION_MOVE事件时，视为手指按下；以后的ACTION_MOVE事件才视为滚动事件
                case MotionEvent.ACTION_MOVE:
                    // 用pointerDown标志位只是手指是否已经按下
                    if (!pointerDown) {
                        startY = currY;
                        pointerDown = true;
                    } else {
                        offsetY = startY - currY; // 下滑大于0
                        // 布局中的内容跟随手指的滚动而滚动
                        // 用scrolledHeight记录以前的滚动事件中滚动过的高度（因为不一定每一次滚动都是从布局的最顶端开始的）
                        this.scrollTo(0, scrolledHeight + offsetY);
                    }
                    break;
                // 手指抬起时，更新scrolledHeight的值；
                // 如果滚动过界（滚动到高于布局最顶端或低于布局最低端的时候），设置滚动回到布局的边界处
                case MotionEvent.ACTION_UP:
                    scrolledHeight += offsetY;
                    if (scrolledHeight + offsetY < 0) {
                        this.scrollTo(0, 0);
                        scrolledHeight = 0;
                    } else if (scrolledHeight + offsetY + measuredHeight > realHeight) {
                        this.scrollTo(0, realHeight - measuredHeight);
                        scrolledHeight = realHeight - measuredHeight;
                    }
                    // 手指抬起后别忘了重置这个标志位
                    pointerDown = false;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 事件拦截，当手指按下或抬起的时候不进行拦截（因为可能这个操作只是点击了布局中的某个子元素）；
     * 当手指移动的时候，才将事件拦截；
     * 因增加最小滑动距离防止点击时误触滑动
     */
    private boolean isInterceptedTouch;
    private int startYY = 0;
    private boolean pointerDownY;
    private int minDistance = 10;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int currY = (int) ev.getY();
        int offsetY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointerDownY = true;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerDownY) {
                    startYY = currY;
                } else {
                    offsetY = currY - startYY;
                }
                pointerDownY = false;
                intercepted = Math.abs(offsetY) > minDistance;
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起后别忘了重置这个标志位
                intercepted = false;
                break;
            default:
                break;
        }
        isInterceptedTouch = intercepted;
        return intercepted;
    }


    /**
     * 流式布局显示
     * Toast.makeText(FlowLayoutActivity.this, keywords, Toast.LENGTH_SHORT).show();
     *
     * @param list
     */
    public void showTag(final List<? extends CharSequence> list, final ItemClickListener listener) {
        if (list == null || list.isEmpty())
            return;
        removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final CharSequence keywords = list.get(i);
            addItemView(LayoutInflater.from(getContext()), keywords, false);
            final int finalI = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    View child = getChildAt(finalI);
                    child.setSelected(!child.isSelected());
                    if (child.isSelected()) {
                        mAllSelectedWords.add(list.get(finalI));
                    } else {
                        mAllSelectedWords.remove(list.get(finalI));
                    }
                    if (listener != null) {
                        listener.onClick(keywords, mAllSelectedWords, finalI);
                    }
                }
            });
        }
    }

    public void setLabelTextPadding(int left, int top, int right, int bottom) {
        isLabelPaddingSet = true;
        labelPaddingLeft = getPercentWidthSizeBigger(left);
        labelPaddingTop = getPercentWidthSizeBigger(top);
        labelPaddingRight = getPercentWidthSizeBigger(right);
        labelPaddingBottom = getPercentWidthSizeBigger(bottom);
    }

    private void updatePaddingOnView(TextView textView) {
        if (isLabelPaddingSet) {
            textView.setPadding(labelPaddingLeft, labelPaddingTop, labelPaddingRight, labelPaddingBottom);
        }
    }

    private int maxLines = -1;//最大显示行数

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public interface ItemClickListener {
        /**
         * item 点击事件
         *
         * @param currentSelectedkeywords
         * @param allSelectedKeywords
         */
        void onClick(CharSequence currentSelectedkeywords, List<CharSequence> allSelectedKeywords, int postion);
    }
}
