package com.cattsoft.wow.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cattsoft.wow.R;


/**
 * Created by wanghao on 2016/5/19.
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
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPositiveButton(OnClickListener listener) {

            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.addContentView(layout, params);
//
//                if (positiveButtonClickListener != null) {
//                    ((Button) layout.findViewById(R.id.ib_run_in)).setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
//                        }
//                    });
//
//                }
//
//                if (negativeButtonClickListener != null) {
//                    ((Button) layout.findViewById(R.id.ib_run_out)).setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
//                        }
//                    });
//                }

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
