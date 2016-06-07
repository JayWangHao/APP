package com.cattsoft.framework.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.Window;

import com.cattsoft.framework.R;
import com.cattsoft.framework.template.TabViewFragmentActivity;
import com.cattsoft.framework.template.TestActivity;

public class WelcomeActivity extends Activity {
	
	public static final String LOGIN ="com.cattsoft.mainframework.LOGIN";

    private boolean isFirstRun = true;
    private SharedPreferences sharedPreferences;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_activity);
        loadGuideInfo();
        Start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void Start() {
        new Thread() {
                public void run() {
                        try {
                                Thread.sleep(2500);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        
                        if(isFirstRun){
        					isFirstRun = false;
        				//	startActivity(new Intent(WelcomeActivity.this,TabViewFragmentActivity.class));
        					
        				//	startActivity(new Intent(WelcomeActivity.this,MosGuideActivity.class));
        					
        				//	startActivity(new Intent(WelcomeActivity.this,TestActivity.class));
        					startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
        					
        				}else{

                            Intent intent = new Intent();
                            intent.setAction(LOGIN);
                            startActivity(intent);
        				}
                        
                        finish();
                }
        }.start();
}
    
    private void loadGuideInfo(){
		sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
		isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
	}
}
