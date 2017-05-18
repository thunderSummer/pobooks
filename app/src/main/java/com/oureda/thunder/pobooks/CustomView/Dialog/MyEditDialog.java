package com.oureda.thunder.pobooks.CustomView.Dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.MyApplication;

/**
 * Created by thunder on 17-5-17.
 */

public class MyEditDialog extends DialogFragment {
    private ButtonListener buttonListener;
    private EditText contentTV;
    private TextView negativeBTN;
    private TextView positiveBTN;
    private TextView titleTV;
    private View view;
    public void initAll(final String content, String title, String positive, String negative, final ButtonListener buttonListener){
        view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.fragment_edit_dialog,null);
        this.buttonListener=buttonListener;
        contentTV= (EditText) view.findViewById(R.id.content_edit_dialog);
        negativeBTN= (TextView) view.findViewById(R.id.negative_edit_dialog);
        positiveBTN = (TextView) view.findViewById(R.id.positive_edit_dialog);
        negativeBTN = (TextView) view.findViewById(R.id.negative_edit_dialog);
        titleTV= (TextView) view.findViewById(R.id.title_edit_dialog );
        titleTV.setText(title);
        contentTV.setText(content);
        negativeBTN.setText(negative);
        positiveBTN.setText(positive);
        negativeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.negativeListener();
            }
        });
        positiveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.positiveListener(contentTV.getText().toString());
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(1);
        return view;
    }

    public interface  ButtonListener{
        void negativeListener();

        void positiveListener(String paramString);
    }
}
