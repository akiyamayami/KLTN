package com.tlcn.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ModelCalendar{

	private String[] cal_days_labels = { "CN", "T2", "T3", "T4", "T5", "T6", "T7" };
	private String[] cal_months_labels = { "January", "February", "March", "April", "May", "June", "July", 
											"August", "September", "October", "November", "December" };
	private int[] cal_days_in_month = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private int startingDay;
	private int monthLength;
	private int day = 1;
	private String month_year_name;
	
	
	public String createCalendar(int year,int month,Calendar now){
		String html = "";
		startingDay = now.get(Calendar.DAY_OF_WEEK) - 1;
		monthLength = cal_days_in_month[month];
		month_year_name =  cal_months_labels[month] + " " + year;
		System.out.println(now.getTime());
		if (month == 1) {
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				monthLength = 29;
			}
		}
		
		html += "<div class='calendar-controls'>"
				+ "<a class='arrow_link previous' href='?m="+ getMYPre(month,year).get(0) + "&y="+ getMYPre(month,year).get(1) +"' title='Tháng trước'>"
				+ "<span class='arrow '>◄</span>"
				+ "<span class='accesshide '>"
				+ "&nbsp;"
				+ "<span class='arrow_text'></span>"
				+ "</span>"
				+ "</a>"
				+ "<span class='hide'> | </span>"
				+ "<span class='current'>"
				+ "<a title='Tháng này' href='#'>"
				+ month_year_name
				+ "</a>"
				+ "</span>"
				+ "<span class='hide'> | </span>"
				+ "<a class='arrow_link next' style='float:right;' href='?m="+ getMYNext(month,year).get(0) + "&y="+ getMYNext(month,year).get(1) +"' title='Tháng tới'>"
				+ "<span class='accesshide '>"
				+ "<span class='arrow_text'></span> &nbsp;"
				+ "</span>"
				+ "<span class='arrow'>►</span>"
				+ "</a></div>";
		html += "<table class='calendartable'>"
				+ "<tbody>"
				+ "<tr class='weekdays'>";
		for (int i = 0; i <= 6; i++) {
            html += "<th scope='col'>" + cal_days_labels[i] + "</th>";
		}
		html += "</tr><tr>";
		for(int i = 0 ; i < 9 ; i++){
			for(int j = 0 ; j <= 6 ; j++){
				if (day <= monthLength && (i > 0 || j >= startingDay)) {
                    html += "<td class='day'>"+ day +"</td>";
                    day++;
				}
				else{
					html += "<td class='dayblank'>&nbsp;</td>";
				}
			}
			if (day > monthLength) {
                break;
			} else {
                html += "</tr><tr>";
			}
		}
		html += "</tr></tbody></table>";
		return html;
	}
	
	public List<String> getMYPre(int m, int y){
		List<String> x = new ArrayList<>();
		if(m == 0){
			x.add("12");
			x.add((y-1)+"");
		}else
		{
			x.add((m-1)+"");
			x.add(y + "");
		}
		return x;
	}
	
	public List<String> getMYNext(int m, int y){
		List<String> x = new ArrayList<>();
		if(m == 11){
			x.add("0");
			x.add(y+1+"");
		}else
		{
			x.add((m+1)+"");
			x.add(y + "");
		}
		return x;
	}
}
