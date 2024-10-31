package com.sps.ofbiz.translator.utils;

import java.util.Random;

public class StyleGenUtil {

	
	public static String getRandomStyle() {
		Random rand = new Random(System.currentTimeMillis());
		int red = rand.nextInt(255);
		int green = rand.nextInt(255);
		int blue = rand.nextInt(255);
		
		return String.format("color:rgb(%d,%d,%d);", red,green,blue);
	}
}
