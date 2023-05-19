package com.cursoandroid.navigationdrawer.ui.principal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.cursoandroid.navigationdrawer.R;

public class PrincipalFragment extends Fragment {

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_principal, container, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.principalLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        Button buttonBar = view.findViewById(R.id.buttonBar);
        Button buttonPie = view.findViewById(R.id.buttonPie);
        Button buttonAnotacoes = view.findViewById(R.id.buttonAnotacoes);


        buttonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity act = getActivity();
                if (act != null) {
                    startActivity(new Intent(act, BarChartClass.class));
                }
            }
        });

        buttonPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity act = getActivity();

                if (act != null) {
                    startActivity(new Intent(act, PieChartClass.class));
                }
            }
        });

        buttonAnotacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity act = getActivity();

                if (act != null) {
                    startActivity(new Intent(act, AnotacoesActivity.class));
                }
            }
        });

        return view;
    }
}
