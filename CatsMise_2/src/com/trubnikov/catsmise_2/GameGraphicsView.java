package com.trubnikov.catsmise_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "DrawAllocation", "HandlerLeak", "ViewConstructor" })

public class GameGraphicsView extends View 
{
	
	// имя файла настроек 
    SharedPreferences mSettings;
	private boolean sound; //SharedPreferences
	private int complication; //SharedPreferences
	private int level;//SharedPreferences
	private int openlevel, levelinit;

	private float soundlevel;

	int widthView;
	int heightView;
	int centerX;
    int mouseWidth, mouseHeight, catWidth, catHeight;
    float scalePics;
    int scaledMouseWidth, scaledMouseHeight, scaledMouseEscapeWidth, scaledMouseEscapeHeight, scaledMouseTrappedWidth, scaledMouseTrappedHeight;
    int scaledCatWidth, scaledCatHeight, scaledCatMissWidth, scaledCatMissHeight, scaledCaughtWidth, scaledCaughtHeight;
    int scaledCheeseTrapWidth, scaledCheeseTrapHeight;
    int scaledDoorSize;
    int distanceToCheeseTrap, distanceToCat;
    
	Paint paint = new Paint();
	Typeface tf; 
	float textX, textY;
	float textSize;
	float textWidth;
	float textX1, textY1;
	float textSize1;
	float textWidth1;
	float textX2, textY2;
	float textSize2;
	float textWidth2;
	String textOnScreen = "";
	String textOnScreen1 = "";
	String textOnScreen2 = "";

	int countCheese;
	int amountOfCheese;
	ArrayList<Bitmap> scaledCheese, cheeseBitmap;
	float cheeseX, cheeseY;
	int cheese = R.drawable.cheese;

	int trap = R.drawable.mousetrap;
	ArrayList<Bitmap> scaledTrap;
	float trapX, trapY;

	//координаты узлов размещения сыра и ловушек и их признаки
	float [] ctX = new float[15];
	float [] ctY = new float[15];;
	boolean [] cell = new boolean [15];
	boolean [] isCheese = new boolean [15];
	ArrayList<Integer> numerator;
	private int n;

    int mouseXleft, mouseXright, mouseYtop, mouseYbottom; //границы фигуры
    int catXleft, catXright, catYtop, catYbottom; //границы фигуры
	int intMouseX1, intMouseY1,	intCatX1,  intCatY1; 
	
	float mouseX1, mouseY1;
	float mouseX2, mouseY2;
	float mouseXdelta, mouseYdelta;
	float mouseSpeed=9.0f; //скорость движения пикс/такт мышки
	float mousePath; //расчетный путь
	float mouseMove; //движение фигуры
	float mouseNumberSteps; // путь/скорость

	float catX1, catY1;
	float catX2, catY2;
	float catXdelta, catYdelta;
	float catSpeed; //скорость движения пикс/такт 2 фигуры
	float catPath; //расчетный путь
	float catMove; //движение фигуры
	float catNumberSteps; // путь/скорость
	
	int mouse = R.drawable.mouse_tss0;
	int [] mouseR = {R.drawable.mouse_right0, R.drawable.mouse_right1, R.drawable.mouse_right2, R.drawable.mouse_right3}; 
	int [] mouseL = {R.drawable.mouse_left0, R.drawable.mouse_left1, R.drawable.mouse_left2, R.drawable.mouse_left3}; 
	int [] mouseTss = {R.drawable.mouse_tss0, R.drawable.mouse_tss1, R.drawable.mouse_tss2, R.drawable.mouse_tss3, R.drawable.mouse_tss4, R.drawable.mouse_tss5, R.drawable.mouse_tss0}; 

	int [] mouseEscaped = {R.drawable.mouse_escape00, R.drawable.mouse_escape01,R.drawable.mouse_escape02,R.drawable.mouse_escape03,R.drawable.mouse_escape04,R.drawable.mouse_escape05,R.drawable.mouse_escape06,R.drawable.mouse_escape07,
	R.drawable.mouse_escape08,R.drawable.mouse_escape09,R.drawable.mouse_escape10,R.drawable.mouse_escape11,R.drawable.mouse_escape12,R.drawable.mouse_escape13,R.drawable.mouse_escape14,R.drawable.mouse_escape15,R.drawable.mouse_escape16,
	R.drawable.mouse_escape17,R.drawable.mouse_escape18,R.drawable.mouse_escape19,R.drawable.mouse_escape20,R.drawable.mouse_escape21,R.drawable.mouse_escape22,R.drawable.mouse_escape23,R.drawable.mouse_escape24,R.drawable.mouse_escape25,
	R.drawable.mouse_escape26,R.drawable.mouse_escape27,R.drawable.mouse_escape28,R.drawable.mouse_escape29,R.drawable.mouse_escape30,R.drawable.mouse_escape31,R.drawable.mouse_escape32,R.drawable.mouse_escape33,R.drawable.mouse_escape34,
	R.drawable.mouse_escape35,R.drawable.mouse_escape36,R.drawable.mouse_escape37,R.drawable.mouse_escape38,R.drawable.mouse_escape39};

	int [] mouseTrapped = {R.drawable.mouse_trapped0, R.drawable.mouse_trapped1,R.drawable.mouse_trapped2,R.drawable.mouse_trapped3,R.drawable.mouse_trapped4,R.drawable.mouse_trapped5,R.drawable.mouse_trapped6,R.drawable.mouse_trapped7};
	
	int mouseMovingNumberPic = 0;
	
	int cat = R.drawable.cat;

	int [] catR = {R.drawable.cat_right00, R.drawable.cat_right01,R.drawable.cat_right02,R.drawable.cat_right03,R.drawable.cat_right04,R.drawable.cat_right05,R.drawable.cat_right06,R.drawable.cat_right07,
	R.drawable.cat_right08,R.drawable.cat_right09,R.drawable.cat_right10,R.drawable.cat_right11};
	int [] catR_b = {R.drawable.bcat_right00, R.drawable.bcat_right01,R.drawable.bcat_right02,R.drawable.bcat_right03,R.drawable.bcat_right04,R.drawable.bcat_right05,R.drawable.bcat_right06,R.drawable.bcat_right07,
	R.drawable.bcat_right08,R.drawable.bcat_right09,R.drawable.bcat_right10,R.drawable.bcat_right11};
	int [] catR_g = {R.drawable.cat_right00g, R.drawable.cat_right01g,R.drawable.cat_right02g,R.drawable.cat_right03g,R.drawable.cat_right04g,R.drawable.cat_right05g,R.drawable.cat_right06g,R.drawable.cat_right07g,
	R.drawable.cat_right08g,R.drawable.cat_right09g,R.drawable.cat_right10g,R.drawable.cat_right11g};
	
	int [] catL = {R.drawable.cat_left00, R.drawable.cat_left01,R.drawable.cat_left02,R.drawable.cat_left03,R.drawable.cat_left04,R.drawable.cat_left05,R.drawable.cat_left06,R.drawable.cat_left07,
	R.drawable.cat_left08,R.drawable.cat_left09,R.drawable.cat_left10,R.drawable.cat_left11};
	int [] catL_b = {R.drawable.bcat_left00, R.drawable.bcat_left01,R.drawable.bcat_left02,R.drawable.bcat_left03,R.drawable.bcat_left04,R.drawable.bcat_left05,R.drawable.bcat_left06,R.drawable.bcat_left07,
	R.drawable.bcat_left08,R.drawable.bcat_left09,R.drawable.bcat_left10,R.drawable.bcat_left11};
	int [] catL_g = {R.drawable.cat_left00g, R.drawable.cat_left01g,R.drawable.cat_left02g,R.drawable.cat_left03g,R.drawable.cat_left04g,R.drawable.cat_left05g,R.drawable.cat_left06g,R.drawable.cat_left07g,
	R.drawable.cat_left08g,R.drawable.cat_left09g,R.drawable.cat_left10g,R.drawable.cat_left11g};

	int [] catLick = {R.drawable.cat_lick00, R.drawable.cat_lick01,R.drawable.cat_lick02,R.drawable.cat_lick03,R.drawable.cat_lick04,R.drawable.cat_lick05,R.drawable.cat_lick06,R.drawable.cat_lick07,
	R.drawable.cat_lick08,R.drawable.cat_lick09,R.drawable.cat_lick10,R.drawable.cat_lick11,R.drawable.cat_lick12,R.drawable.cat_lick13,R.drawable.cat_lick14,R.drawable.cat_lick15,R.drawable.cat_lick16,
	R.drawable.cat_lick17,R.drawable.cat_lick18,R.drawable.cat_lick19,R.drawable.cat_lick20,R.drawable.cat_lick21,R.drawable.cat_lick22,R.drawable.cat_lick23,R.drawable.cat_lick24,R.drawable.cat_lick25,
	R.drawable.cat_lick26,R.drawable.cat_lick27,R.drawable.cat_lick28,R.drawable.cat_lick29,R.drawable.cat};
	int [] catLick_b = {R.drawable.bcat_lick00, R.drawable.bcat_lick01,R.drawable.bcat_lick02,R.drawable.bcat_lick03,R.drawable.bcat_lick04,R.drawable.bcat_lick05,R.drawable.bcat_lick06,R.drawable.bcat_lick07,
	R.drawable.bcat_lick08,R.drawable.bcat_lick09,R.drawable.bcat_lick10,R.drawable.bcat_lick11,R.drawable.bcat_lick12,R.drawable.bcat_lick13,R.drawable.bcat_lick14,R.drawable.bcat_lick15,R.drawable.bcat_lick16,
	R.drawable.bcat_lick17,R.drawable.bcat_lick18,R.drawable.bcat_lick19,R.drawable.bcat_lick20,R.drawable.bcat_lick21,R.drawable.bcat_lick22,R.drawable.bcat_lick23,R.drawable.bcat_lick24,R.drawable.bcat_lick25,
	R.drawable.bcat_lick26,R.drawable.bcat_lick27,R.drawable.bcat_lick28,R.drawable.bcat_lick29,R.drawable.bcat};
	int [] catLick_g = {R.drawable.cat_lick00g, R.drawable.cat_lick01g,R.drawable.cat_lick02g,R.drawable.cat_lick03g,R.drawable.cat_lick04g,R.drawable.cat_lick05g,R.drawable.cat_lick06g,R.drawable.cat_lick07g,
	R.drawable.cat_lick08g,R.drawable.cat_lick09g,R.drawable.cat_lick10g,R.drawable.cat_lick11g,R.drawable.cat_lick12g,R.drawable.cat_lick13g,R.drawable.cat_lick14g,R.drawable.cat_lick15g,R.drawable.cat_lick16g,
	R.drawable.cat_lick17g,R.drawable.cat_lick18g,R.drawable.cat_lick19g,R.drawable.cat_lick20g,R.drawable.cat_lick21g,R.drawable.cat_lick22g,R.drawable.cat_lick23g,R.drawable.cat_lick24g,R.drawable.cat_lick25g,
	R.drawable.cat_lick26g,R.drawable.cat_lick27g,R.drawable.cat_lick28g,R.drawable.cat_lick29g,R.drawable.catg};

	int [] caughtMouse = {R.drawable.catch00, R.drawable.catch01,R.drawable.catch02,R.drawable.catch03,R.drawable.catch04,R.drawable.catch05,R.drawable.catch06,R.drawable.catch07,
	R.drawable.catch08,R.drawable.catch09,R.drawable.catch10,R.drawable.catch11,R.drawable.catch12,R.drawable.catch13,R.drawable.catch14,R.drawable.catch15,R.drawable.catch16,
	R.drawable.catch17,R.drawable.catch18,R.drawable.catch19,R.drawable.catch20,R.drawable.catch21,R.drawable.catch22,R.drawable.catch23,R.drawable.catch24,R.drawable.catch25,
	R.drawable.catch26,R.drawable.catch27,R.drawable.catch28,R.drawable.catch29,R.drawable.catch30,R.drawable.catch31,R.drawable.catch32};
	int [] caughtMouse_b = {R.drawable.bcatch00, R.drawable.bcatch01,R.drawable.bcatch02,R.drawable.bcatch03,R.drawable.bcatch04,R.drawable.bcatch05,R.drawable.bcatch06,R.drawable.bcatch07,
	R.drawable.bcatch08,R.drawable.bcatch09,R.drawable.bcatch10,R.drawable.bcatch11,R.drawable.bcatch12,R.drawable.bcatch13,R.drawable.bcatch14,R.drawable.bcatch15,R.drawable.bcatch16,
	R.drawable.bcatch17,R.drawable.bcatch18,R.drawable.bcatch19,R.drawable.bcatch20,R.drawable.bcatch21,R.drawable.bcatch22,R.drawable.bcatch23,R.drawable.bcatch24,R.drawable.bcatch25,
	R.drawable.bcatch26,R.drawable.bcatch27,R.drawable.bcatch28,R.drawable.bcatch29,R.drawable.bcatch30,R.drawable.bcatch31,R.drawable.bcatch32};
	int [] caughtMouse_g = {R.drawable.catch00g, R.drawable.catch01g,R.drawable.catch02g,R.drawable.catch03g,R.drawable.catch04g,R.drawable.catch05g,R.drawable.catch06g,R.drawable.catch07g,
	R.drawable.catch08g,R.drawable.catch09g,R.drawable.catch10g,R.drawable.catch11g,R.drawable.catch12g,R.drawable.catch13g,R.drawable.catch14g,R.drawable.catch15g,R.drawable.catch16g,
	R.drawable.catch17g,R.drawable.catch18g,R.drawable.catch19g,R.drawable.catch20g,R.drawable.catch21g,R.drawable.catch22g,R.drawable.catch23g,R.drawable.catch24g,R.drawable.catch25g,
	R.drawable.catch26g,R.drawable.catch27g,R.drawable.catch28g,R.drawable.catch29g,R.drawable.catch30g,R.drawable.catch31g,R.drawable.catch32g};

	int [] catMiss = {R.drawable.cat_miss00, R.drawable.cat_miss01,R.drawable.cat_miss02,R.drawable.cat_miss03,R.drawable.cat_miss04,R.drawable.cat_miss05,R.drawable.cat_miss06,R.drawable.cat_miss07,
	R.drawable.cat_miss08,R.drawable.cat_miss09,R.drawable.cat_miss10,R.drawable.cat_miss11,R.drawable.cat_miss12,R.drawable.cat_miss13,R.drawable.cat_miss14,R.drawable.cat_miss15,R.drawable.cat_miss16,
	R.drawable.cat_miss17,R.drawable.cat_miss18,R.drawable.cat_miss19,R.drawable.cat_miss20,R.drawable.cat_miss21,R.drawable.cat_miss22,R.drawable.cat_miss23,R.drawable.cat_miss24,R.drawable.cat_miss25,
	R.drawable.cat_miss26,R.drawable.cat_miss27,R.drawable.cat_miss28,R.drawable.cat_miss29,R.drawable.cat_miss30,R.drawable.cat_miss31,R.drawable.cat_miss32,R.drawable.cat_miss33,R.drawable.cat_miss34,
	R.drawable.cat_miss35,R.drawable.cat_miss36,R.drawable.cat_miss37,R.drawable.cat_miss38,R.drawable.cat_miss39};
	int [] catMiss_b = {R.drawable.bcat_miss00, R.drawable.bcat_miss01,R.drawable.bcat_miss02,R.drawable.bcat_miss03,R.drawable.bcat_miss04,R.drawable.bcat_miss05,R.drawable.bcat_miss06,R.drawable.bcat_miss07,
	R.drawable.bcat_miss08,R.drawable.bcat_miss09,R.drawable.bcat_miss10,R.drawable.bcat_miss11,R.drawable.bcat_miss12,R.drawable.bcat_miss13,R.drawable.bcat_miss14,R.drawable.bcat_miss15,R.drawable.bcat_miss16,
	R.drawable.bcat_miss17,R.drawable.bcat_miss18,R.drawable.bcat_miss19,R.drawable.bcat_miss20,R.drawable.bcat_miss21,R.drawable.bcat_miss22,R.drawable.bcat_miss23,R.drawable.bcat_miss24,R.drawable.bcat_miss25,
	R.drawable.bcat_miss26,R.drawable.bcat_miss27,R.drawable.bcat_miss28,R.drawable.bcat_miss29,R.drawable.bcat_miss30,R.drawable.bcat_miss31,R.drawable.bcat_miss32,R.drawable.bcat_miss33,R.drawable.bcat_miss34,
	R.drawable.bcat_miss35,R.drawable.bcat_miss36,R.drawable.bcat_miss37,R.drawable.bcat_miss38,R.drawable.bcat_miss39};
	int [] catMiss_g = {R.drawable.cat_miss00g, R.drawable.cat_miss01g,R.drawable.cat_miss02g,R.drawable.cat_miss03g,R.drawable.cat_miss04g,R.drawable.cat_miss05g,R.drawable.cat_miss06g,R.drawable.cat_miss07g,
	R.drawable.cat_miss08g,R.drawable.cat_miss09g,R.drawable.cat_miss10g,R.drawable.cat_miss11g,R.drawable.cat_miss12g,R.drawable.cat_miss13g,R.drawable.cat_miss14g,R.drawable.cat_miss15g,R.drawable.cat_miss16g,
	R.drawable.cat_miss17g,R.drawable.cat_miss18g,R.drawable.cat_miss19g,R.drawable.cat_miss20g,R.drawable.cat_miss21g,R.drawable.cat_miss22g,R.drawable.cat_miss23g,R.drawable.cat_miss24g,R.drawable.cat_miss25g,
	R.drawable.cat_miss26g,R.drawable.cat_miss27g,R.drawable.cat_miss28g,R.drawable.cat_miss29g,R.drawable.cat_miss30g,R.drawable.cat_miss31g,R.drawable.cat_miss32g,R.drawable.cat_miss33g,R.drawable.cat_miss34g,
	R.drawable.cat_miss35g,R.drawable.cat_miss36g,R.drawable.cat_miss37g,R.drawable.cat_miss38g,R.drawable.cat_miss39g};

	int catMovingNumberPic = 0;
	
	int [] door = {R.drawable.door00, R.drawable.door01,R.drawable.door02,R.drawable.door03,R.drawable.door04,R.drawable.door05,R.drawable.door06,R.drawable.door07,
	R.drawable.door08,R.drawable.door09,R.drawable.door10,R.drawable.door11,R.drawable.door12,R.drawable.door13,R.drawable.door14};
	
	MouseAsyncTask mouseAsyncTask; 
	CatAsyncTask catAsyncTask;
	Thread threadCaught, threadIntro, threadEscape, threadTrap, threadDoor;
	Handler handlerCaught, handlerIntro, handlerEscape, handlerTrap, handlerDoor;
	
	Context context;
	MainActivity mActivity; 

	Canvas canvas;
	private Bitmap mouseBitmap, catBitmap, trapBitmap, doorBitmap, scaledDoor, scaledMouse, scaledCat;
	
	boolean ontouchevent, splashscreenintro=true, splashscreencaught, splashscreenescape, overdraw, catmove, doorclosed=true, stopdoorthread=false;

	SoundPool soundPool;
	boolean playRunMusic = true;
	MediaPlayer mediaPlayer;
	

	
	
	public GameGraphicsView(Context context, MainActivity mActivity) 
	{
		super(context); 

	//вычисляем размеры дисплея и коэффициенты масштабирования
    DisplayMetrics display = this.getResources().getDisplayMetrics();
    heightView = display.heightPixels;
    widthView = display.widthPixels;
    
    //коэффициент масштабирования картинок
    scalePics = (float) (widthView/1280.0f) ;

    //коэффициент масштабирования текста
    float scale = (float) (heightView/720.0f) ;
    textSize = 50*scale;
	paint.setTextSize(textSize);
	centerX = widthView/2;
	textY = 100*scale;
	textY1 = 160*scale;
	textY2 = 220*scale;

    
    //масштабируем сыр и мышеловку
    scaledCheeseTrapWidth = Math.round(100 * scalePics);
    scaledCheeseTrapHeight = Math.round(84 * scalePics);

    //масштабируем калитку
    scaledDoorSize = Math.round(80 * scalePics);
    
    //масштабируем мышку
    scaledMouseWidth = Math.round(108 * scalePics);
    scaledMouseHeight = Math.round(160 * scalePics);
    scaledMouseEscapeWidth = Math.round(229 * scalePics);
    scaledMouseEscapeHeight = Math.round(280 * scalePics);
    scaledMouseTrappedWidth = Math.round(350* scalePics);
    scaledMouseTrappedHeight = Math.round(184 * scalePics);
 
    //масштабируем кошку
    scaledCatWidth = Math.round(141 * scalePics);
    scaledCatHeight = Math.round(160 * scalePics);
    scaledCatMissWidth = Math.round(203 * scalePics);
    scaledCatMissHeight = Math.round(280 * scalePics);
    scaledCaughtWidth = Math.round(339 * scalePics);
    scaledCaughtHeight = Math.round(350 * scalePics);

    //дистанции событий
    distanceToCat = scaledMouseWidth/4+scaledCatWidth/4;
    distanceToCheeseTrap = scaledMouseWidth/4+scaledCheeseTrapHeight/4;

    //координаты узлов размещения сыра и ловушек
    ctX[0]=widthView/6-scaledCheeseTrapWidth/2; ctY[0]=heightView/4-scaledCheeseTrapHeight/2;	
	ctX[1]=widthView/6-scaledCheeseTrapWidth/2; ctY[1]=heightView/2-scaledCheeseTrapHeight/2;
	ctX[2]=widthView*5/6-scaledCheeseTrapWidth/2; ctY[2]=heightView*3/4-scaledCheeseTrapHeight/2;
	ctX[3]=widthView*5/6-scaledCheeseTrapWidth/2; ctY[3]=heightView/2-scaledCheeseTrapHeight/2;
	ctX[4]=widthView/6-scaledCheeseTrapWidth/2; ctY[4]=heightView*3/4-scaledCheeseTrapHeight/2;
	ctX[5]=widthView*4/6-scaledCheeseTrapWidth/2; ctY[5]=heightView*3/4-scaledCheeseTrapHeight/2;
	ctX[6]=widthView/2-scaledCheeseTrapWidth/2; ctY[6]=heightView/4-scaledCheeseTrapHeight/2;
	ctX[7]=widthView/3-scaledCheeseTrapWidth/2; ctY[7]=heightView*3/4-scaledCheeseTrapHeight/2;
	ctX[8]=widthView*5/6-scaledCheeseTrapWidth/2; ctY[8]=heightView/4-scaledCheeseTrapHeight/2;
	ctX[9]=widthView/3-scaledCheeseTrapWidth/2; ctY[9]=heightView/4-scaledCheeseTrapHeight/2;
	ctX[10]=widthView/2-scaledCheeseTrapWidth/2; ctY[10]=heightView*3/4-scaledCheeseTrapHeight/2;
	ctX[11]=widthView*4/6-scaledCheeseTrapWidth/2; ctY[11]=heightView/4-scaledCheeseTrapHeight/2;
	ctX[12]=widthView/3-scaledCheeseTrapWidth/2; ctY[12]=heightView/2-scaledCheeseTrapHeight/2;
	ctX[13]=widthView/2-scaledCheeseTrapWidth/2; ctY[13]=heightView/2-scaledCheeseTrapHeight/2;
	ctX[14]=widthView*4/6-scaledCheeseTrapWidth/2; ctY[14]=heightView/2-scaledCheeseTrapHeight/2;
	
	//вызов SoundPool, Mediaplayer и начальных параметров игры  
	this.mActivity=mActivity; 
	soundPool= mActivity.getSoundPool();
	mediaPlayer= mActivity.getMediaPlayer();
	mSettings = mActivity.mSettings;
    sound = mSettings.getBoolean("Sound", true);
    soundlevel=(sound)?0.4f:0;
	complication = mSettings.getInt("Complication", 1);
	level = mSettings.getInt("Level", 1);
	levelinit=level;
    switch (complication) 
    {
	case 1: catSpeed=4.0f; openlevel = mSettings.getInt("OpenLevelE", 1); break;
	case 2: catSpeed=5.0f; openlevel = mSettings.getInt("OpenLevelN", 1); break;
	case 3: catSpeed=7.0f; openlevel = mSettings.getInt("OpenLevelH", 1); break;
	}
    //text
    tf = mActivity.tf;
    paint.setTypeface(tf);
	paint.setAntiAlias(true);
    paint.setShadowLayer(2, 3, 3, R.color.dark_grey);
	
	}

	@Override
	protected void onDraw(Canvas canvas)
	{	
	    
		if(splashscreenintro) splashScreenIntro();

			for(int i=0; i<levelinit; i++)//раскладем сыр и мышеловки
			{
				if(i%2==0) {n=numerator.get(i); isCheese[n]=true; cheeseX=ctX[n]; cheeseY=ctY[n]; 
							if(!scaledCheese.get(n).isRecycled()) 
							{canvas.drawBitmap(scaledCheese.get(n), cheeseX, cheeseY, null);}}
				else {n=numerator.get(i); trapX=ctX[n]; trapY=ctY[n]; 
							if(!scaledTrap.get(n).isRecycled())
							{canvas.drawBitmap(scaledTrap.get(n), trapX, trapY, null);}}
			}
			canvas.drawText(textOnScreen, textX, textY, paint);
			canvas.drawText(textOnScreen1, textX1, textY1, paint);
			canvas.drawText(textOnScreen2, textX2, textY2, paint);
			canvas.drawBitmap(scaledDoor, widthView-scaledDoorSize, 0, null);
			if(scaledMouse!=null&&!scaledMouse.isRecycled()){canvas.drawBitmap(scaledMouse, mouseX1, mouseY1, null);}
			if(scaledCat!=null&&!scaledCat.isRecycled()){canvas.drawBitmap(scaledCat, catX1, catY1, null);}
	}

	// splashScreenIntro ---------------------------------------------------------
	void  splashScreenIntro (){
		
		//Случайный нумератор выбранных узлов размещения сыра и ловушек
		numerator = new ArrayList<Integer>();
		for (int i=0; i<=14; i++) 
		{
			numerator.add(i); //заряжаем нумератор числами
			cell[i]=false; //обнуляем ячейки
			isCheese[i]=false; //обнуляем сыр
		}

	    Collections.shuffle(numerator); //трясем нумератор

		levelinit=level;
	    
	    for(int i=0; i<levelinit; i++)
		{
			n=numerator.get(i); cell[n]=true; //открываем ячейки для проверки действия
		}
		//-------------------------------------------------------------

	    doorBitmap=BitmapFactory.decodeResource(getResources(), door[0]);
		scaledDoor=Bitmap.createScaledBitmap(doorBitmap, scaledDoorSize, scaledDoorSize, true);
		doorclosed=true;
		stopdoorthread=false;
		
		textOnScreen = "";
		textOnScreen1 = "";
		textOnScreen2 = "";

		mouseX1=0; 
		mouseY1=(heightView-scaledMouseHeight);;

		catX1=(float) (widthView-scaledCatWidth);
		catY1=0;
		
		trapBitmap = BitmapFactory.decodeResource(getResources(), trap);
		cheeseBitmap = new ArrayList<Bitmap>();
		scaledCheese = new ArrayList<Bitmap>();
		scaledTrap = new ArrayList<Bitmap>();
		countCheese=0;
		
		for (int i=0; i<=14; i++) 
		{		
				cheeseBitmap.add(BitmapFactory.decodeResource(getResources(), cheese));
				scaledCheese.add(Bitmap.createScaledBitmap(cheeseBitmap.get(i), scaledCheeseTrapWidth, scaledCheeseTrapHeight, true));
				scaledTrap.add(Bitmap.createScaledBitmap(trapBitmap, scaledCheeseTrapWidth, scaledCheeseTrapHeight, true));
		}

		amountOfCheese=(levelinit-1)/2+1;//количество сыра которое нужно собрать
		if(splashscreenintro)
		{
			splashscreenintro=false;
			threadIntro = new Thread (new Runnable()
			{
			@Override
			public void run() {
				for (int i=0; i<=30; i++){
				try {TimeUnit.MILLISECONDS.sleep(60);}
				catch (InterruptedException e) {e.printStackTrace();}
				handlerIntro.sendEmptyMessage(i);}	
			}
		});

		threadIntro.start();

		handlerIntro = new Handler()
		{
			@Override
			public void handleMessage(android.os.Message msg)
			{	int pic=0;
		    switch (complication) 
		    {
		    case 1: catBitmap = BitmapFactory.decodeResource(getResources(), catLick[msg.what]); break;
		    case 2: catBitmap = BitmapFactory.decodeResource(getResources(), catLick_g[msg.what]); break;
		    case 3: catBitmap = BitmapFactory.decodeResource(getResources(), catLick_b[msg.what]); break;
		    }	
			scaledCat= Bitmap.createScaledBitmap(catBitmap, scaledCatWidth, scaledCatHeight, true); 
				pic=msg.what/5;
				mouseBitmap = BitmapFactory.decodeResource(getResources(), mouseTss[pic]);
			    scaledMouse=Bitmap.createScaledBitmap(mouseBitmap, scaledMouseWidth, scaledMouseHeight, true);
			    if (msg.what==30) {
			    ontouchevent = true; 
			    overdraw = true; 
			    catmove=true; 
			    try {mediaPlayer.start();} catch (Exception e) {e.printStackTrace();}
			    }
				else {ontouchevent = false;}
				invalidate();
			};
		};
		}
		}	
	// splashScreenIntro ---------------------------------------------------------

	// splashScreenCaught ---------------------------------------------------------
	void  splashScreenCaught (){

		if(splashscreencaught)
		{
			catX1=(float) (widthView/2-scaledCaughtWidth/2);
			catY1=(float) (heightView/2-scaledCaughtHeight/2);;

			paint.setColor(Color.RED);
			textOnScreen = getResources().getString(R.string.what_a_pity);
	        // Используем measureText для измерения ширины
	        textWidth = paint.measureText(textOnScreen);
			textX = centerX-(textWidth/2.0f);

			splashscreencaught=false;
			threadCaught = new Thread (new Runnable()
		{
			@Override
			public void run() {
				for (int i=0; i<=32; i++){
				try {TimeUnit.MILLISECONDS.sleep(60);}
				catch (InterruptedException e) {e.printStackTrace();}
				handlerCaught.sendEmptyMessage(i);}	
			}
		});

		threadCaught.start();

		handlerCaught = new Handler()
		{
			@Override
			public void handleMessage(android.os.Message msg)
			{	
				switch (complication) 
			    {
				case 1: catBitmap = BitmapFactory.decodeResource(getResources(), caughtMouse[msg.what]); break;
				case 2: catBitmap = BitmapFactory.decodeResource(getResources(), caughtMouse_g[msg.what]); break;
				case 3: catBitmap = BitmapFactory.decodeResource(getResources(), caughtMouse_b[msg.what]); break;
				}
				scaledCat= Bitmap.createScaledBitmap(catBitmap, scaledCaughtWidth, scaledCaughtHeight, true);
				invalidate();
			    if (msg.what==32) {splashscreenintro=true; splashScreenIntro();}
			};
		};
		}
		}	
	// splashScreenCaught ---------------------------------------------------------

	// splashScreenTrapped ---------------------------------------------------------
	void  splashScreenTrapped (){
		screenRecycle();
		soundPool.play(mActivity.soundTrapped,soundlevel,soundlevel,0,0,1);
		
		mouseX1=(float) (widthView/2-scaledMouseTrappedWidth/2);
		mouseY1=(float) (heightView/2-scaledMouseTrappedHeight/2);;

		paint.setColor(Color.RED);
		textOnScreen = getResources().getString(R.string.again_trapped);
        // Используем measureText для измерения ширины
        textWidth = paint.measureText(textOnScreen);
		textX = centerX-(textWidth/2.0f);

		threadTrap = new Thread (new Runnable()
		{
			@Override
			public void run() {
				for (int i=0; i<=7; i++){
				try {TimeUnit.MILLISECONDS.sleep(300);}
				catch (InterruptedException e) {e.printStackTrace();}
				handlerTrap.sendEmptyMessage(i);}	
			}
		});

		threadTrap.start();

		handlerTrap = new Handler()
		{
			@Override
			public void handleMessage(android.os.Message msg)
			{	
				mouseBitmap = BitmapFactory.decodeResource(getResources(), mouseTrapped[msg.what]);
				scaledMouse= Bitmap.createScaledBitmap(mouseBitmap, scaledMouseTrappedWidth, scaledMouseTrappedHeight, true);
				invalidate();
			    if (msg.what==7) 
			    { 
			    splashscreenintro=true; 
			    splashScreenIntro();
			    }
			};
		};
		}
	// splashScreenTrapped ---------------------------------------------------------

	// splashScreenDoorOpened ---------------------------------------------------------
	void  splashScreenDoorOpened (){
		soundPool.play(mActivity.soundDoorOpened,soundlevel,soundlevel,0,0,1);
		
		threadDoor = new Thread (new Runnable()
		{
			@Override
			public void run() {
				for (int i=0; i<=14; i++){
					if (stopdoorthread) {i=14; handlerDoor.sendEmptyMessage(0);} //мышка попалась во время открытия двери
					else{
				try {TimeUnit.MILLISECONDS.sleep(214);}
				catch (InterruptedException e) {e.printStackTrace();}
				handlerDoor.sendEmptyMessage(i);}
						}	
			}
		});

		threadDoor.start();

		handlerDoor = new Handler()
		{
			@Override
			public void handleMessage(android.os.Message msg)
			{	
				doorBitmap = BitmapFactory.decodeResource(getResources(), door[msg.what]);
				scaledDoor= Bitmap.createScaledBitmap(doorBitmap, scaledDoorSize, scaledDoorSize, true);
				invalidate();
			};
		};
		}
	// splashScreenDoorOpened ---------------------------------------------------------

	
	// splashScreenEscape ---------------------------------------------------------
	void  splashScreenEscape (){
		if(splashscreenescape)
		{
			catX1=(float) (widthView/4-scaledCatMissWidth/2);
			catY1=(float) (heightView/2-scaledCatMissHeight/2);;
			mouseX1=(float) (widthView*3/4-scaledMouseEscapeWidth/2);
			mouseY1=(float) (heightView/2-scaledMouseEscapeHeight/2);;

			paint.setColor(Color.YELLOW);

			SharedPreferences.Editor editor = mSettings.edit();      

			if (level == 15)
			{
			    switch (complication) 
			    {
				case 1: textOnScreen = getResources().getString(R.string.escaped_one);
						textOnScreen1 = getResources().getString(R.string.escaped_one_1);
						textOnScreen2 = getResources().getString(R.string.escaped_one_2);
						complication=2; level=1; break;
				case 2: textOnScreen = getResources().getString(R.string.escaped_two);
						textOnScreen1 = getResources().getString(R.string.escaped_two_1);
						textOnScreen2 = getResources().getString(R.string.escaped_two_2);
						complication=3; level=1; break;
				case 3: textOnScreen = getResources().getString(R.string.escaped_three);
						textOnScreen1 = getResources().getString(R.string.escaped_three_1);
						break;
				}
				editor.putInt("Complication", complication);
		        textWidth1 = paint.measureText(textOnScreen1);
				textX1 = centerX-(textWidth1/2.0f);
		        textWidth2 = paint.measureText(textOnScreen2);
				textX2 = centerX-(textWidth2/2.0f);
			}
			
			else if(level<15) 
			{
				level++; 
				textOnScreen = getResources().getString(R.string.escaped);

				if(openlevel<level) openlevel=level;

		    switch (complication) 
		    {
			case 1: editor.putInt("OpenLevelE", openlevel);  break;
			case 2: editor.putInt("OpenLevelN", openlevel);  break;
			case 3: editor.putInt("OpenLevelH", openlevel);  break;
			}
			}

			editor.putInt("Level", level);
			// Apply the edits!      
		    editor.apply();    

	        textWidth = paint.measureText(textOnScreen);
			textX = centerX-(textWidth/2.0f);

			
			splashscreenescape=false;
			threadEscape = new Thread (new Runnable()
		{
			@Override
			public void run() {
			for (int i=0; i<=39; i++){
			try {TimeUnit.MILLISECONDS.sleep(200);}
			catch (InterruptedException e) {e.printStackTrace();}
			handlerEscape.sendEmptyMessage(i);}	
			}
		});
		threadEscape.start();

		handlerEscape = new Handler()
		{
			@Override
			public void handleMessage(android.os.Message msg)
			{
				switch (complication) 
			    {
				case 1:	catBitmap = BitmapFactory.decodeResource(getResources(), catMiss[msg.what]); break;
				case 2:	if (level == 1) {catBitmap = BitmapFactory.decodeResource(getResources(), catMiss[msg.what]);}
						else {catBitmap = BitmapFactory.decodeResource(getResources(), catMiss_g[msg.what]);} break;
				case 3:	if (level == 1) {catBitmap = BitmapFactory.decodeResource(getResources(), catMiss_g[msg.what]);}
						else {catBitmap = BitmapFactory.decodeResource(getResources(), catMiss_b[msg.what]);} break;
				}
				scaledCat= Bitmap.createScaledBitmap(catBitmap, scaledCatMissWidth, scaledCatMissHeight, true);
				mouseBitmap = BitmapFactory.decodeResource(getResources(), mouseEscaped[msg.what]);
			    scaledMouse=Bitmap.createScaledBitmap(mouseBitmap, scaledMouseEscapeWidth, scaledMouseEscapeHeight, true);
				invalidate();
			    if (msg.what==39) {
			    splashscreenintro=true; 
			    splashScreenIntro();}
			};
		};
		}
		}
	// splashScreenEscape ---------------------------------------------------------

	
	@Override
	public boolean onTouchEvent (MotionEvent event)
	{

		if (ontouchevent)
		{	
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{	
			if (mouseAsyncTask != null) {mouseMove=mousePath; mouseAsyncTask.cancel(true);}		 

			 // проверка на выход рисунка за пределы view

			if ((event.getX()-(float)scaledMouseWidth/2)<=0) {mouseX2=0;}
			 else {if ((event.getX()+(float)scaledMouseWidth/2)>widthView) {mouseX2=(float)widthView-(float)scaledMouseWidth;}
			 else {mouseX2 = event.getX()-(float)scaledMouseWidth/2;}}

			 if ((event.getY()-(float)scaledMouseHeight/2)<=0) {mouseY2=0;}
			 else {if ((event.getY()+(float)scaledMouseHeight/2)>heightView) {mouseY2=(float)heightView-(float)scaledMouseHeight;}
			 else {mouseY2 = event.getY()-(float)scaledMouseHeight/2;}}

			 
			mousePath=(float)Math.hypot((mouseX2-mouseX1),(mouseY2-mouseY1)); //гипотенуза из теоремы Пифагора
			mouseNumberSteps=mousePath/mouseSpeed;
			mouseXdelta=(mouseX2-mouseX1)/mouseNumberSteps;
			mouseYdelta=(mouseY2-mouseY1)/mouseNumberSteps;

			if (mouseX2==0&&mouseY2==0&&mouseX1==0&&mouseY1==0){} //что бы не зависла
			else {moveMouse();}//двигаем фигурку
		}
		}
		else {return false;}
		return false;
	}
	
//обработка событий--------------------------------------------------------------------------------------------
	void overDraw()
	{	
		if(overdraw){
//		Log.d(TAG,"overDraw");

		//номер кадра мышки
		if (mouseMovingNumberPic<3) {mouseMovingNumberPic++;}
		else {mouseMovingNumberPic=0;}
		//номер кадра кота
		if (catMovingNumberPic<11) {catMovingNumberPic++;}
		else {catMovingNumberPic=0;}

		//округленные координаты мышки и кота
		intMouseX1 = (int)mouseX1; 
//		Log.d(TAG,"intMouseX1="+intMouseX1);
		intMouseY1 = (int)mouseY1; 
//		Log.d(TAG,"intMouseY1="+intMouseY1);
		intCatX1 = (int)catX1; 
		intCatY1 = (int)catY1; 
		
		//координаты вершин рисунка мышки
		mouseXright = (intMouseX1+scaledMouseWidth);
		mouseYtop = intMouseY1;

		catXleft = intCatX1; 
		catXright = (intCatX1+catWidth);
		catYtop = intCatY1;
		catYbottom = (intCatY1+catHeight);

		//если мышка нашла сыр или попала в мышеловку------------------------------------------
	    for (int i=0; i<=14; i++) 
		{
		if(cell[i])
		{if(Math.hypot((mouseX1-ctX[i]),(mouseY1-ctY[i]))<=distanceToCheeseTrap)
		{cell[i]=false; 	
		if(isCheese[i]) {soundPool.play(mActivity.soundNyam,soundlevel,soundlevel,1,0,1); scaledCheese.get(i).recycle(); countCheese++;}
		else {splashScreenTrapped();}}}
		}
		//если мышка нашла сыр или попала в мышеловку------------------------------------------
		
		//если мышка съела весь сыр ------------------------------------------
		if ((doorclosed)&&(countCheese==amountOfCheese)) {doorclosed=false; splashScreenDoorOpened();}
		//если мышка съела весь сыр ------------------------------------------
		
		//если кошка поймала мышку-------------------------------------------
		if 	(Math.hypot((mouseX1-catX1),(mouseY1-catY1))<=distanceToCat)
			{
			screenRecycle();
			splashscreencaught=true;
			soundPool.play(mActivity.soundCaught,soundlevel,soundlevel,0,0,1);
			splashScreenCaught();
			}
		//если кошка поймала мышку-------------------------------------------

		//если мышка убежала-------------------------------------------
		else if ((countCheese==amountOfCheese)&&(mouseXright>=widthView-10)&&(mouseYtop<=10))
			{
			screenRecycle();
			splashscreenescape=true;
			soundPool.play(mActivity.soundEscaped,soundlevel,soundlevel,0,0,1);
			splashScreenEscape();
			}
		//если мышка убежала--------------------------------------------

		
		else
		{		//перерисовываем картинку в координатах 
				if (!mouseBitmap.isRecycled())
				{if (mouseMove>=mousePath)	{mouseBitmap = BitmapFactory.decodeResource(getResources(), mouse);} //если остановилась
				else {	if(mouseX2>=mouseX1) {mouseBitmap = BitmapFactory.decodeResource(getResources(), mouseR[mouseMovingNumberPic]);} //если бежит направо
						else {mouseBitmap = BitmapFactory.decodeResource(getResources(), mouseL[mouseMovingNumberPic]);}} //если бежит налево
				scaledMouse=Bitmap.createScaledBitmap(mouseBitmap, scaledMouseWidth, scaledMouseHeight, true);
				}

				if (!catBitmap.isRecycled())
				{if(catX2>=catX1) //если бежит направо
				{
					switch (complication) 
				    {
					case 1: catBitmap = BitmapFactory.decodeResource(getResources(), catR[catMovingNumberPic]); break;
					case 2: catBitmap = BitmapFactory.decodeResource(getResources(), catR_g[catMovingNumberPic]); break;
					case 3: catBitmap = BitmapFactory.decodeResource(getResources(), catR_b[catMovingNumberPic]); break;
					}
				} 
				else //если бежит налево
				{
					switch (complication) 
				    {
					case 1:catBitmap = BitmapFactory.decodeResource(getResources(), catL[catMovingNumberPic]); break;
					case 2:catBitmap = BitmapFactory.decodeResource(getResources(), catL_g[catMovingNumberPic]); break;
					case 3:catBitmap = BitmapFactory.decodeResource(getResources(), catL_b[catMovingNumberPic]); break;
					}
				} 
				scaledCat=Bitmap.createScaledBitmap(catBitmap, scaledCatWidth, scaledCatHeight, true);
				}
				invalidate();		
		}
		}
	}  
	//обработка событий конец-------------------------------------------------------------------------------------------------------
		
	void screenRecycle(){
		stopdoorthread = true; ontouchevent = false; catmove=false;
		try 
		{
			mediaPlayer.pause();
			mediaPlayer.seekTo(0);
		} catch (Exception e) {e.printStackTrace();}
		
		if (mouseAsyncTask != null) {mouseMove=mousePath; mouseAsyncTask.cancel(true);}		 
		if (catAsyncTask != null) {catMove=catPath; catAsyncTask.cancel(true);}		 

		try{
			for(int i=0; i<=14; i++)//очистим сыр и мышеловки
			{
				if (!scaledCheese.get(i).isRecycled())//удаляем если сыр не съеден 
				{scaledCheese.get(i).recycle();}
				scaledTrap.get(i).recycle();
			}
			} catch (Exception e) {e.printStackTrace();}
				scaledMouse.recycle(); //bitmap стереть
				scaledCat.recycle(); //bitmap стереть
				splashscreenintro=false;
				overdraw=false;
	}

	public void moveMouse ()
	{
		mouseAsyncTask= new MouseAsyncTask();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{mouseAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);}
		else{mouseAsyncTask.execute();}
		}

	public void moveCat ()
	{
		if (catmove){
		catAsyncTask= new CatAsyncTask();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{catAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);}
		else{catAsyncTask.execute();}
		}
		}

	public class MouseAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) 
		{
				for (mouseMove=0; mouseMove<mousePath; mouseMove+=mouseSpeed)
				{
					isCancelled();
					if (isCancelled()) return null;
					publishProgress();
					SystemClock.sleep(75);
				}
			return null;
		} 
		@Override
		protected void onProgressUpdate (Void... params)//имеет доступ к UI
		{
		super.onProgressUpdate();
		mouseX1+=mouseXdelta;
		mouseY1+=mouseYdelta;
		catX2 = mouseX1;
		catY2 = mouseY1;
		catPath=(float)Math.hypot((catX2-catX1),(catY2-catY1)); 
		catNumberSteps=catPath/catSpeed;
		catXdelta=(catX2-catX1)/catNumberSteps;
		catYdelta=(catY2-catY1)/catNumberSteps;
		if (catAsyncTask != null) {catMove=catPath; catAsyncTask.cancel(true);}
		moveCat();
		}
		@Override
		protected void onCancelled()
		{super.onCancelled();} 
	}//Конец MouseAsyncTask

	public class CatAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) 
		{
				for (catMove=0; catMove<=catPath; catMove+=catSpeed)
				{
					isCancelled();
					if (isCancelled()) return null;
					publishProgress();
					SystemClock.sleep(75);
				}
			return null;
		} 
		@Override
		protected void onProgressUpdate (Void... params)
		{
		super.onProgressUpdate();
		if (catmove){
		catX1+=catXdelta;
		catY1+=catYdelta;
		overDraw();
		}
		}

	@Override
		protected void onCancelled()
		{super.onCancelled();} 
	}//Конец CatAsyncTask1	
}//GameGraphicsView конец




