package nicolagigante.garage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nicolagigante.garage.R;

public class Main extends Activity implements View.OnClickListener {

    private Button btn;

    public String getWifiName(Context context) {
        String ssid = "none";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED) {
            ssid = wifiInfo.getSSID();
        }
        return ssid;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            ImageView img= (ImageView) findViewById(R.id.imageViewWifi);
            img.setImageResource(R.drawable.materialwifi);
            TextView txt = (TextView)findViewById(R.id.textView4);
            txt.setText(R.string.wifisatisfied);
        }
        else {
            NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (m3G.isConnected()){
                ImageView img = (ImageView) findViewById(R.id.imageViewWifi);
                img.setImageResource(R.drawable.material3g);
                TextView txt = (TextView)findViewById(R.id.textView4);
                txt.setText(R.string.mobilewarn);
            }
            else {
                ImageView img = (ImageView) findViewById(R.id.imageViewWifi);
                img.setImageResource(R.drawable.materialwifidisconnected);
                TextView txt = (TextView)findViewById(R.id.textView4);
                txt.setText(R.string.wifineed);
                btn.setEnabled(false);
            }
        }

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);

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
        return true;
    }

    @Override
    public void onClick(View view) {
        final Animation zoom = AnimationUtils.loadAnimation(this, R.anim.scale);
        btn.startAnimation(zoom);
        new MyAsyncTask().execute("hey");
    }

    public void Hamburger(View view){
        Intent i = new Intent(Main.this, Choose.class);
        startActivity(i);
    }
    public void Notconnected(View view){
        Intent i = new Intent(Main.this, Notconnected.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result) {
            Toast.makeText(getApplicationContext(), R.string.cmdsent,
                    Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String valueIWantToSend) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://192.168.0.110/LetMeIn.html");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("message",
                        ""));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

    }
}
