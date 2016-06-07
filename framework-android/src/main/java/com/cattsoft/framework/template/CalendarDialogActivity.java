package com.cattsoft.framework.template;


import java.util.Date;

import android.os.Bundle;
import android.widget.Toast;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.view.CalendarView;
import com.cattsoft.framework.view.CalendarView.OnItemClickListener;

public class CalendarDialogActivity extends BaseActivity{

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_calendar_view);
		CalendarView calendar = (CalendarView) findViewById(R.id.calendar);
		//获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getYearAndmonth().split("-"); 
		//点击上一月 同样返回年月 
		String leftYearAndmonth = calendar.clickLeftMonth(); 
		String[] lya = leftYearAndmonth.split("-");
		//点击下一月
		String rightYearAndmonth = calendar.clickRightMonth(); 
		String[] rya = rightYearAndmonth.split("-");
		calendar.setOnItemClickListener(new calendarItemClickListener());
	}
	
	class calendarItemClickListener implements OnItemClickListener{
		@Override
		public void OnItemClick(Date date) {
			Toast.makeText(getApplicationContext(), date+"", Toast.LENGTH_SHORT).show();
		}
	 }

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub
		
	}
}
