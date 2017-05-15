package com.oureda.thunder.pobooks.fragment.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oureda.thunder.pobooks.R;

/**
 * Created by thunder on 17-5-14.
 */

public class FragmentLocalHand extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_local_hand,null);
        return view;
    }
}
