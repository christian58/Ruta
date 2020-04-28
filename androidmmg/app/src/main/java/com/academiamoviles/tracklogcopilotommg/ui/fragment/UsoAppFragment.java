package com.academiamoviles.tracklogcopilotommg.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academiamoviles.tracklogcopilotommg.R;
import com.academiamoviles.tracklogcopilotommg.ui.UsoApp;
import com.academiamoviles.tracklogcopilotommg.ui.adapter.UsoAppAdapter;

/**
 * Created by Android on 07/02/2017.
 */

public class UsoAppFragment extends BaseFragment {

    RecyclerView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uso_fragment, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UsoApp objUsoApp = UsoApp.readIS(getActivity());
        UsoAppAdapter adapter = new UsoAppAdapter(objUsoApp.fecha);
        listView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
