package com.rockapps.mfuentes.workingcalendar.wizard;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rockapps.mfuentes.workingcalendar.R;
import com.rockapps.mfuentes.workingcalendar.WizardActivity;

import java.security.InvalidParameterException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_enter_period extends Fragment {
    static final String PERIOD_FRAGMENT = "period";
    static final String WORK_DAYS = "work_days";
    static final String REST_DAYS = "rest_days";
    private WizardActivity mListener;
    final static String USERNAME = "username";
    public Fragment_enter_period() {
    }

    public static Fragment_enter_period newInstance() {
        Fragment_enter_period fragment = new Fragment_enter_period();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RelativeLayout view = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_enter_date, container);
        TextView textView = (TextView) view.findViewById(R.id.excelent_text);
        textView.setText("Hola " + getArguments().getString(USERNAME,"") + ", mucho gusto!");
        Button next = (Button) view.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int workDays = (Integer.valueOf(((EditText) view.findViewById(R.id.workdays_enter)).getText().toString()));
                    int restDays = (Integer.valueOf(((EditText) view.findViewById(R.id.restdays_enter)).getText().toString()));
                    if (workDays != 0 && restDays != 0) {
                        SharedPreferences settings = getActivity().getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(WORK_DAYS, workDays);
                        editor.putInt(REST_DAYS, restDays);
                        mListener.next(PERIOD_FRAGMENT);
                    }
                    else {
                        throw new InvalidParameterException("Datos erroneos");
                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Por favor ingrese ambos datos.", Toast.LENGTH_LONG);
                }
            }
        });

        Button last = (Button) view.findViewById(R.id.last_button);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.last(PERIOD_FRAGMENT);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WizardActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement WizardActivity");
        }
    }



}
