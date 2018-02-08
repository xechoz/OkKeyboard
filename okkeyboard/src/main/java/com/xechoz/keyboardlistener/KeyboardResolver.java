package com.xechoz.keyboardlistener;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * Created by xechoz on 2018/2/8.
 */

public class KeyboardResolver implements KeyboardChangedResolver {
    private InnerOnGlobalLayoutListener layoutListener;
    private WeakReference<KeyboardChangedListener> keyboardListenerRef;
    private WeakReference<View> reference;
    private boolean isPause = false;
    private int positionY;

    /**
     * 这个view，在键盘出来的时候，会被顶起来；键盘收起，就恢复到默认的位置。
     * 一般来说，是 EditText，或者一起被顶起的 Layout
     *
     * @param target   位置随键盘变化的view
     * @param listener 键盘变化回调
     */
    @Override
    public void watch(@NonNull View target, KeyboardChangedListener listener) {
        // 移除之前的监听
        if (reference != null && layoutListener != null) {
            View view = getTargetView();

            if (view != null && view != target && view.getViewTreeObserver() != null) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
            }
        }

        if (target.getViewTreeObserver() != null) {
            int[] location = new int[2];
            target.getLocationInWindow(location);
            layoutListener = new InnerOnGlobalLayoutListener(location[1]);
            reference = new WeakReference<>(target);
            target.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
            keyboardListenerRef = new WeakReference<>(listener);
        }
    }

    /**
     * 移除之前添加的键盘变化监听
     */
    @Override
    public void unwatch() {
        View view = getTargetView();

        if (view != null && view.getViewTreeObserver() != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        }
    }

    @Override
    public void pause() {
        isPause = true;
    }

    @Override
    public void resume() {
        isPause = false;
    }

    @Nullable
    private View getTargetView() {
        return reference != null ? reference.get() : null;
    }

    @Nullable
    private KeyboardChangedListener getKeyboardListener() {
        return keyboardListenerRef != null ? keyboardListenerRef.get() : null;
    }

    private class InnerOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private int initY = Integer.MIN_VALUE;
        private android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        private CheckTask checkTask = new CheckTask();
        private final int CHECK_DELAY = 32;

        InnerOnGlobalLayoutListener(int initY) {
            this.initY = initY;
            positionY = initY;
        }

        @Override
        public void onGlobalLayout() {
            if (!isPause) {
                View target = getTargetView();

                if (target != null) {
                    int[] location = new int[2];
                    target.getLocationInWindow(location);
                    int currentY = location[1];
                    int dy = currentY - positionY;

                    if (dy != 0) {
                        checkTask.setCurrentState(target, currentY, dy);
                        handler.removeCallbacks(checkTask);
                        handler.postDelayed(checkTask, CHECK_DELAY);
                    }
                }
            }
        }
    }

    private class CheckTask implements Runnable {
        private int[] location = new int[2];
        private int keyboardHeight = 300;
        private int currentY;
        private int dy;
        private View target;

        void setCurrentState(View target, int currentY, int dy) {
            this.currentY = currentY;
            this.dy = dy;
            this.target = target;
        }

        @Override
        public void run() {
            target.getLocationInWindow(location);

            if (location[1] == currentY && Math.abs(dy) >= keyboardHeight) {
                positionY = currentY;

                if (dy > 0) {
                    onKeyboardHide();
                } else if (dy < 0) {
                    keyboardHeight = Math.abs(dy);
                    onKeyboardOpen();
                }
            }
        }
    }

    private void onKeyboardOpen() {
        KeyboardChangedListener listener = getKeyboardListener();

        if (listener != null) {
            listener.onKeyboardOpen();
        }
    }

    private void onKeyboardHide() {
        KeyboardChangedListener listener = getKeyboardListener();

        if (listener != null) {
            listener.onKeyboardHide();
        }
    }
}
