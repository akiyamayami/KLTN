package com.tlcn.service;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.tlcn.dto.ModelCalendar;

@Service
public class CalendarService {
	public String createCalendar(String month, String year){
		System.out.println(month+"xx" + year);
		Calendar now = Calendar.getInstance();
		int y,m;
		if(month == null || year == null)
		{
			m = now.get(Calendar.MONTH);
			y = now.get(Calendar.YEAR);
			now.set(y, m, 1);
		}else{
			now.clear();
			y = Integer.parseInt(year);
			m = Integer.parseInt(month);
			now.set(y, m, 1);
		}
		String html = new ModelCalendar().createCalendar(y, m, now);
		return html;
	}
	
}
