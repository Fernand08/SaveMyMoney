package com.demo.savemymoney.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class ClockNotifyService extends Service {

    private  static  final  String LOGTAG = ClockNotifyService.class.getSimpleName();

    private Timer temporizador;
    private TimerTask tarea;
    private Handler handler = new Handler();
    private SharedPreferences preferences;
  private   Integer count ;

    public ClockNotifyService() {
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOGTAG,"Servicio creado");


        IniciarTemporizador();
    }

    public void getCount(Integer integer){

       preferences  = getApplication(). getApplicationContext().getSharedPreferences("countDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("count");
        editor.putString("count",String.valueOf(integer));
        editor.commit();

    }

    public  void notifys(){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Savemymoney")
                .setContentText("Te recuerdo que registres tus gastos al dia")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    private void IniciarTemporizador(){

   String counter = preferences.getString("count","");
     Integer count = Integer.parseInt(counter);

        Date horaDeAvisar = new Date(System.currentTimeMillis());
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Date ahora = now.getTime();
        Calendar c = Calendar.getInstance();

        c.setTime(horaDeAvisar);

        if(c.get(Calendar.HOUR_OF_DAY )>23){
            c.set(Calendar.DAY_OF_YEAR,c.get(Calendar.DAY_OF_YEAR)+1);
        }
        c.set(Calendar.HOUR_OF_DAY,3);
        c.set(Calendar.MINUTE,10);
        c.set(Calendar.SECOND,0);
        horaDeAvisar =  c.getTime();
       // String horaDeAvisar_string = horaDeAvisar.toString();
        //String ahora_string = ahora.toString();
         Integer countAhora = (int) (new Date().getTime() / 1000);
        Integer countAvisa = (int)(horaDeAvisar.getTime()/1000);
        temporizador = new Timer();

        tarea = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run(){

                        new Thread(new Runnable() {
                            public void run() {

                                while(countAhora >= countAvisa){
                                    if(count >=0){
                                        notifys();
                                    }else
                                        Log.d("Detail","Detail found");

                                }
                                try {

                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    }
                });
            }
        };
        int tiempoRepeticion = 86400000;// un dia

        temporizador.schedule(tarea, horaDeAvisar, tiempoRepeticion);
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences = getApplicationContext().getSharedPreferences("countDetail", Context.MODE_PRIVATE);
        IniciarTemporizador();
        return START_STICKY;



    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOGTAG,"Servicio destruido");
    }

    public class ClockBinder extends Binder{
        public ClockNotifyService getClockBinder(){
            return ClockNotifyService.this;
        }
    }

    public  final IBinder binder = new ClockBinder();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.



        return  binder;
    }


}
