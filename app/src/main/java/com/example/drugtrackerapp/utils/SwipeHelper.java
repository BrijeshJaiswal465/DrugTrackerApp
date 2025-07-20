package com.example.drugtrackerapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * An abstract class that extends ItemTouchHelper.SimpleCallback to provide swipe-to-reveal functionality
 * for RecyclerView items. This helper creates buttons that appear when a user swipes an item left,
 * allowing for actions like delete, edit, etc.
 */
public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    private RecyclerView recyclerView;
    private List<UnderlayButton> buttons;
    private GestureDetector gestureDetector;
    private int swipedPos = -1;
    private final int buttonSize;
    private static int textSize = 0;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<UnderlayButton>> buttonsBuffer;
    private Queue<Integer> recoverQueue;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons) {
                if (button.onClick(e.getX(), e.getY()))
                    break;
            }

            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos);
            if (swipedViewHolder != null) {
                View swipedItem = swipedViewHolder.itemView;
                Rect rect = new Rect();
                swipedItem.getGlobalVisibleRect(rect);

                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
                    if (rect.top < point.y && rect.bottom > point.y)
                        gestureDetector.onTouchEvent(e);
                    else {
                        recoverQueue.add(swipedPos);
                        swipedPos = -1;
                        recoverSwipedItem();
                    }
                }
            }
            return false;
        }
    };

    /**
     * Constructor for SwipeHelper.
     * 
     * @param context The context used for gesture detection
     * @param recyclerView The RecyclerView to attach this swipe helper to
     * @param buttonSize The size of the buttons that appear when swiping
     * @param textSize The text size for button labels
     */
    public SwipeHelper(Context context, RecyclerView recyclerView, int buttonSize, int textSize) {
        super(0, ItemTouchHelper.LEFT);
        mContext = context;
        this.recyclerView = recyclerView;
        this.buttonSize = buttonSize;
        this.textSize = textSize;
        this.buttons = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        buttonsBuffer = new HashMap<>();
        recoverQueue = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer o) {
                if (contains(o))
                    return false;
                else
                    return super.add(o);
            }
        };

        attachSwipe();
    }


    /**
     * Called when ItemTouchHelper wants to move the dragged item from its old position to
     * the new position. This implementation returns false as we don't support drag and drop.
     * 
     * @param recyclerView The RecyclerView to which the ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being dragged
     * @param target The ViewHolder over which the currently active item is being dragged
     * @return false since drag and drop is not supported
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Called when a ViewHolder is swiped by the user.
     * This method handles the swipe action, updates the swiped position, and prepares the buttons to be displayed.
     * 
     * @param viewHolder The ViewHolder that was swiped by the user
     * @param direction The direction to which the ViewHolder was swiped
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (swipedPos != pos)
            recoverQueue.add(swipedPos);

        swipedPos = pos;

        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer.get(swipedPos);
        else
            buttons.clear();

        buttonsBuffer.clear();
        swipeThreshold = 0.5f * buttons.size() * buttonSize;
        recoverSwipedItem();
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if (pos < 0) {
            swipedPos = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                List<UnderlayButton> buffer = new ArrayList<>();

                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                } else {
                    buffer = buttonsBuffer.get(pos);
                }

                translationX = dX * buffer.size() * buttonSize / itemView.getWidth();
                drawButtons(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    /**
     * Recovers swiped items by notifying the adapter to redraw them.
     * This method is synchronized to prevent concurrent modification issues.
     */
    private synchronized void recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            int pos = recoverQueue.poll();
            if (pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    /**
     * Draws the underlay buttons on the canvas when an item is swiped.
     * 
     * @param c The canvas on which to draw the buttons
     * @param itemView The view of the swiped item
     * @param buffer The list of buttons to draw
     * @param pos The position of the swiped item in the adapter
     * @param dX The horizontal displacement caused by the swipe
     */
    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX) {
        float right = itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (UnderlayButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            left,
                            itemView.getTop(),
                            right,
                            itemView.getBottom()
                    ),
                    pos
            );

            right = left;
        }
    }

    /**
     * Attaches this SwipeHelper to the RecyclerView.
     * This method creates an ItemTouchHelper with this SwipeHelper as the callback
     * and attaches it to the RecyclerView.
     */
    public void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Abstract method that must be implemented by subclasses to create the buttons
     * that will be displayed when an item is swiped.
     * 
     * @param viewHolder The ViewHolder for which to create buttons
     * @param underlayButtons The list to which the created buttons should be added
     */
    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

    /**
     * Inner class representing a button that appears when an item is swiped.
     * Each button has text, an optional image, a background color, and a click listener.
     */
    public static class UnderlayButton {
        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private RectF clickRegion;
        private UnderlayButtonClickListener clickListener;

        /**
         * Constructor for UnderlayButton.
         * 
         * @param text The text to display on the button
         * @param imageResId The resource ID of an image to display on the button (can be 0 for no image)
         * @param color The background color of the button
         * @param clickListener The listener to call when the button is clicked
         */
        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
        }

        /**
         * Handles click events on the button.
         * 
         * @param x The x-coordinate of the click
         * @param y The y-coordinate of the click
         * @return true if the click was handled, false otherwise
         */
        public boolean onClick(float x, float y) {
            if (clickRegion != null && clickRegion.contains(x, y)) {
                clickListener.onClick(pos);
                return true;
            }

            return false;
        }

        /**
         * Draws the button on the canvas.
         * 
         * @param c The canvas on which to draw
         * @param rect The rectangle defining the button's bounds
         * @param pos The position of the item in the adapter
         */
        public void onDraw(Canvas c, RectF rect, int pos) {
            Paint p = new Paint();

            // Draw background
            p.setColor(color);
            c.drawRect(rect, p);

            // Draw Text
            p.setColor(Color.WHITE);
            p.setTextSize(textSize);

            Rect r = new Rect();
            float cHeight = rect.height();
            float cWidth = rect.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            c.drawText(text, rect.left + x, rect.top + y, p);

            clickRegion = rect;
            this.pos = pos;
        }
    }

    /**
     * Interface for handling clicks on underlay buttons.
     */
    public interface UnderlayButtonClickListener {
        /**
         * Called when an underlay button is clicked.
         * 
         * @param pos The position of the item in the adapter
         */
        void onClick(int pos);
    }
}