package br.com.fei.carsync.manager.model;

import android.util.Log;

import br.com.fei.carsync.view.activity.Comunicacao;
import br.com.fei.carsync.view.activity.Requisicoes;

/**
 * Created by PamelaPeixinho on 5/9/16.
 */
    public class TemperaturaOleoMotor extends AbstractComandoOBD {

            public TemperaturaOleoMotor(){ super("015C"); }

            @Override
            public void run() {
    //        setSuspend(false);
                while(true){
                    setResposta(Comunicacao.sendReceiveOBD(getPID())); //synchronized function
                    calculate();
                    Requisicoes.respTemperaturaOleoMotor = getResposta();
                    Requisicoes.updateRequisicoesViewPrevencao();
                    Log.d("Thread", "Temp Oleo");
                    try {
                        Thread.sleep(1000);
                        if (isSuspend()) {
                            synchronized(this) {
                                Log.d("Thread", "Suspender RPM");
                                while(isSuspend())
                                    wait();
                                Log.d("Thread", "Sai, não estou mais suspenso RPM");
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
    //            Log.d("RPM ", getResposta());
                }

    //        Log.d("Thread", "SAI DO LOOP DE REQUISICOES DO RPM");
    //        this.currentThread().interrupt();
    //        Log.d("Thread", "isInterrupted?" + String.valueOf(this.currentThread().isInterrupted()));
            }

            @Override
            protected void calculate() {
                String respAux = getResposta();
                respAux = respAux.substring(respAux.length() - 2);
                Log.d("Thread", "Resp TempOleoMotor: " + respAux);
                Long i = Long.parseLong(respAux, 16);
                i-=40;
                Log.d("Thread", "Resp TempOleoMotor Dec: " + String.valueOf(i));
                setResposta(Long.toString(i));
            }

            @Override
            public synchronized void notifyThread(){
                if (!isSuspend())
                    notify();
            }
        }

