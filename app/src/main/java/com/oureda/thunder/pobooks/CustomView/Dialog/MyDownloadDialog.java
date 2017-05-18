package com.oureda.thunder.pobooks.CustomView.Dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oureda.thunder.pobooks.CustomView.RoundButton;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by thunder on 17-5-15.
 */

public class MyDownloadDialog extends DialogFragment {
    @BindView(R.id.only_50_download_dialog)
    RoundButton only50DownloadDialog;
    @BindView(R.id.only_back_download_dialog)
    RoundButton onlyBackDownloadDialog;
    @BindView(R.id.all_download_dialog)
    RoundButton allDownloadDialog;
    Unbinder unbinder;
    //    private MyButton allIV;
    private ButtonListener buttonListener;
    //    private MyButton only50IV;
//    private MyButton onlyBackIV;
    private View view;

    public void initAll(final ButtonListener buttonListener) {
        this.view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.fragment_download_dialog, null);
        this.buttonListener = buttonListener;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        getDialog().requestWindowFeature(1);
        unbinder = ButterKnife.bind(this, this.view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.only_50_download_dialog, R.id.only_back_download_dialog, R.id.all_download_dialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.only_50_download_dialog:
                buttonListener.only50Listener();
                break;
            case R.id.only_back_download_dialog:
                buttonListener.onlyBackListener();
                break;
            case R.id.all_download_dialog:
                buttonListener.allListener();
                break;
        }
    }

    public interface ButtonListener {
        void allListener();

        void only50Listener();

        void onlyBackListener();
    }
}
