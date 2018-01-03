package com.astromedicomp.helloworld2;

import android.widget.TextView;
import android.content.Context;
import android.view.Gravity;
import android.graphics.Color;

public class MyView extends TextView {
	public MyView(Context context)
	{
		super(context);
		setText("Hellow World!!!");
		setTextSize(60);
		setTextColor(Color.GREEN);
		setGravity(Gravity.CENTER);
	}
}
