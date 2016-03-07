package com.instil.ibeattest;

import com.instil.ibeattest.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

public class SettingsActivity extends Activity implements OnClickListener{
	
	private EditText textField1;
	private EditText textField2;
	private String id;
	private String sid;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.content_display_message);

	   intent = getIntent();
	   id = intent.getStringExtra("id");
	   sid = intent.getStringExtra("sid");
	   
	   textField1 = (EditText) findViewById(R.id.editText1);
	   textField1.setText(id);
	   
	   textField2 = (EditText) findViewById(R.id.editText2);
	   textField2.setText(sid);
	   
	   Button button = (Button) findViewById(R.id.button1);
       button.setOnClickListener((OnClickListener) this);
	}
	
	public void onClick(View v) {
		
        if (v.getId() == R.id.button1) {
        	
        	MainActivity b = new MainActivity();
        	id = textField1.getText().toString();
        	sid = textField2.getText().toString();
        	
        	intent.putExtra("returnedid", id);
        	intent.putExtra("returnedsid", sid);
        	setResult(RESULT_OK, intent);
        	finish();
        }
	}
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
 
}