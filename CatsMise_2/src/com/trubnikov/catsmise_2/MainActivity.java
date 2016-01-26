package com.trubnikov.catsmise_2;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


public class MainActivity extends Activity {


	// имя файла настроек
    public static final String APP_PREFERENCES = "mysettings"; 
    SharedPreferences mSettings;
	private boolean music;

	
	int soundRun, soundCaught, soundNyam, soundTrapped, soundDoorOpened, soundEscaped;
	SoundPool soundPool;

	MediaPlayer mediaPlayer;
	AudioManager audioManager;
	
	GameGraphicsView myGameGraphicsView;

	AlertDialog.Builder ad;
	Context context;

	Typeface tf;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // если хотим, чтобы экран не выключался
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		
        mSettings = getSharedPreferences(APP_PREFERENCES, 0);
        music = mSettings.getBoolean("Music", true); 

		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		mediaPlayer = MediaPlayer.create(this, R.raw.run_mouse);
		if (music) mediaPlayer.setVolume(0.4f, 0.4f); else mediaPlayer.setVolume(0.0f, 0.0f);
		mediaPlayer.setLooping(true);
        soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
        //подключаем звук через ресурсы
        soundCaught = soundPool.load(this, R.raw.caught_mouse, 1);	
        soundNyam = soundPool.load(this, R.raw.nyam, 1);	
        soundTrapped = soundPool.load(this, R.raw.trapped, 1);	
        soundDoorOpened = soundPool.load(this, R.raw.door_opened, 1);	
        soundEscaped = soundPool.load(this, R.raw.get_burrow, 1);	
 
    	tf = Typeface.createFromAsset(getAssets(),"fonts/SEGOESCB.TTF");

        
        setContentView(new GameGraphicsView(getApplicationContext(),this));
	}//onCreate


    @Override
    public void onResume() {
       super.onResume();
   	if (mediaPlayer != null)
   	{
   		try {mediaPlayer.start();}
   		catch (Exception e) {e.printStackTrace();}
   	}
    }

	


	public SoundPool getSoundPool() {
		return soundPool;
	}
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
@Override 
protected void onStop() {
	super.onStop();
	if (mediaPlayer != null)
	{
		try {mediaPlayer.pause();
		mediaPlayer.seekTo(0);}
		catch (Exception e) {e.printStackTrace();}
	}
}

@Override 
protected void onDestroy() {
	super.onDestroy();
	soundPool.release();
	if (mediaPlayer != null)
	{
		try {mediaPlayer.release(); mediaPlayer=null;}
		catch (Exception e) {e.printStackTrace();}
	}
}

//диалог кнопки Back
@Override
public void onBackPressed() {
	openQuitDialog();
}

private void openQuitDialog() {
	
	//Настройка AlertDialog
    context = MainActivity.this;
    String title = getResources().getString(R.string.do_exit);
    String button1String = getResources().getString(R.string.exit);
    String button2String = getResources().getString(R.string.main_menu);	    
    String button3String = getResources().getString(R.string.cancel);
    ad = new AlertDialog.Builder(context);
    ad.setTitle(title);  // заголовок
    ad.setIcon(R.drawable.ic_launcher);//иконка
    ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int arg1) 
        {
        	finish();            }
    });
    ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int arg1) {
        	goBack();
        }
    });
    ad.setNeutralButton(button3String, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int arg1) {               
            }
    });
    ad.setCancelable(true);
    ad.setOnCancelListener(new OnCancelListener() {
        public void onCancel(DialogInterface dialog) {
            }
    });	

    ad.show();
}

void goBack(){
	Intent intent = new Intent(this,MainMenu.class);
	startActivity(intent);finish();	
	}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main_menu, menu);
	return true;
}
@Override
public boolean onOptionsItemSelected (MenuItem item)
{
	switch (item.getItemId()) 
	{
	case R.id.main_menu: {Intent intent = new Intent(this,MainMenu.class); startActivity(intent);finish();}	return true;
	case R.id.exit: finish(); return true;
	
	default: return super.onOptionsItemSelected(item);
	}
}

}
