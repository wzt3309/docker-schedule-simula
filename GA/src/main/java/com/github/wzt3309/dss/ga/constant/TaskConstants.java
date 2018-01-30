package com.github.wzt3309.dss.ga.constant;

public class TaskConstants {

	public static final int DEF_NEED_TNUM=2;
	public static final int MAX_NEED_TNUM=2;	//2^2
	public static final int MIN_NEED_TNUM=0;	//2^0
	
	public static final float DEF_TEST_FREQ=2.1F;
	public static final float MAX_TEST_FREQ=2.5F;
	public static final float MIN_TEST_FREQ=1.8F;
	
	public static final float DEF_TEST_CUSE=5;
	public static final float MAX_TEST_CUSE=20.0F;
	public static final float MIN_TEST_CUSE=1.0F;
	
	public static final int DEF_NEED_MEM=128000;
	public static final int MAX_NEED_MEM=500000;
	public static final int MIN_NEED_MEM=10000;
	
	public static final int DEF_NEED_WAITT=300;
	public static final int MAX_NEED_WAITT=900;
	public static final int MIN_NEED_WAITT=100;
	
	public static final int DEF_NEED_CHUNK=400;
	public static final int MAX_NEED_CHUNK=900;
	public static final int MIN_NEED_CHUNK=100;
	
	public static final int DEF_NEED_NET=4;
	public static final int MAX_NEED_NET=20;
	public static final int MIN_NEED_NET=2;
	
	//-------------------------------其他常量-------------------------------//
	public static final int RES_LEVEL_LOW=1;					//资源使用等级 低
	public static final int RES_LEVEL_MID=2;					//资源使用等级 中
	public static final int RES_LEVEL_HIGH=3;					//资源使用等级 高
	public static final int TASK_ALL=0;							//完整任务
	public static final int TASK_CPU=1;							//cpu任务
	public static final int TASK_MEM=2;							//mem任务
	public static final int TASK_DISK=3;						//disk任务
	public static final int TASK_NET=4;							//net任务
	
	
	
	
	
	
	
	
}
