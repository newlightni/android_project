package com.bs.teachassistant.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bs.teachassistant.R;


/**
 * 无标题对话框
 * 带textview信息提示
 */
public class MessageDialog extends Dialog {

    private Button positiveButton;
    private Button negativeButton;
    public static MessageDialog messageDialog = null;

    private Context context;

    public MessageDialog(Context context) {
        super(context, R.style.dialog);
        this.context=context.getApplicationContext();
    }

    public static MessageDialog getInstence(Context context) {
        if (null == messageDialog)
            messageDialog = new MessageDialog(context);
        return messageDialog;
    }

    /**
     * @param message     对话框显示的信息
     * @param txtNegative 忽略按钮的提示内容
     * @param txtPositive 确认操作的按钮内容
     */
    public void setCustomDialog(String title,String message, String txtNegative, String txtPositive) {
        View mView;
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_message, null);
        TextView txtMessage = (TextView) mView.findViewById(R.id.txt_message);
        TextView txtTitle = (TextView) mView.findViewById(R.id.txt_title);

        positiveButton = (Button) mView.findViewById(R.id.btn_loginExit);
        negativeButton = (Button) mView.findViewById(R.id.btn_loginCancle);
        View view = mView.findViewById(R.id.view_dervier);
        negativeButton.setText(String.format("%s", txtNegative));
        positiveButton.setText(String.format("%s", txtPositive));
        if (TextUtils.isEmpty(txtNegative)) {
            negativeButton.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

        } else {
            negativeButton.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(txtPositive)) {
            positiveButton.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {
            positiveButton.setVisibility(View.VISIBLE);
        }
        txtMessage.setText(String.format("%s", message));
        if(TextUtils.isEmpty(title)){
            txtTitle.setVisibility(View.GONE);
        }else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }
        super.setContentView(mView);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     *
     * @param listener 监听器
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener 监听器
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK;
    }
}
