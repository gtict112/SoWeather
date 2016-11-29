package com.example.administrator.soweather.com.example.administrator.soweather.general;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private boolean cancelable;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
            cancelable = true;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean flag) {
            cancelable = flag;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_custom, null);
            dialog.setCancelable(cancelable);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (!"".equals(title) && title != null) {
                ((TextView) layout.findViewById(R.id.title)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.title)).setText(title);

            }
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);
                ((Button) layout.findViewById(R.id.negative_button)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                        dialog.dismiss();
                    }
                });
            } else {
                layout.findViewById(R.id.negative_button).setVisibility(View.GONE);
            }
            if (!"".equals(message) && message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
                ((LinearLayout) layout.findViewById(R.id.content)).setVisibility(View.VISIBLE);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(contentView,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }

        public CustomDialog show() {
            CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
