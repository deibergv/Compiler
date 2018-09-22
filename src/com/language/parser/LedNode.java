package com.language.parser;


public class LedNode {

	public int heightValue;
	public boolean objective = false;
	public int position;
	
	public int heightLed;
	public int playerLed;
	public int objLed;
	
	
	
	LedNode(int pos, int pheight, int pheightLed, int pplayerLed, int pobjLed){
		
		heightValue = pheight;
		position = pos;
		
		heightLed = pheightLed;
		playerLed = pplayerLed;
		objLed = pobjLed;
	}
	
	
	public void setHeight(int pheight){
		
		heightValue = pheight;
		
	}
	
	public int getHeight() {
		return heightValue;
	}
	
	
	public void setObjective(){
		
		objective = true;
		
	}
	
	public void clearObjective(){
		objective = false;
	}
	
	
	public int getHeightLed(){
		return heightLed;
	}
	
	public int getPlayerLed(){
		return playerLed;
	}
	
	public int getObjLed(){
		return objLed;
	}
	
	
}
