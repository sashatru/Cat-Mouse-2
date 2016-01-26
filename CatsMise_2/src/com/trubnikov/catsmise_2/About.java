package com.trubnikov.catsmise_2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.startad.lib.SADView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

public class About extends Activity implements OnClickListener{

	Button back;
	TextView text;
	Typeface tf;

	AlertDialog.Builder ad;
	Context context;
	
	private float textSize, butTextSize;
	
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
	    textSize = 20*scale;
	    butTextSize = 18*scale; 
    	tf = Typeface.createFromAsset(getAssets(),"fonts/SEGOESCB.TTF");

        
        setContentView(R.layout.about);

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

	    back = (Button) findViewById(R.id.bBack);
	    back.setOnClickListener(this);
	    back.setText(R.string.back);
	    back.setTypeface(tf);
	    back.setTextSize(TypedValue.COMPLEX_UNIT_SP,butTextSize);
	    back.setTextColor(getResources().getColor(R.color.yellow));
	    back.setShadowLayer(2, 3, 3, R.color.dark_grey);
	    text = (TextView) findViewById(R.id.textView1);
	    text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSize);
	    text.setTypeface(tf);
	    text.setShadowLayer(2, 3, 3, R.color.dark_grey);
	    text.setLineSpacing(0.0f, 0.6f);
    }

	@Override
	public void onClick(View v) {
		Intent intent=null;
		intent=new Intent(this,MainMenu.class); startActivity(intent); finish();
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
		    context = About.this;
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
