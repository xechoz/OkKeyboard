package com.xechoz.keyboardlistener;

import android.view.View;

/**
 * Created by xechoz on 2018/2/8.
 */

public interface KeyboardChangedResolver {
    /**
     * 这个view，在键盘出来的时候，会被顶起来；键盘收起，就恢复到默认的位置。
     * 一般来说，是 EditText，或者一起被顶起的 Layout
     *
     * @param target   位置随键盘变化的view
     * @param listener 键盘变化回调
     */
    void watch(View target, KeyboardChangedListener listener);

    /**
     * 移除之前添加的键盘变化监听
     */
    void unwatch();

    void pause();

    void resume();
}
