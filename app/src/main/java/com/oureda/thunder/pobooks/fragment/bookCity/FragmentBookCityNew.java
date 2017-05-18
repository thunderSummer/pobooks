package com.oureda.thunder.pobooks.fragment.bookCity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oureda.thunder.pobooks.R;

/**
 * Created by thunder on 17-5-15.
 */

public class FragmentBookCityNew extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_book_city_new,null);
        return view;
    }
}
