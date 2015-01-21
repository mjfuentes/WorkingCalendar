package com.rockapps.mfuentes.workingcalendar.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rockapps.mfuentes.workingcalendar.R;
import com.rockapps.mfuentes.workingcalendar.WizardActivity;
import com.rockapps.mfuentes.workingcalendar.wizard.Fragment_done;
import com.rockapps.mfuentes.workingcalendar.wizard.Fragment_enter_date;
import com.rockapps.mfuentes.workingcalendar.wizard.Fragment_enter_period;
import com.rockapps.mfuentes.workingcalendar.wizard.Fragment_welcome;

public class StartupWizardActivity extends ActionBarActivity implements WizardActivity{
    private static final String DATE_FRAGMENT = "date";
    private static final String DONE_FRAGMENT = "done";
    private static final String PERIOD_FRAGMENT = "period";
    private static final String WELCOME_FRAGMENT = "welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_wizard);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, Fragment_welcome.newInstance())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_startup_wizard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void next(String fragment) {
        Fragment newFragment = null;
        switch (fragment) {
            case WELCOME_FRAGMENT:
                newFragment = Fragment_enter_period.newInstance();
                break;
            case PERIOD_FRAGMENT:
                newFragment = Fragment_enter_date.newInstance();
                break;
            case DATE_FRAGMENT:
                newFragment = Fragment_done.newInstance();
                break;
            case DONE_FRAGMENT:
                Intent intent = new Intent(StartupWizardActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, newFragment)
                .commit();
    }

    @Override
    public void last(String fragment) {
        Fragment newFragment = null;
        switch (fragment) {
            case PERIOD_FRAGMENT:
                newFragment = Fragment_welcome.newInstance();
                break;
            case DATE_FRAGMENT:
                newFragment = Fragment_enter_period.newInstance();
                break;
            case DONE_FRAGMENT:
                newFragment = Fragment_enter_date.newInstance();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, newFragment)
                .commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            return rootView;
        }
    }
}
