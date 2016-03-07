package com.instil.ibeattest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.instil.ibeattest.MainActivity;
import com.instil.ibeattest.R;
import com.instil.ibeattest.ibeJNI;
//import com.instil.ibeattest.MainActivity.PostClass;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private EditText textField;
	private TextView textview;
	private String ciphertxt;
	private ProgressDialog progress;
	private String node = "node 1";
	private String nodesid = "29358e9e83eab77500f02d74d62e7cb4";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textField = (EditText) findViewById(R.id.editText);
		textview = (TextView) findViewById(R.id.textView2);
		
		copyAssets();
		
		String plaintxt = readFromFile("plain.txt");
		plaintxt = plaintxt.replaceAll("\\n","");
		textField.setText(plaintxt);
	
        Button button5 = (Button) findViewById(R.id.Button01);
        button5.setOnClickListener((OnClickListener) this);

        Button button6 = (Button) findViewById(R.id.Button02);
        button6.setOnClickListener((OnClickListener) this);

	}
	
	static {
		System.loadLibrary("gmp");
		System.loadLibrary("ssl_1_0_0");
		System.loadLibrary("crypto_1_0_0");
        System.loadLibrary("ibe");
    }

	    //Copy file from assets folder to /sdcard/Android/data/com.example.ibe/files/
	    private void copyAssets() {
	        AssetManager assetManager = getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("");
	        } catch (IOException e) {
	            Log.e("tag", "Failed to get asset file list.", e);
	        }
	        if (files != null) for (String filename : files) {
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open(filename);
	              File outFile = new File(getExternalFilesDir(null), filename);
	              out = new FileOutputStream(outFile);
	              copyFile(in, out);
	            } catch(IOException e) {
	                Log.e("tag", "Failed to copy asset file: " + filename, e);
	            }     
	            finally {
	                if (in != null) {
	                    try {
	                        in.close();
	                    } catch (IOException e) {
	                        // NOOP
	                    }
	                }
	                if (out != null) {
	                    try {
	                        out.close();
	                    } catch (IOException e) {
	                        // NOOP
	                    }
	                }
	            }  
	        }
	    }
	    private void copyFile(InputStream in, OutputStream out) throws IOException {
	        byte[] buffer = new byte[1024];
	        int read;
	        while((read = in.read(buffer)) != -1){
	          out.write(buffer, 0, read);
	        }
	    }

	    private String readFromFile(String mytext) {

	        //Find the directory for the SD Card using the API
	      File sdcard = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.instil.ibeattest/files/");

	      //Get the text file
	      File file = new File(sdcard,mytext);

	      //Read text from file
	      StringBuilder text = new StringBuilder();

	      try {
	          BufferedReader br = new BufferedReader(new FileReader(file));
	          String line;
	          while ((line = br.readLine()) != null) {
	        	  text.append(line);
	        	  text.append("\n");
	          }
	          br.close();
	      }
	      catch (IOException e) {
	    	  e.printStackTrace();
	      }

	        return text.toString();
	    }
	    
	    private void writeToFile(String mytext, String theText) {
	    	
	    	try {
				FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.instil.ibeattest/files/"+mytext);
				writer.write(theText);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    	    
	    public void onClick(View v) {


	            if (v.getId() == R.id.Button01) {
	            	String myPlainText = "$plaintxt=\"" + textField.getText().toString() + "\";";
	            	StringBuilder myText = new StringBuilder();
	            	myText.append("$id=\""+getID()+"\";");
	            	myText.append("$sid=\""+getsID()+"\";");
	            	myText.append(myPlainText);
	            	writeToFile("plain.txt",myText.toString());
	            	ibeJNI.myencrypt();
	        		ciphertxt = readFromFile("cipher.txt");
	        		textview.setText(ciphertxt);

	        }
	            else if (v.getId() == R.id.Button02) {
		            
	            	ciphertxt = readFromFile("cipher.txt");
	            	
	            	//send http post
	            	new PostClass(this).execute();

	        }
	    }
	    

	    
	    private class PostClass extends AsyncTask<String, Void, Void> {
	    	 
	        private final Context context;
	     
	        public PostClass(Context c){
	            this.context = c;
	        }
	     
	        protected void onPreExecute(){
	            progress= new ProgressDialog(this.context);
	            progress.setMessage("Sending");
	            progress.show();
	        }
	     
	        @Override
	        protected Void doInBackground(String... params) {
	            try {
	     
	                final TextView outputView = (TextView) findViewById(R.id.textView2);
	                URL url = new URL("http://10.8.132.99/ibe/heart.php");
	                
	                ciphertxt = readFromFile("cipher.txt");
	                
	                String encodedurl = URLEncoder.encode(ciphertxt,"UTF-8");
	                
	                	
	                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	                String urlParameters = "beat=" + encodedurl;
	                connection.setRequestMethod("POST");
	                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
	                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
	                connection.setDoOutput(true);
	                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
	                dStream.writeBytes(urlParameters);
	                dStream.flush();
	                dStream.close();
	                int responseCode = connection.getResponseCode();
	     
	                final StringBuilder output = new StringBuilder("Request URL " + url);
	                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
	                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
	                output.append(System.getProperty("line.separator")  + "Type " + "POST");
	                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String line = "";
	                StringBuilder responseOutput = new StringBuilder();
	                System.out.println("output===============" + br);
	                while((line = br.readLine()) != null ) {
	                    responseOutput.append(line);
	                }
	                br.close();
	     
	                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
	     
	                MainActivity.this.runOnUiThread(new Runnable() {
	     
	                    @Override
	                    public void run() {
	                        outputView.setText(output);
	                        progress.dismiss();
	                    }
	                });
	     
	            } catch (MalformedURLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            return null;
	        }
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			sendMessage(null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void sendMessage(View view) {
	    Intent intent = new Intent(this, SettingsActivity.class);
	    intent.putExtra("id", node);
	    intent.putExtra("sid", nodesid);
	    startActivityForResult(intent,1);
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  if(resultCode==RESULT_OK && requestCode==1){
	   String msg = data.getStringExtra("returnedid");
	   String msg2 = data.getStringExtra("returnedsid");
	   node = msg;
	   nodesid = msg2;
	  }
	 }
	
	public String getID() {return node;}
	public void setID(String s) { node = s;}
	public String getsID() {return nodesid;}
	public void setsID(String s) { nodesid = s;}
	
}
