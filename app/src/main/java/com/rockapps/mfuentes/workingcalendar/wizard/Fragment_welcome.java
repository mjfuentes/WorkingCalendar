package com.rockapps.mfuentes.workingcalendar.wizard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rockapps.mfuentes.workingcalendar.R;
import com.rockapps.mfuentes.workingcalendar.WizardActivity;

import java.security.InvalidParameterException;

public class Fragment_welcome extends Fragment {

    private WizardActivity mListener;

    public static Fragment_welcome newInstance() {
        Fragment_welcome fragment = new Fragment_welcome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_welcome() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final RelativeLayout view = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome, container);
        Button next = (Button) view.findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = settings.edit();
                try {
                    String username = (((EditText) view.findViewById(R.id.username_enter)).getText().toString());
                    if (!username.equals(null) && !username.equals("")){
                        editor.putString("username", username);
                        mListener.next();
                    }
                    else {
                        throw new InvalidParameterException("Nombre invalido");
                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "Por favor ingrese su nombre antes de continuar.", Toast.LENGTH_LONG);
                }
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
