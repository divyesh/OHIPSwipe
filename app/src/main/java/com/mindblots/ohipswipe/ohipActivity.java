package com.mindblots.ohipswipe;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;

import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
>>>>>>> FETCH_HEAD
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
   /* @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER))
        {
            ReadInput();
        }
        // Returning false allows other listeners to react to the press.
        return false;
    }*/
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
        if(ot.length()==10) {
            String otn= ot.substring(0,4)+" "+ot.substring(4,7)+" "+ot.substring(7,10);
            _ohipText.setText(otn);
        }
        else if(ot.length()>9) {

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
            // new LongRunningGetIO().execute();
            tokenInfo.setText("Your token number is");
            timer = new Timer();
            myTask = new ClearTask();
            timer.schedule(myTask, 9000);
        }
    }
    public void postData(String hci) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://dymo.herokuapp.com/tokens");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("healthcard", hci));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            InputStream inputStream = response.getEntity().getContent();
            Toast.makeText(getBaseContext(),inputStream.toString(),Toast.LENGTH_SHORT);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws Exception {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);
                if (n>0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(Void... params) {

            // Send Ohip text
            String ot=_ohipText.getText().toString();

            HttpClient httpClient =new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost("http://localhost:3000/NewPatient/"+ot);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }

        protected void onPostExecute(String results) {
            if (results!=null) {
                tokenInfo.setText("Your token number is");
                String rslt[]=results.split("|");
                tokenNum.setText(rslt[0]);
                waittime.setText(waittimetxt+" "+ rslt[1]);
            }
        }
    }
}
