package com.mindblots.ohipswipe;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;

import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ohipActivity extends Activity implements View.OnClickListener/*, View.OnKeyListener*/ {

    EditText _ohipText;
    private List<Button> buttons;
    TextView tokenNum;
    TextView tokenInfo;
    String waittimetxt;
    TextView waittime;
    TextView errortxt;
    Timer timer;
    ClearTask myTask;
    private static final int[] BUTTON_IDS = {
            R.id.one,
            R.id.two,
            R.id.three,
            R.id.four,
            R.id.five,
            R.id.six,
            R.id.seven,
            R.id.eight,
            R.id.nine,
            R.id.zero,
            R.id.goBtn,
            R.id.clearBtn,
            R.id.deleteBtn
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohip);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _ohipText=(EditText)findViewById(R.id.ohipText);
        _ohipText.setInputType(InputType.TYPE_NULL);
        //_ohipText.setOnKeyListener(this);
        tokenNum=(TextView)findViewById(R.id.token);
        buttons=new ArrayList<Button>();
        for(int id : BUTTON_IDS) {
            Button button = (Button)findViewById(id);
            button.setOnClickListener(this); // maybe
            buttons.add(button);
        }
        waittime=(TextView)findViewById(R.id.waiting);
        waittimetxt=waittime.getText().toString();
        errortxt=(TextView)findViewById(R.id.errorInfo);
        tokenInfo=(TextView)findViewById(R.id.infoToken);
    }

    private void AddText(String newdigit)
    {
        String ot=_ohipText.getText().toString();
        //ot=ot.replace(" ","");
        if(ot.length()<10)
        _ohipText.setText(ot+newdigit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ohip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void Error(boolean valid)
    {
        if(!valid) {
            errortxt.setTextColor(Color.RED);
            errortxt.setText("Health Card is not valid");
        }else{
            errortxt.setTextColor(Color.GREEN);
            errortxt.setText("Valid Health Card");
        }
    }
    private void ReadInput()
    {
        HealthCard hc=new HealthCard();
        String ot=_ohipText.getText().toString();
        //ot=ot.replace(" ","");
       // if(ot.length()==10) {
            //String otn= ot.substring(0,4)+" "+ot.substring(4,7)+" "+ot.substring(7,10);
            //_ohipText.setText(otn);
       // }
        //if(ot.length()>9)
        {

            try {

                Boolean valid=true;

                if(ot.length()==10)
                {
                    valid=hc.Validation(ot);

                }else {
                    hc.Read(ot);
                    valid=hc.valid;
                }

                Error(valid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new HttpAsyncTask().execute("5558888888");
            tokenInfo.setText("Your token number is");
            timer = new Timer();
            myTask = new ClearTask();
            timer.schedule(myTask, 9000);
        }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.goBtn:
           {
                ReadInput();
           }
            break;
           case R.id.clearBtn:
           {
                _ohipText.setText("");
           }
           break;
           case R.id.deleteBtn:
           {
               String ot=_ohipText.getText().toString();
               if (ot.length()>0)
               {
                   _ohipText.setText(ot.substring(0,ot.length()-1));
               }
           }
           break;
           case R.id.one:
           case R.id.two:
           case R.id.three:
           case R.id.four:
           case R.id.five:
           case R.id.six:
           case R.id.seven:
           case R.id.eight:
           case R.id.nine:
           case R.id.zero:
           {
               Button b = (Button)v;
               String buttonText = b.getText().toString();
               AddText(buttonText);
           }
           break;


       }
    }
    private class HttpAsyncTask extends AsyncTask<String, Integer, Double>{

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), params[0], Toast.LENGTH_LONG).show();
            Log.i("OHIP Entered",params[0]);
            postData(params[0]);
            Log.i("OHIP SENT",params[0]);
            return null;
        }

        protected void onPostExecute(Double result){

            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
        }


        public void postData(String valueIWantToSend) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://dymo.herokuapp.com/tokens.json");
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("healthcard", valueIWantToSend));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    Log.i("OHIP Response", "Key : " + header.getName()
                            + " ,Value : " + header.getValue());
                }
                Log.i("OHIP Response",response.toString());
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

    }
    class ClearTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable(){

                @Override
                public void run() {

                    errortxt.setText("");
                    _ohipText.setText("");
                    tokenInfo.setText("Last Token Printed");
                }});
        }

    }
}
