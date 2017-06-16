package com.bs.teachassistant.view.wheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.bs.teachassistant.R;
import com.bs.teachassistant.utils.DisplayUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by limh on 2017/4/13.
 */
public class WheelView extends View {
    /**
     * 滚动的间隔
     */
    private static final int SCROLLING_DURATION = 400;

    /**
     * 最小滚动的距离
     */
    private static final int MIN_DELTA_FOR_SCROLLING = 1;

    /**
     * 当前颜色
     */
    private static final int VALUE_TEXT_COLOR = 0xFF000000;

    /**
     * 文本的颜色
     */
    private static final int ITEMS_TEXT_COLOR = 0x7F000000;

    /**
     * 顶部和底部的颜色
     */
    private static final int[] SHADOWS_COLORS = new int[]{0xFFFFFFFF,
            0x7FFFFFFF, 0x7FFFFFFF};

    /** Additional items height (is added to standard text item height) */
    /**
     * 附加的高度
     */
    private static final int ADDITIONAL_ITEM_HEIGHT = 20;

    /**
     * 文字的大小
     */
    private static final int TEXT_SIZE = 16;
    /** Top and bottom items offset (to hide that) */
    /**
     * 顶部和底部的距离
     */
    private final int ITEM_OFFSET = TEXT_SIZE / 4;

    /** Additional width for items layout */
    /**
     * 附加的宽度
     */
    private static final int ADDITIONAL_ITEMS_SPACE = 2;

    /** Label offset */
    /**
     * 文本的距离
     */
    private static final int LABEL_OFFSET = 4;

    /** Left and right padding value */
    /**
     * 左右的距离
     */
    private static final int PADDING = 2;

    /** Default count of visible items */
    /**
     * 默认显示的项目
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Wheel Values
    //滚轮的值
    private WheelAdapter adapter = null;
    //所选择的当前的项目
    private int currentItem = 0;

    // 相应的宽度
    private int itemsWidth = 0;
    private int labelWidth = 0;

    //显示的项目数
    private int visibleItems = DEF_VISIBLE_ITEMS;

    // 项目的高度
    private int itemHeight = 0;

    // Text paints
    private TextPaint itemsPaint;
    private TextPaint valuePaint;

    // 相应的布局控件
    private StaticLayout itemsLayout;
    private StaticLayout labelLayout;
    private StaticLayout valueLayout;

    // 标签和文本
    private String label;
    private Drawable centerDrawable;

    // Shadows drawables
    private GradientDrawable topShadow;
    private GradientDrawable bottomShadow;

    // Scrolling
    private boolean isScrollingPerformed;
    private int scrollingOffset;

    // Scrolling animation
    private GestureDetector gestureDetector;
    private Scroller scroller;
    private int lastScrollY;

    // 是不是循环
    boolean isCyclic = true;

    // Listeners
    private List<OnWheelChangedListener> changingListeners = new LinkedList<>();
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<>();
    private Context context;

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    /**
     * Constructor
     */
    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    /**
     * Constructor
     */
    public WheelView(Context context) {
        super(context);
        initData(context);

    }

    /**
     * Initializes class data
     *
     * @param context the context
     */
    private void initData(Context context) {
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        this.context = context;
        scroller = new Scroller(context);
    }

    /**
     * Gets wheel adapter
     *
     * @return the adapter
     */
    public WheelAdapter getAdapter() {
        return adapter;
    }

    /**
     * Sets wheel adapter
     *
     * @param adapter the new wheel adapter
     */
    public void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        invalidateLayouts();
        invalidate();
    }

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(getContext(), interpolator);
    }

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * Sets count of visible items
     *
     * @param count the new count
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
        invalidate();
    }

    /**
     * Gets label
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets label
     *
     * @param newLabel the label to set
     */
    public void setLabel(String newLabel) {
        if (label == null || !label.equals(newLabel)) {
            label = newLabel;
            labelLayout = null;
            invalidate();
        }
    }

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * Removes wheel changing listener
     *
     * @param listener the listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        changingListeners.remove(listener);
    }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * Adds wheel scrolling listener
     *
     * @param listener the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.remove(listener);
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    public void setCurrentItem(int index, boolean animated) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return; // throw?
        }
        if (index < 0 || index >= adapter.getItemsCount()) {
            if (isCyclic) {
                while (index < 0) {
                    index += adapter.getItemsCount();
                }
                index %= adapter.getItemsCount();
            } else {
                return; // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                scroll(index - currentItem, SCROLLING_DURATION);
            } else {
                invalidateLayouts();

                int old = currentItem;
                currentItem = index;

                notifyChangingListeners(old, currentItem);

                invalidate();
            }
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
     *
     * @return true if wheel is cyclic
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * Set wheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;

        invalidate();
        invalidateLayouts();
    }

    /**
     * Invalidates layouts
     */
    private void invalidateLayouts() {
        itemsLayout = null;
        valueLayout = null;
        scrollingOffset = 0;
    }

    /**
     * Initializes resources
     */
    private void initResourcesIfNecessary() {
        if (itemsPaint == null) {
            itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            itemsPaint.setTextSize(DisplayUtil.sp2px(context, TEXT_SIZE));
        }
        //选中值字体大小 样式
        if (valuePaint == null) {
            valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            valuePaint.setTextSize(DisplayUtil.sp2px(context, TEXT_SIZE));
            valuePaint.setShadowLayer(0.2f, 0, 0.2f, 0xFFC0C0C0);
        }
        //选中颜色
        if (centerDrawable == null) {
            centerDrawable = getContext().getResources().getDrawable(R.color.colorTransparent);
        }

        if (topShadow == null) {
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }

        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }
        setBackgroundResource(R.color.colorWhite);
    }

    /**
     * Calculates desired height for layout
     *
     * @param layout the source layout
     * @return the desired layout height
     */
    private int getDesiredHeight(Layout layout) {
        if (layout == null) {
            return 0;
        }

        int desired = getItemHeight() * visibleItems - DisplayUtil.dip2px(context,ITEM_OFFSET * 2)
                - DisplayUtil.dip2px(context,ADDITIONAL_ITEM_HEIGHT);

        desired = Math.max(desired, getSuggestedMinimumHeight());

        return desired;
    }

    /**
     * Returns text item by index
     *
     * @param index the item index
     * @return the item or null
     */
    private String getTextItem(int index) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return null;
        }
        int count = adapter.getItemsCount();
        if ((index < 0 || index >= count) && !isCyclic) {
            return null;
        } else {
            while (index < 0) {
                index = count + index;
            }
        }

        index %= count;
        return adapter.getItem(index);
    }

    /**
     * Builds text depending on current value
     *
     * @param useCurrentValue
     * @return the text
     */
    private String buildText(boolean useCurrentValue) {
        StringBuilder itemsText = new StringBuilder();
        int addItems = visibleItems / 2 + 1;

        for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
            if (useCurrentValue || i != currentItem) {
                String text = getTextItem(i);
                if (text != null) {
                    itemsText.append(text);
                }
            }
            if (i < currentItem + addItems) {
                itemsText.append("\n");
            }
        }

        return itemsText.toString();
    }

    /**
     * Returns the max item length that can be present
     *
     * @return the max length
     */
    private int getMaxTextLength() {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return 0;
        }

        int adapterLength = adapter.getMaximumLength();
        if (adapterLength > 0) {
            return adapterLength;
        }

        String maxText = null;
        int addItems = visibleItems / 2;
        for (int i = Math.max(currentItem - addItems, 0);
             i < Math.min(currentItem + visibleItems, adapter.getItemsCount()); i++) {
            String text = adapter.getItem(i);
            if (text != null && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }

        return maxText != null ? maxText.length() : 0;
    }

    /**
     * Returns height of wheel item
     *
     * @return the item height
     */
    private int getItemHeight() {
        if (itemHeight != 0) {
            return itemHeight;
        } else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
            itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
            return itemHeight;
        }

        return getHeight() / visibleItems;
    }

    /**
     * Calculates control width and creates text layouts
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();

        int width = widthSize;

        int maxLength = getMaxTextLength();
        if (maxLength > 0) {
            float textWidth = (float) Math.ceil(Layout.getDesiredWidth("0", itemsPaint));
            itemsWidth = (int) (maxLength * textWidth);
        } else {
            itemsWidth = 0;
        }
        itemsWidth += ADDITIONAL_ITEMS_SPACE; // make it some more

        labelWidth = 0;
        if (label != null && label.length() > 0) {
            labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label, valuePaint));
        }

        boolean recalculate = false;
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
            recalculate = true;
        } else {
            width = itemsWidth + labelWidth + 2 * DisplayUtil.dip2px(context,PADDING);
            if (labelWidth > 0) {
                width += DisplayUtil.dip2px(context,LABEL_OFFSET);
            }

            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
                recalculate = true;
            }
        }

        if (recalculate) {
            // recalculate width
            int pureWidth = width - DisplayUtil.dip2px(context,LABEL_OFFSET) - 2 * DisplayUtil.dip2px(context,PADDING);
            if (pureWidth <= 0) {
                itemsWidth = labelWidth = 0;
            }
            if (labelWidth > 0) {
                double newWidthItems = (double) itemsWidth * pureWidth
                        / (itemsWidth + labelWidth);
                itemsWidth = (int) newWidthItems;
                labelWidth = pureWidth - itemsWidth;
            } else {
                itemsWidth = pureWidth + DisplayUtil.dip2px(context,LABEL_OFFSET); // no label
            }
        }

        if (itemsWidth > 0) {
            createLayouts(itemsWidth, labelWidth);
        }

        return width;
    }

    /**
     * Creates layouts
     *
     * @param widthItems width of items layout
     * @param widthLabel width of label layout
     */
    private void createLayouts(int widthItems, int widthLabel) {
        if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
            itemsLayout = new StaticLayout(buildText(isScrollingPerformed), itemsPaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER,
                    1, DisplayUtil.dip2px(context,ADDITIONAL_ITEM_HEIGHT), false);
        } else {
            itemsLayout.increaseWidthTo(widthItems);
        }

        if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
            String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
            valueLayout = new StaticLayout(text != null ? text : "",
                    valuePaint, widthItems, widthLabel > 0 ?
                    Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER,
                    1, DisplayUtil.dip2px(context,ADDITIONAL_ITEM_HEIGHT), false);
        } else if (isScrollingPerformed) {
            valueLayout = null;
        } else {
            valueLayout.increaseWidthTo(widthItems);
        }

        if (widthLabel > 0) {
            if (labelLayout == null || labelLayout.getWidth() > widthLabel) {
                labelLayout = new StaticLayout(label, valuePaint,
                        widthLabel, Layout.Alignment.ALIGN_NORMAL, 1,
                        DisplayUtil.dip2px(context,ADDITIONAL_ITEM_HEIGHT), false);
            } else {
                labelLayout.increaseWidthTo(widthLabel);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight(itemsLayout);

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (itemsLayout == null) {
            if (itemsWidth == 0) {
                calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            } else {
                createLayouts(itemsWidth, labelWidth);
            }
        }

        if (itemsWidth > 0) {
            canvas.save();
            // Skip padding space and hide a part of top and bottom items
            canvas.translate(DisplayUtil.dip2px(context,PADDING), -DisplayUtil.dip2px(context,ITEM_OFFSET));
            drawItems(canvas);
            drawValue(canvas);
            canvas.restore();
        }

        drawCenterRect(canvas);
        drawShadows(canvas);
    }

    /**
     * Draws shadows on top and bottom of control
     *
     * @param canvas the canvas for drawing
     */
    private void drawShadows(Canvas canvas) {
        topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
        topShadow.draw(canvas);

        bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems,
                getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }

    /**
     * Draws value and label layout
     *
     * @param canvas the canvas for drawing
     */
    private void drawValue(Canvas canvas) {
        valuePaint.setColor(VALUE_TEXT_COLOR);
        valuePaint.drawableState = getDrawableState();

        Rect bounds = new Rect();
        itemsLayout.getLineBounds(visibleItems / 2, bounds);

        // draw label
        if (labelLayout != null) {
            canvas.save();
            canvas.translate(itemsLayout.getWidth() + DisplayUtil.dip2px(context,LABEL_OFFSET), bounds.top);
            labelLayout.draw(canvas);
            canvas.restore();
        }

        // draw current value
        if (valueLayout != null) {
            canvas.save();
            canvas.translate(0, bounds.top + scrollingOffset);
            valueLayout.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * Draws items
     *
     * @param canvas the canvas for drawing
     */
    private void drawItems(Canvas canvas) {
        canvas.save();

        int top = itemsLayout.getLineTop(1);
        canvas.translate(0, -top + scrollingOffset);

        itemsPaint.setColor(ITEMS_TEXT_COLOR);
        itemsPaint.drawableState = getDrawableState();
        itemsLayout.draw(canvas);

        canvas.restore();
    }

    /**
     * Draws rect for current value
     *
     * @param canvas the canvas for drawing
     */
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = getItemHeight() / 2;
        centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
        centerDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return true;
        }

        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }
        return true;
    }

    /**
     * Scrolls the wheel
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;

        int count = scrollingOffset / getItemHeight();
        int pos = currentItem - count;
        if (isCyclic && adapter.getItemsCount() > 0) {
            // fix position by rotating
            while (pos < 0) {
                pos += adapter.getItemsCount();
            }
            pos %= adapter.getItemsCount();
        } else if (isScrollingPerformed) {
            //
            if (pos < 0) {
                count = currentItem;
                pos = 0;
            } else if (pos >= adapter.getItemsCount()) {
                count = currentItem - adapter.getItemsCount() + 1;
                pos = adapter.getItemsCount() - 1;
            }
        } else {
            // fix position
            pos = Math.max(pos, 0);
            pos = Math.min(pos, adapter.getItemsCount() - 1);
        }

        int offset = scrollingOffset;
        if (pos != currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }

        // update offset
        scrollingOffset = offset - count * getItemHeight();
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    // gesture listener
    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            if (isScrollingPerformed) {
                scroller.forceFinished(true);
                clearMessages();
                return true;
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            startScrolling();
            doScroll((int) -distanceY);
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastScrollY = currentItem * getItemHeight() + scrollingOffset;
            int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
            int minY = isCyclic ? -maxY : 0;
            scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY, maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    // Messages
    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    /**
     * Set next message to queue. Clears queue before.
     *
     * @param message the message to set
     */
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    /**
     * Clears messages from queue
     */
    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    // animation handler
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
                doScroll(delta);
            }

            // scrolling is not finished when it comes to final Y
            // so, finish it manually
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = scroller.getFinalY();
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    /**
     * Justifies wheel
     */
    private void justify() {
        if (adapter == null) {
            return;
        }

        lastScrollY = 0;
        int offset = scrollingOffset;
        int itemHeight = getItemHeight();
        boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0;
        if ((isCyclic || needToIncrease) && Math.abs((float) offset) > (float) itemHeight / 2) {
            if (offset < 0)
                offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
            else
                offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
        }
        if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
            scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
            setNextMessage(MESSAGE_JUSTIFY);
        } else {
            finishScrolling();
        }
    }

    /**
     * Starts scrolling
     */
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }
    }

    /**
     * Finishes scrolling
     */
    void finishScrolling() {
        if (isScrollingPerformed) {
            notifyScrollingListenersAboutEnd();
            isScrollingPerformed = false;
        }
        invalidateLayouts();
        invalidate();
    }


    /**
     * Scroll the wheel
     *
     * @param itemsToScroll items to scroll
     * @param time          scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        scroller.forceFinished(true);

        lastScrollY = scrollingOffset;
        int offset = itemsToScroll * getItemHeight();

        scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
        setNextMessage(MESSAGE_SCROLL);

        startScrolling();
    }

}
