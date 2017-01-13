package com.github.dockerschesimu.manager;

import static com.github.dockerschesimu.constant.TaskConstants.*;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.tools.BaseMath;
import static com.github.dockerschesimu.tools.BaseLogger.*;
/**
 * 任务生成
 * @author wzt
 *
 */
public class TaskManager {

	public static Task simpleTask(){
		return new Task(DEF_NEED_TNUM,DEF_TEST_FREQ,DEF_TEST_CUSE
				,DEF_NEED_MEM,DEF_NEED_WAITT,DEF_NEED_CHUNK,DEF_NEED_NET,"default");
	}
	public static List<Task> simpleTasks(int size){
		List<Task> tasks=new ArrayList<>();
		for(int i=0;i<size;i++){
			tasks.add(simpleTask());
		}
		printTasks(tasks,0);
		return tasks;
	}
	/**
	 * 产生随机4个资源消耗等级的任务
	 * @return
	 */
	public static Task randomTask(){
		int cpul=randomResLev();		
		int meml=randomResLev();				
		int diskl=randomResLev();		
		int netl=randomResLev();
		
		
		return randomTask(cpul, meml, diskl, netl);
		
	}
	/**
	 * 产生规定资源消耗等级的任务
	 * @param cpul
	 * @param meml
	 * @param diskl
	 * @param netl
	 * @return
	 */
	public static Task randomTask(int cpul,int meml,int diskl,int netl){
		int tnum=randomTnum(cpul);
		float freq=randomFreq(cpul);
		float cuse=randomCuse(cpul);
		int mem=randomMem(meml);
		int waitt=randomWait(4-diskl);	//资源消耗等级低，需要产生更大的waitt
		int chunk=randomChunk(4-diskl);
		int net=randomNet(netl);
		String lev=cpul+","+meml+","+diskl+","+netl;
		return new Task(tnum,freq,cuse,mem,waitt,chunk,net,lev);
	}
	public static List<Task> randomTasks(int size){
		List<Task> tasks=new ArrayList<>();
		for(int i=0;i<size;i++){
			tasks.add(randomTask());
		}
		printTasks(tasks,0);
		return tasks;
	}
	/**
	 * levs size*4维数组 代表每个task的4个资源等级,第一维度是size第二维度是4
	 * @param size
	 * @param levs
	 * @return
	 */
	public static List<Task> randomTasks(int size,int[][] levs){
		List<Task> tasks=new ArrayList<>();
		for(int i=0;i<size;i++){
			if(levs!=null&&levs[0].length==4){
				if(i<levs.length){
					tasks.add(randomTask(levs[i][0], levs[i][1], levs[i][2], levs[i][3]));
				}else{
					tasks.add(randomTask());
				}
			}else{
				tasks.add(randomTask());
			}
		}
		printTasks(tasks,0);
		return tasks;
	}
	
	/**
	 * 产生4种资源，平均的，有一种资源是high其他都是low的任务
	 * @param size
	 * @return
	 */
	public static List<Task> randomTasksFit(int size){
		return randomTasks(size,levs(size));
	}
	/**
	 * 产生4种资源，任意比a:b:c:d，有一种资源是high其他都是low的任务
	 * a cpu为high的比例
	 * b mem为high的比例
	 * c disk为high的比例
	 * d net为high的比例
	 * @param size
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static List<Task> randomTasksFit(int size,double a,double b,double c,double d){
		return randomTasks(size,levs(size,a,b,c,d));
	}
	/**
	 * 单一资源任务
	 * @param type
	 * @param lev
	 * @return
	 */
	public static Task oneResTask(int type,int lev){
		Task task=new Task();
		String slev=null;
		switch(type){
		case 1:
			slev=lev+",0,0,0";
			task.setNeedThreadNum(randomTnum(lev));
			task.setTestFrequence(randomFreq(lev));
			task.setTestCoreUse(randomCuse(lev));
			break;
		case 2:
			slev="0,"+lev+",0,0";
			task.setNeedMemory(randomMem(lev));
			break;
		case 3:
			slev="0,0,"+lev+",0";
			task.setNeedWaitt(randomWait(lev));
			task.setNeedChunk(randomChunk(lev));
			break;
		case 4:
			slev="0,0,0,"+lev;
			task.setNeedNet(randomNet(lev));
			break;
		}
		task.setLev(slev);
		return task;
	}
	/**
	 * 一组单一资源任务 资源消耗情况 高=中=低 
	 * @param size
	 * @param type
	 * @return
	 */
	public static List<Task> oneResTasks(int size,int type){
		List<Task> tasks=new ArrayList<>();
		for(int i=0;i<size;i++){
			Task task=null;
			if(i<size/3){
				task=oneResTask(type,RES_LEVEL_LOW);
			}else if(i>=size/3&&i<size*2/3){
				task=oneResTask(type,RES_LEVEL_MID);
			}else{
				task=oneResTask(type,RES_LEVEL_HIGH);
			}
			tasks.add(task);
		}
		printTasks(tasks,type);
		return tasks;
	}
	/**
	 * 一组单一资源任务 资源消耗情况为levs记录，levs每项为1 2 3
	 * @param size
	 * @param type
	 * @param levs
	 * @return
	 */
	public static List<Task> oneResTasks(int size,int type,int[] levs){
		List<Task> tasks=new ArrayList<>();
		for(int i=0;i<size;i++){
			Task task=null;
			if(i<levs.length){
				task=oneResTask(type,levs[i]);
			}else{
				task=oneResTask(type,RES_LEVEL_LOW);
			}
			tasks.add(task);
		}
		printTasks(tasks,type);
		return tasks;
	}
	
	public static void printTasks(List<Task> tasks,int type){
		INFO("------------------打印Task列表 开始------------------");
		for(Task task:tasks){
			switch(type){
			case 0:
				INFO(task);
				break;
			case 1:
				INFO(task.pCpu());
				break;
			case 2:
				INFO(task.pMem());
				break;
			case 3:
				INFO(task.pDisk());
				break;
			case 4:
				INFO(task.pNet());
				break;
			}
		}
		INFO("------------------打印Task列表 结束------------------\n");
	}
	
	/**
	 * 产生平均比例，高icpu任务数=高imem数=高idisk数=高inet数
	 * @param size
	 * @return
	 */
	private static int[][] levs(int size){
		return levs(size,1,1,1,1);
	}
	/**
	 * 产生固定比例，
	 * 高icpu任务数:高imem数:高idisk数:高inet数=a:b:c:d
	 * @param size
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static int[][] levs(int size,double a,double b,double c,double d){		
		int[][] levs=new int[size][4];
		double num=a+b+c+d;
		a=a*size/num+0;
		b=b*size/num+a;
		c=c*size/num+b;
		d=d*size/num+c;
		
		for(int i=0;i<size;i++){			
			if(i<a){
				levs[i][0]=RES_LEVEL_HIGH;
				levs[i][1]=RES_LEVEL_LOW;
				levs[i][2]=RES_LEVEL_LOW;
				levs[i][3]=RES_LEVEL_LOW;
			}else if(i>=a&&i<b){
				levs[i][0]=RES_LEVEL_LOW;
				levs[i][1]=RES_LEVEL_HIGH;
				levs[i][2]=RES_LEVEL_LOW;
				levs[i][3]=RES_LEVEL_LOW;
			}else if(i>=b&&i<c){
				levs[i][0]=RES_LEVEL_LOW;
				levs[i][1]=RES_LEVEL_LOW;
				levs[i][2]=RES_LEVEL_HIGH;
				levs[i][3]=RES_LEVEL_LOW;
			}else{
				levs[i][0]=RES_LEVEL_LOW;
				levs[i][1]=RES_LEVEL_LOW;
				levs[i][2]=RES_LEVEL_LOW;
				levs[i][3]=RES_LEVEL_HIGH;
			}			
		}
		return levs;
	}
	
	/**
	 * 随机产生资源消耗的等级
	 * @return
	 */
	private static int randomResLev(){
		int lev=(int)(Math.random()*3+1);
		switch(lev){
		case 1:return RES_LEVEL_LOW;
		case 2:return RES_LEVEL_MID;
		case 3:return RES_LEVEL_HIGH;
		default:return -1;
		}
	}
	/**
	 * 根据资源消耗的等级产生 任务需要的线程数
	 * @param seed
	 * @return
	 */
	private static int randomTnum(int seed){
		int n=BaseMath.random(MIN_NEED_TNUM,MAX_NEED_TNUM,seed);
		return (int)Math.pow(2, n);
		
	}
	/**
	 * 根据资源消耗的等级产生 任务测试时cpu主频
	 * @param seed
	 * @return
	 */
	private static float randomFreq(int seed){
		float freq=BaseMath.random(MIN_TEST_FREQ,MAX_TEST_FREQ,seed);
		return freq;
	}
	/**
	 * 根据资源消耗的等级产生 任务在测试cpu主频下单核资源消耗情况
	 * @param seed
	 * @return
	 */
	private static float randomCuse(int seed){
		float cuse=BaseMath.random(MIN_TEST_CUSE,MAX_TEST_CUSE,seed);
		return cuse;
	}
	/**
	 * 根据资源消耗的等级产生 任务需要的内存数
	 * @param seed
	 * @return
	 */
	private static int randomMem(int seed){
		int mem=BaseMath.random(MIN_NEED_MEM,MAX_NEED_MEM,seed);
		return mem;
	}
	/**
	 * 根据资源消耗的等级产生 任务需要的io等待时间
	 * @param seed
	 * @return
	 */
	private static int randomWait(int seed){
		int wait=BaseMath.random(MIN_NEED_WAITT,MAX_NEED_WAITT,seed);
		return wait;
	}
	/**
	 * 根据资源消耗的等级产生 任务需要的单次io块大小
	 * @param seed
	 * @return
	 */
	private static int randomChunk(int seed){
		int chunk=BaseMath.random(MIN_NEED_CHUNK,MAX_NEED_CHUNK,seed);
		return chunk;
	}
	/**
	 * 根据资源消耗的等级产生 任务需要的网络带宽
	 * @param seed
	 * @return
	 */
	private static int randomNet(int seed){
		int net=BaseMath.random(MIN_NEED_NET,MAX_NEED_NET,seed);
		return net;
	}
}
