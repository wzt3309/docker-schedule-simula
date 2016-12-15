package com.github.dockerschesimu.constant;

public class Device {

	//-------------------------------cpu相关常量-------------------------------//
	public static final float DEFAULT_CORE_FREQUENCY=2.00F;		//默认核心主频2.00GHz
	public static final int DEFAULT_CPU_CORENUM=6;				//默认cpu核心数为6
	public static final boolean DEFAULT_CPU_HYPERTHREAD=true;	//默认cpu是超线程的
	public static final int DEFAULT_CPU_HYPERTHREADN=2;			//默认cpu是超线程数为2
	public static final String CPU_NICKNAME_SEQ
		="abcdefghijklmnopqrstuvwxyz";							//cpu别名生成序列
	public static final int CPU_NICKNAME_LENGTH=5;				//cpu别名长度
	
	//-------------------------------标识符常量-------------------------------//
	public static final int IDENTIFI_DEFAULT=0x1000;
	public static final int IDENTIFI_CPU=0x1001;
	public static final int IDENTIFI_CORE=0x1002;
	
	
	
}