package com.trubnikov.catsmise_2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.startad.lib.SADView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class Complication extends Activity implements OnClickListener{

	private RadioButton rbEasy, rbNormal, rbHard;
	

	AlertDialog.Builder ad;
	Context context;

	
	private int complication;
	private int level, openlevel_e, openlevel_n, openlevel_h;//SharedPreferences;

// имя файла настроек
    public static final String APP_PREFERENCES = "mysettings"; 
    
    SharedPreferences mSettings;

	private float textSize;
	Typeface tf;

	
	//Реклама StartAd.mobi
	protected SADView sadView;
	//Реклама
	private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
		//вычисляем размеры дисплея и коэффициенты масштабирования для текста 
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int height = display.heightPixels;

        //коэффициент масштабирования текста
	    float scale = (float) (height/720.0f) ;
	    textSize = 26*scale;
	   	tf = Typeface.createFromAsset(getAssets(),"fonts/SEGOESCB.TTF");
      
        
        setContentView(R.layout.complic_layout);

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
        complication = mSettings.getInt("Complication", 1); 
        openlevel_e = mSettings.getInt("OpenLevelE", 1);
        openlevel_n = mSettings.getInt("OpenLevelN", 1);
        openlevel_h = mSettings.getInt("OpenLevelH", 1);
       
        rbEasy = (RadioButton) findViewById(R.id.rbEasy); 
        rbEasy.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        rbEasy.setTypeface(tf);
        rbEasy.setOnClickListener(this);
        rbNormal = (RadioButton) findViewById(R.id.rbNormal); 
        rbNormal.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        rbNormal.setTypeface(tf);
        rbNormal.setOnClickListener(this);
        rbHard = (RadioButton) findViewById(R.id.rbHard); 
        rbHard.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        rbHard.setTypeface(tf);
        rbHard.setOnClickListener(this); 
        switch (complication) 
        {
		case 1: rbEasy.setChecked(true); break;
		case 2: rbNormal.setChecked(true); break;
		case 3: rbHard.setChecked(true); break;
		}
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
	    if (this.sadView != null) {this.sadView.destroy();}
	    super.onDestroy();
	  }

    
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {	
		case R.id.rbEasy: complication=1; level=openlevel_e; 
		intent=new Intent(this,MainMenu.class); startActivity(intent); finish(); break;
		case R.id.rbNormal: complication=2; level=openlevel_n;
		intent=new Intent(this,MainMenu.class); startActivity(intent);finish(); break;
		case R.id.rbHard: complication=3; level=openlevel_h; 
		intent=new Intent(this,MainMenu.class); startActivity(intent);finish(); break;		
		}
		transData();
	}

	
	private void transData() { //передача данных между активити

	    // We need an Editor object to make preference changes.
	    // All objects are from android.context.Context      
	    SharedPreferences.Editor editor = mSettings.edit();      
	    editor.putInt("Complication", complication);
	    editor.putInt("Level", level);
	    // Apply the edits!      
	    editor.apply();    
	}

	
	
	//диалог кнопки Back
	@Override
	public void onBackPressed() {
		openQuitDialog();
	}

	private void openQuitDialog() {
		
		//Настройка AlertDialog
	    context = Complication.this;
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
