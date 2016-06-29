package com.staj.proje;

import com.staj.proje.config.Constants;
import com.staj.proje.config.DefaultProfileUtil;
import com.staj.proje.config.JHipsterProperties;


public class Node {
	private int index;
	private String title;
	private int year;
	private String director;
	public String value1;
	public String value2;
	
	public int getIndex() { return index; }
	public String getTitle() { return title; }
	public int getYear() { return year; }
	public String getDirector() { return director; }

	public void setIndex(int index) { this.index = index; }
	public void setTitle(String title) { this.title = title; }
	public void setYear(int year) { this.year = year; }
	public void setDirector(String director) { this.director = director; }

	public Node(String title, String director, int year, int index) {
		this.index = index;
		this.title = title;
		this.year = year;
		this.director = director;
	}
}
