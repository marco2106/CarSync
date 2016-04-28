package com.example.ahmad.bluetooth_teste.Activities;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ahmad.bluetooth_teste.Modelo.RPM;
import com.example.ahmad.bluetooth_teste.Modelo.Velocidade;
import com.example.ahmad.bluetooth_teste.R;

/**
 * Created by ahmad on 25/04/2016.
 */
public class Requisicoes extends AppCompatActivity{

    private Velocidade velocidade;
    private RPM rpm;

    private Button btnCombustivel;

    private static TextView txtVelocidade;
    private static TextView txtRPM;
    private static TextView txtCombustivel;

    public static String respVelocidade;
    public static String respRPM;

    private TabHost tabHost;

    private static Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec aba1=tabHost.newTabSpec("PRIMEIRA");
        aba1.setContent(R.id.PRIMEIRA);
        aba1.setIndicator("PERFORMANCE");

        TabHost.TabSpec aba2=tabHost.newTabSpec("SEGUNDA");
        aba2.setContent(R.id.SEGUNDA);
        aba2.setIndicator("SEGUNDA");

        TabHost.TabSpec aba3=tabHost.newTabSpec("TERCEIRA");
        aba3.setContent(R.id.TERCEIRA);
        aba3.setIndicator("TERCEIRA");

        tabHost.addTab(aba1);
        tabHost.addTab(aba2);
        tabHost.addTab(aba3);


        velocidade = new Velocidade();
        rpm = new RPM();

        txtVelocidade = (TextView)findViewById(R.id.textView_velocidade);
        txtRPM = (TextView)findViewById(R.id.textView_rpm);
        txtCombustivel = (TextView)findViewById(R.id.textView_combustivel);

        velocidade.start();
        rpm.start();


        btnCombustivel = (Button)findViewById(R.id.button_combustivel);
        btnCombustivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCombustivel(v);
            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                Log.d("COMB 2", " "+ tabHost.getCurrentTab() + " " + tabId);
            }
        });
    }
    public void getCombustivel(View v){
        String respDec = Comunicacao.sendReceiveOBD("012F");
        txtCombustivel.setText(respDec + "%");

    }


    public void getVelocidade(View v){

        String respDec = Comunicacao.sendReceiveOBD("010D").substring(4);
        Log.d("RESP", respDec);
        Long i = Long.parseLong(respDec, 16);
        Log.d("RESP", Long.toString(i));
        txtVelocidade.setText(Long.toString(i) + " Km/h");

    }

    public void getRPM(View v){
        String respDec = Comunicacao.sendReceiveOBD("010C");
        respDec = respDec.substring(respDec.length() - 4);
        Long i = Long.parseLong(respDec, 16);
        i /= 4;
        Log.d("RESP", respDec);
        txtRPM.setText(Long.toString(i)+ " RPM");
    }

    public synchronized static void updateRequisicoesView(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                while(true)
                txtVelocidade.setText(respVelocidade);
                txtRPM.setText(respRPM);
            }
        });
    }



}