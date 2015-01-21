package com.rockapps.mfuentes.workingcalendar.wizard;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Fragment_done extends Fragment {
    private static final String DONE_FRAGMENT = "done";
    private WizardActivity mListener;

    public static Fragment_done newInstance() {
        Fragment_done fragment = new Fragment_done();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_done() {
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
                mListener.next(DONE_FRAGMENT);
            }
        });
        Button last = (Button) view.findViewById(R.id.last_button);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.last(DONE_FRAGMENT);
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
