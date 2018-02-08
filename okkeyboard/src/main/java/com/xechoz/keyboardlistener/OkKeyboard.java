package com.xechoz.keyboardlistener;

import android.view.View;

/**
 * Created by xechoz on 2018/2/8.
 */

public class OkKeyboard {
    // TODO: 2018/2/9 builder pattern
    public static KeyboardChangedResolver watch(View target, KeyboardChangedListener listener) {
        KeyboardChangedResolver resolver = new KeyboardResolver();
        resolver.watch(target, listener);
        return resolver;
    }
}
