package com.oureda.thunder.pobooks.CustomView.Dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.MyApplication;

/**
 * Created by thunder on 17-5-1.
 */

public class MyDialog extends DialogFragment {
    private TextView titleDialog;
    private TextView contentDialog;
    private TextView positiveDialog;
    private TextView negativeDialog;
    private View view;
    private ButtonListener buttonListener;


    public void initAll(String title, String content, String positive, String negative, final ButtonListener buttonListener) {
        view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.fragment_dialog, null);
        this.buttonListener=buttonListener;
        titleDialog= (TextView) view.findViewById(R.id.title_dialog);
        contentDialog= (TextView) view.findViewById(R.id.content_dialog);
        positiveDialog= (TextView) view.findViewById(R.id.positive_dialog);
        negativeDialog= (TextView) view.findViewById(R.id.negative_dialog);
        titleDialog.setText(title);
        contentDialog.setText(content);
        positiveDialog.setText(positive);
        negativeDialog.setText(negative);
        positiveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.positiveListener();
            }
        });
        negativeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.negativeListener();
            }
        });



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(1);
        return view;
    }

    public  interface ButtonListener {
         void negativeListener();

         void positiveListener();
    }
}
