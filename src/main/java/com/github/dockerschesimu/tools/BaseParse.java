package com.github.dockerschesimu.tools;

public class BaseParse {

	public static int boolsToInt(boolean[] bools){
		if(bools==null)
			return -1;
		int num=0;
		for(boolean bool:bools){
			num<<=1;
			if(bool)
				num+=1;
		}
		return num;
	}
}
