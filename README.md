# OkKeyboard
监听键盘弹起，隐藏的工具库

# How to Use

```java
EditText thePushUpView = findViewById(R.id.my_edit_text);

OkKeyboard.watch(thePushUpView, new KeyboardChangedListener() {
    @Override
    public void onKeyboardOpen() {
        Log.d(TAG, "onKeyboardOpen");
    }

    @Override
    public void onKeyboardHide() {
        Log.d(TAG, "onKeyboardHide");
    }
});
```