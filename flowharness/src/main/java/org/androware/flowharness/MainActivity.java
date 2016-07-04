package org.androware.flowharness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.Constants;
import org.androware.flow.JsonFlowEngine;



public class MainActivity extends AppCompatActivity {

    public final static String TAG = "main";

    public void l(String s) {
        FilterLog.inst().log(TAG, s);
    }

    public void l(String tag, String s) {
        FilterLog.inst().log(tag, s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.androware.flow.R.layout.activity_main);

        ResourceUtils.R = org.androware.flow.R.class;

        FilterLog.inst().activateTag(TAG);
        FilterLog.inst().activateTag(Constants.TAG);
        ObjectReaderFactory.getInstance(this);   // intialize activity for read factory
        l("id: " + ResourceUtils.getResId("id", "next"));
        //JsonFlowEngine.inst(this).startFlow("model_test");
        JsonFlowEngine.inst(this).startFlow("bind_test_flow");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }
}
