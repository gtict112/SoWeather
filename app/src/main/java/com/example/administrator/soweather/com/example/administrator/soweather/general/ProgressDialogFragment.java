package com.example.administrator.soweather.com.example.administrator.soweather.general;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/10/10.
 */

public class ProgressDialogFragment  extends DialogFragment {
    private final Object DIALOG_LOCK = new Object();
    private boolean isShown;
    private TextView messageTextView;
    private String message;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.DialogFragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.DialogFragment#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.DialogFragment#show(android.support.v4.app.
     * FragmentManager, java.lang.String)
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        synchronized (DIALOG_LOCK) {
            if (isShown) {
                return;
            } else {
                try {
                    if (this.isAdded()) {
                        this.dismiss();
                    }
                    super.show(manager, null);
                    isShown = true;
                } catch (Exception e) {
                    isShown = false;
                }
            }
        }
    }

    public ProgressDialogFragment() {
        this.setCancelable(false);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ProgressDialogFragment.this.getActivity());
        LayoutInflater inflater = (LayoutInflater) ProgressDialogFragment.this
                .getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.diallog_progress, null);

        messageTextView = (TextView) view.findViewById(R.id.message);
        if (message != null)
            messageTextView.setText(message);
        builder.setView(view);
        return builder.show();
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(final String message) {
        this.message = message;
        if (messageTextView != null)
            messageTextView.post(new Runnable() {
                @Override
                public void run() {
                    if (messageTextView != null) {
                        messageTextView.setText(message);
                    }
                }
            });
    }

    @Override
    public void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
    }

    public void dismiss() {
        synchronized (DIALOG_LOCK) {
            try {
                super.dismissAllowingStateLoss();
                isShown = false;
            } catch (Exception e) {
                isShown = true;
            }
        }
    }



}
