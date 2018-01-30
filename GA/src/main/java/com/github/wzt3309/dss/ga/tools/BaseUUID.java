package com.github.wzt3309.dss.ga.tools;

import java.util.Date;
import java.util.Random;

import com.github.wzt3309.dss.ga.constant.DeviceConstants;

public class BaseUUID {

	private static Date date=new Date();
	private static StringBuffer buf=new StringBuffer();	
	/**
	 * 获取各种模拟设备或者组件的的uuid
	 * 不在设备标识符列表的，设备标识符为默认，设备标号为0
	 * @param identity
	 * @return 设备标识符+日期+设备标号
	 */
	public static synchronized long uuid(int identity){
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str=String.format("%2$04x%1$tY%1$tm%1$td%1$tk%1$tM%1$tS", date,identity);
		return Long.parseLong(str);
		
	}
	public static synchronized String randomstr(int length){
		StringBuffer br=new StringBuffer();
		Random rand=new Random(System.currentTimeMillis());
		for(int i=0;i<length;i++)
			br.append(DeviceConstants.CPU_NICKNAME_SEQ.charAt(
					rand.nextInt(DeviceConstants.CPU_NICKNAME_SEQ.length())));
		return br.toString();
	}
}
