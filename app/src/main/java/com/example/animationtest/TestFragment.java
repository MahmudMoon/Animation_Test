package com.example.animationtest;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class TestFragment extends DialogFragment {
    Button btnClose;
    EditText etData;
    private testInterface Test;
    public TestFragment(testInterface TestInterface){
        this.Test = TestInterface;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_layout, null);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        btnClose = view.findViewById(R.id.btnClose);
        etData = view.findViewById(R.id.etData);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final View decorView = getDialog()
                .getWindow()
                .getDecorView();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        decorView.animate().translationY(-100)
                .setStartDelay(100)
                .setDuration(300)
                .start();


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.showResultData(etData.getText().toString());
               dismiss();
            }
        });
    }

}
