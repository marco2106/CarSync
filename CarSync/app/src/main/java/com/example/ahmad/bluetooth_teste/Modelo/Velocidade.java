package com.example.ahmad.bluetooth_teste.Modelo;

import android.util.Log;

import com.example.ahmad.bluetooth_teste.Activities.Comunicacao;
import com.example.ahmad.bluetooth_teste.Activities.Requisicoes;

/**
 * Created by PamelaPeixinho on 4/27/16.
 */
public class Velocidade extends AbstractComandoOBD{


    public Velocidade(){
        super("010D");
    }

    @Override
    public void run() {
        while(true){
            setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
            calculate();
            Requisicoes.respVelocidade = getResposta() + " Km/h";
            Requisicoes.updateRequisicoesView();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("Velocidade: ", getResposta());
        }
    }

    @Override
    protected void calculate() {
        String respAux = getResposta();
        respAux = respAux.substring(respAux.length() - 2);
        Long i = Long.parseLong(respAux, 16);
        setResposta(Long.toString(i));
    }
}