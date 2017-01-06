package com.github.dockerschesimu.constant;

public class DeviceConstants {

	//-------------------------------cpu/core相关常量-------------------------------//
	public static final float DEFAULT_CORE_FREQUENCY=2.00F;		//默认核心主频2.00GHz
	public static final int DEFAULT_CPU_CORENUM=6;				//默认cpu核心数为6
	public static final boolean DEFAULT_CPU_HYPERTHREAD=true;	//默认cpu是超线程的
	public static final int DEFAULT_CPU_HYPERTHREADN=2;			//默认cpu是超线程数为2
	public static final String CPU_NICKNAME_SEQ
		="abcdefghijklmnopqrstuvwxyz";							//cpu别名生成序列
	public static final int CPU_NICKNAME_LENGTH=5;				//cpu别名长度
	
	//-------------------------------内存相关常量-------------------------------//
	public static final int DEFAULT_MEM_TOTAL=16000000;			//默认内存总容量为16G
	
	//-------------------------------硬盘相关常量-------------------------------//
	public static final int DEFAULT_SATA_RPM=7200;				//默认机械硬盘转速7200r/s
	public static final int DEFAULT_SATA_MTR=210;				//默认机械硬盘最大持续传输率210MB/s
	public static final int DEFAULT_CHUNK_SIZE=4;				//默认随机读写IO块大小
	public static final float DEFAULT_SATA_AAT=5F;				//默认机械硬盘平均选址时间5ms
	
	
	//-------------------------------标识符常量-------------------------------//
	public static final int IDENTIFI_DEFAULT=0x1000;			//未知设备
	public static final int IDENTIFI_CPU=0x1001;				//CPU标识符
	public static final int IDENTIFI_CORE=0x1002;				//核心标识符
	public static final int IDENTIFI_MEM=0x1003;				//内存标识符
	public static final int IDENTIFI_DISK=0x1004;				//硬盘标识符
	
	
}