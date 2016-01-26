package com.trubnikov.catsmise_2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.startad.lib.SADView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu extends Activity implements OnClickListener{

	Button musicBut, soundBut, complicBut, levelBut, playBut, aboutBut;

	private float textSize;
	Typeface tf;

	private int level, openlevel_e, openlevel_n, openlevel_h;//SharedPreferences
	private  boolean sound, music;


	// имя файла настроек
    public static final String APP_PREFERENCES = "mysettings"; 
    SharedPreferences mSettings;
	
	Context context;
	AlertDialog.Builder ad;
	
	//Реклама StartAd.mobi
	protected SADView sadView;
	//Реклама
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_layout);

		//вычисляем размеры дисплея и коэффициенты масштабирования для текста 
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int height = display.heightPixels;
        //коэффициент масштабирования текста
	    float scale = (float) (height/720.0f) ;
	    textSize = 18*scale;
      
	   	tf = Typeface.createFromAsset(getAssets(),"fonts/SEGOESCB.TTF");
		
        // Реклама StartAd.mobi
        this.sadView = new SADView(this, getString(R.string.banner_start_ad_mobi_id));
        LinearLayout layout = (LinearLayout)findViewById(R.id.advLayout);
        layout.addView(this.sadView);
        //Load ad for currently active language in app
        if(getResources().getString(R.string.hello_world).equals("Hello"))
        {this.sadView.loadAd(SADView.LANGUAGE_EN);} //or 
        else 
        {this.sadView.loadAd(SADView.LANGUAGE_RU);}
		
		//Реклама
	    // Creating an AdView
		adView = (AdView) findViewById(R.id.adView);
	    // Creating an AdRequest
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	    .addTestDevice("TEST_DEVICE_ID")
	    .build();
	    // Загрузка adView с объявлением.
	    adView.loadAd(adRequest);

        mSettings = getSharedPreferences(APP_PREFERENCES, 0);
        sound = mSettings.getBoolean("Sound", true); 
        music = mSettings.getBoolean("Music", true);
        level = mSettings.getInt("Level", 1);
        openlevel_e = mSettings.getInt("OpenLevelE", 1);
        openlevel_n = mSettings.getInt("OpenLevelN", 1);
        openlevel_h = mSettings.getInt("OpenLevelH", 1);

        
		musicBut= (Button) findViewById(R.id.bMusic); musicBut.setOnClickListener(this);
		if (music) musicBut.setBackgroundResource(R.drawable.butmusic); else musicBut.setBackgroundResource(R.drawable.butmusic_off);
		soundBut= (Button) findViewById(R.id.bSound); soundBut.setOnClickListener(this);
		if (sound) soundBut.setBackgroundResource(R.drawable.butsound); else soundBut.setBackgroundResource(R.drawable.butsound_off);
		complicBut= (Button) findViewById(R.id.bComplication); complicBut.setOnClickListener(this);
		complicBut.setText(R.string.complication);
		complicBut.setTypeface(tf);
		complicBut.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		complicBut.setTextColor(getResources().getColor(R.color.yellow));
		complicBut.setShadowLayer(2, 3, 3, R.color.dark_grey);
		levelBut= (Button) findViewById(R.id.bLevel); levelBut.setOnClickListener(this);
		levelBut.setText(R.string.level);
		levelBut.setTypeface(tf);
		levelBut.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		levelBut.setTextColor(getResources().getColor(R.color.yellow));
		levelBut.setShadowLayer(2, 3, 3, R.color.dark_grey);
		aboutBut= (Button) findViewById(R.id.bAbout); aboutBut.setOnClickListener(this);
		aboutBut.setText(R.string.b_about);
		aboutBut.setTypeface(tf);
		aboutBut.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		aboutBut.setTextColor(getResources().getColor(R.color.yellow));
		aboutBut.setShadowLayer(2, 3, 3, R.color.dark_grey);
		playBut= (Button) findViewById(R.id.bPlay); playBut.setOnClickListener(this);
		playBut.setText(R.string.play);
		playBut.setTypeface(tf);
		playBut.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		playBut.setTextColor(getResources().getColor(R.color.yellow));
		playBut.setShadowLayer(2, 3, 3, R.color.dark_grey);

	}
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(null != this.sadView) this.sadView.saveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        if(null != this.sadView) this.sadView.restoreInstanceState(outState);
    }
		
	@Override
	  public void onPause() {
	    adView.pause();
	    super.onPause();
	  }

	  @Override
	  public void onResume() {
	    super.onResume();
	    adView.resume();
	  }

	  @Override
	  public void onDestroy() {
	    adView.destroy();
	    try {
	    	if (this.sadView != null) {this.sadView.destroy();}
		} catch (Exception e) {	}
	    super.onDestroy();
	  }

	    @Override    protected void onStop()
	    {
	    transData();
	    super.onStop();      
	    }

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId())
		{
		case R.id.bPlay:{transData(); intent=new Intent(this,MainActivity.class); 
		startActivity(intent); finish(); break;}

		case R.id.bComplication:{intent=new Intent(this,Complication.class);
		startActivity(intent);finish(); break;}

		case R.id.bLevel:{intent=new Intent(this,Level.class);
		startActivity(intent);finish(); break;}

		case R.id.bAbout:{intent=new Intent(this,About.class);
		startActivity(intent);finish(); break;}
		
		case R.id.bMusic: 	if (music) {music=false; musicBut.setBackgroundResource(R.drawable.butmusic_off);} 
							else {music=true;musicBut.setBackgroundResource(R.drawable.butmusic);} break;

		case R.id.bSound: 	if (sound) {sound=false; soundBut.setBackgroundResource(R.drawable.butsound_off);} 
							else {sound=true;soundBut.setBackgroundResource(R.drawable.butsound);} break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		finish();
		return super.onOptionsItemSelected(item);
	}

	private void transData() { //передача данных между активити

	    // We need an Editor object to make preference changes.
	    // All objects are from android.context.Context      
	    SharedPreferences.Editor editor = mSettings.edit();      
	    editor.putBoolean("Sound", sound);
	    editor.putBoolean("Music", music);
	    editor.putInt("Level", level);
	    editor.putInt("OpenLevelE", openlevel_e); 
	    editor.putInt("OpenLevelN", openlevel_n); 
	    editor.putInt("OpenLevelH", openlevel_h); 
	    // Apply the edits!      
	    editor.apply();    
	}

}
