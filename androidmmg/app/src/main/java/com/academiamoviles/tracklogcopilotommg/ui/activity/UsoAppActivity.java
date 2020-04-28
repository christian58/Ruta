package com.academiamoviles.tracklogcopilotommg.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.academiamoviles.tracklogcopilotommg.R;

public class UsoAppActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uso_activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
