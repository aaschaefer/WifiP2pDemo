package csc495.wifip2pdemo;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectedActivity extends ActionBarActivity {

    ServerSocket server;
    Socket client;
    String host;
    int port = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        WifiP2pInfo info = bundle.getParcelable("Info");

        TextView tv = (TextView) findViewById(R.id.textViewPeerName);

        if(info.isGroupOwner) {
            try {
                server = new ServerSocket(port);
                client = server.accept();

                InputStream is = client.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                tv.setText(isr.read());
            }catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            client = new Socket();
            //host = info.groupOwnerAddress.getHostAddress();
            try {
                client.bind(null);
                client.connect((new InetSocketAddress(info.groupOwnerAddress, port)), 500);

                OutputStream os = client.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                osw.write("Test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connected, menu);
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
}
