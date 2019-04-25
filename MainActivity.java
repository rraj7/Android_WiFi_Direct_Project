package com.example.wifidirect;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {

    EditText e1,e2;
    public void button_click(View v)
    {
        e1=(EditText)findViewById(R.id.ip);
        e2=(EditText) findViewById(R.id.msg);
        backgroundTask b = new backgroundTask();
        Log.i("Info",e1.getText().toString() +e2.getText().toString());
        b.execute(e1.getText().toString(),e2.getText().toString());


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread myThread = new Thread(new MyServer());
        myThread.start();


    }
    class MyServer implements Runnable
    {
        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        //BufferedReader bufferedReader;
        String message;
        Handler handler = new Handler();



        @Override
        public void run() {

            try
            {
                ss = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Connection Established ",Toast.LENGTH_LONG).show();

                    }
                });
                while (true)
                {
                    mysocket = ss.accept();
                    dis = new DataInputStream(mysocket.getInputStream());
                    message = dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Message from Device A: "+message,Toast.LENGTH_LONG).show();

                        }
                    });

                }

            }catch(IOException e)
            {
                e.printStackTrace();
            }

        }
    }


    class backgroundTask extends AsyncTask<String, Void, String>
    {
        Socket s;
        DataOutputStream dos;
        String ip,message;


        @Override
        protected String doInBackground(String... params) {
            ip = params[0];
            message=params[1];

            try{
                s = new Socket(ip,9700);
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                s.close();

            }catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }

}
