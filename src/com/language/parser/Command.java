package com.language.parser;

public class Command {
	
	public int x, y;
	
	public String id;
	public String action;
	
	public int dirpos;
	
	Command(int px, int py, String pID, String paction){
		
		x = px;
		y = py;
		id = pID;
		action = paction;
		
	}
	

	Command(int dir, String pID, String paction)
	{
		dirpos = dir;
		id = pID;
		action = paction;
	}
}
