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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Level extends Activity implements OnClickListener{

	LinearLayout layout1;
	TableLayout tableLayout;
	TableRow [] tableRow;
	Button [] butLevel;
	final int m=5, n=3, b=m*n; //количество строк, столбцов, кнопок
	int enableLevel = R.drawable.butlevel_e;
	int disableLevel = R.drawable.level_d;

	AlertDialog.Builder ad;
	Context context;
	
	private int complication; //SharedPreferences
	private int openlevel, level;
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
	    textSize = 32*scale;
      
	   	tf = Typeface.createFromAsset(getAssets(),"fonts/SEGOESCB.TTF");
        
        setContentView(R.layout.level_layout);

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
        level = mSettings.getInt("Level", 1);
    	complication = mSettings.getInt("Complication", 1);
        switch (complication) 
        {
    	case 1: openlevel = mSettings.getInt("OpenLevelE", 1); break;
    	case 2: openlevel = mSettings.getInt("OpenLevelN", 1); break;
    	case 3: openlevel = mSettings.getInt("OpenLevelH", 1); break;
    	}
        
		layout1 = (LinearLayout) findViewById(R.id.levelLayout);
		layout1.setPadding(10, 20, 20, 20);

		//программные объекты
		tableLayout = new TableLayout(this);
		tableRow = new TableRow [m];
		butLevel = new Button [b];// кнопки 
		int butN = 0;
		for (int i=0; i<m; i++)
		{
			tableRow[i]=new TableRow(this);
			tableRow[i].setLayoutParams(new TableLayout.LayoutParams(-1,-1, 1.0f));//(-1,-1, 1.0f) - равномерное распределение объектов  
			for (int j=0; j<n; j++)
			{
				butLevel[butN] = new Button (this);
				butLevel[butN].setId(butN+1);
				butLevel[butN].setOnClickListener(this);
				butLevel[butN].setLayoutParams(new TableRow.LayoutParams(-1,-1, 1.0f));//(-1,-1, 1.0f) - равномерное распределение объектов  
				if (butN<openlevel)
				{//активные кнопки 
				butLevel[butN].setBackgroundResource(enableLevel);
				butLevel[butN].setText(""+(butN+1));
				butLevel[butN].setTypeface(tf);
				butLevel[butN].setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
				butLevel[butN].setTextColor(getResources().getColor(R.color.yellow));
				butLevel[butN].setShadowLayer(2, 3, 3, R.color.dark_grey);
				}
				else 
				{//не активные кнопки
				butLevel[butN].setBackgroundResource(disableLevel);
				butLevel[butN].setEnabled(false);
				butLevel[butN].setText(""+(butN+1));
				butLevel[butN].setTypeface(tf);
				butLevel[butN].setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
				butLevel[butN].setTextColor(getResources().getColor(R.color.dark_grey));
				}
				tableRow[i].addView(butLevel[butN]);
				butN++;
				}
			tableLayout.addView(tableRow[i]);//строка в таблицу
		}
		layout1.addView(tableLayout);//таблица в слой
    }

	@Override
	public void onClick(View v) {

		level = v.getId();
		transData();
		Intent intent=null;
		intent=new Intent(this,MainMenu.class); startActivity(intent); finish();
	}

	private void transData() { //передача данных между активити

	    // We need an Editor object to make preference changes.
	    // All objects are from android.context.Context      
	    SharedPreferences.Editor editor = mSettings.edit();      
	    editor.putInt("Level", level);
	    // Apply the edits!      
	    editor.apply();    
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


	//диалог кнопки Back
	@Override
	public void onBackPressed() {
		openQuitDialog();
	}

	private void openQuitDialog() {
		
		//Настройка AlertDialog
	    context = Level.this;
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
		getMenuInflater().inflate(R.menu.level_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) 
		{
		case R.id.main_menu: {Intent intent = new Intent(this,MainMenu.class); startActivity(intent);finish();}	return true;
		case R.id.clean_progress:
			level=1;
		    SharedPreferences.Editor editor = mSettings.edit();      
		    editor.putInt("Level", level);
	        switch (complication) 
	        {
	    	case 1: editor.putInt("OpenLevelE", 1); break;
	    	case 2: editor.putInt("OpenLevelN", 1); break;
	    	case 3: editor.putInt("OpenLevelH", 1); break;
	    	}
		    editor.apply();    
		    recreate();
			; return true;
		case R.id.exit: finish(); return true;
		
		default: return super.onOptionsItemSelected(item);
		}
	}
}
