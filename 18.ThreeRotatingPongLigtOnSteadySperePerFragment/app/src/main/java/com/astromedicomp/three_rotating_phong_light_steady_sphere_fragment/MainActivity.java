package com.astromedicomp.three_rotating_phong_light_steady_sphere_fragment;

import android.app.Activity;
import android.view.Window; //for Window class
import android.view.WindowManager; // for WindowManager class
import android.content.pm.ActivityInfo; // for ActivityInfo class
import android.os.Bundle;
import android.graphics.Color;

public class MainActivity extends Activity {
	private GLESView glesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // to remove title bar of the window
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
     
        //setContentView(R.layout.activity_main); //commented because we will not be using activity_main class from XML, we will be writing our own Java class for activity_main
		
		MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//this.getWindow().getDecorView().setBackgroundColor(Color.rgb(0,0,0)); //no need to mention this. DecorView also called as RootView
		
		glesView = new GLESView(this);
		
		setContentView(glesView);
	
    }
	 @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
    }
}
