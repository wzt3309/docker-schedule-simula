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
	public static final int MAX_CORE_NUM=4;						//cpu最大核心数2^6
	public static final int MIN_CORE_NUM=1;						//cpu最小核心数
	public static final float MAX_CORE_FRE=3.5F;				//cpu最高主频
	public static final float MIN_CORE_FRE=1.5F;				//cpu最低主频
	
	//-------------------------------内存相关常量-------------------------------//
	public static final int DEFAULT_MEM_TOTAL=16000000;			//默认内存总容量为16G
	public static final int MAX_MEM_TOTAL=7;					//最大内存为2^7G
	public static final int MIN_MEM_TOTAL=1;					//最小内存为2^1G
	
	//-------------------------------硬盘相关常量-------------------------------//
	public static final int DEFAULT_SATA_RPM=5600;				//默认机械硬盘转速5600r/s
	public static final int DEFAULT_SATA_MTR=107;				//默认机械硬盘最大持续传输率107MB/s
	public static final int DEFAULT_CHUNK_SIZE=4;				//默认随机读写IO块大小
	public static final float DEFAULT_SATA_AAT=5F;				//默认机械硬盘平均选址时间5ms
	public static final int MAX_SATA_RPM=15;					//最大机械硬盘rpm15*1000
	public static final int MIN_SATA_RPM=3;					   	//最小机械硬盘rpm3*1000
	public static final int MAX_SATA_MTR=300;					//最大MTR
	public static final int MIN_SATA_MTR=100;					//最小MTR
	public static final float MAX_SATA_AAT=10;					//最大AAT
	public static final float MIN_SATA_AAT=1;					//最小AAT
	
	//-------------------------------网络相关常量-------------------------------//
	public static final int DEFAULT_NET_TOTAL=100;				//默认网络带宽100Mbps
	public static final int MAX_NET_TOTAL=100;					//最大内存为100Mbps
	public static final int MIN_NET_TOTAL=4;					//最小内存为1G
	
	//-------------------------------标识符常量-------------------------------//
	public static final int IDENTIFI_DEFAULT=0x1000;			//未知设备
	public static final int IDENTIFI_CPU=0x1001;				//CPU标识符
	public static final int IDENTIFI_CORE=0x1002;				//核心标识符
	public static final int IDENTIFI_MEM=0x1003;				//内存标识符
	public static final int IDENTIFI_DISK=0x1004;				//硬盘标识符
	
	//-------------------------------其他常量-------------------------------//
	public static final int DEVICE_LEVEL_LOW=1;					//硬件等级 低
	public static final int DEVICE_LEVEL_MID=2;					//硬件等级 中
	public static final int DEVICE_LEVEL_HIGH=3;				//硬件等级 高
}