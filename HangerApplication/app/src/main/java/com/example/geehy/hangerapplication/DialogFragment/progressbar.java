
package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import com.example.geehy.hangerapplication.R;

public class progressbar extends DialogFragment {
    private Dialog dialog;
    private View view;
    private ProgressBar circlebar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//배경 투명하게
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.progress, null);
        init();
        dialog.setContentView(view);
        return dialog;
    }

    private void init() {

        circlebar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }





}