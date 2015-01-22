package com.rockapps.mfuentes.workingcalendar.wizard;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rockapps.mfuentes.workingcalendar.R;
import com.rockapps.mfuentes.workingcalendar.WizardActivity;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class Fragment_enter_date extends Fragment  {
    private static String DATE_FRAGMENT = "date";
    private static String START_DATE = "start_date";
    private WizardActivity mListener;
    final static String USERNAME = "username";
    public Fragment_enter_date() {
    }

    public static Fragment_enter_date newInstance() {
        Fragment_enter_date fragment = new Fragment_enter_date();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RelativeLayout view = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_enter_date, container,false);
        TextView textView = (TextView) view.findViewById(R.id.excelent_text);
        SharedPreferences settings = getActivity().getSharedPreferences("PREFS", 0);
        String name = settings.getString(USERNAME,"");
        if (!name.equals("")) {
            textView.setText("Genial, " + name + "!");
        }
        Button next = (Button) view.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatePicker datePicker = (DatePicker)view.findViewById(R.id.date_picker);
                    String date = datePicker.getDayOfMonth() + "-" + datePicker.getMonth()+1 + "-" + datePicker.getYear();
                    if (date != "") {
                        SharedPreferences settings = getActivity().getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(START_DATE, date);
                        editor.commit();
                        mListener.next(DATE_FRAGMENT);
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
                mListener.last(DATE_FRAGMENT);
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
