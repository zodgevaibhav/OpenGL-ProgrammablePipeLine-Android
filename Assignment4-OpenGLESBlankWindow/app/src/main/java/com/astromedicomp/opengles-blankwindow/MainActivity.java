package com.astromedicomp.openglesblankwindow;

import android.app.Activity;
import android.view.Window; //for Window class
import android.view.WindowManager; // for WindowManager class
import android.content.pm.ActivityInfo; // for ActivityInfo class
import android.os.Bundle;
import android.graphics.Color;

public class MainActivity extends Activity {
	private MyView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // to remove title bar of the window
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); //commented because we will not be using activity_main class from XML, we will be writing our own Java class for activity_main
		
		MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().getDecorView().setBackgroundColor(Color.rgb(0,0,0)); //no need to mention this. DecorView also called as RootView
		
		myView = new MyView(this);
		
		setContentView(myView);
	
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
