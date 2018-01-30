package com.github.wzt3309.dss.ga.algorithm;

import java.util.List;

import com.github.wzt3309.dss.ga.manager.Cluster;
import com.github.wzt3309.dss.ga.manager.Task;
import com.github.wzt3309.dss.ga.tools.BaseLogger;

public class SavgAlgroithm {
	private Cluster cluster;
	private int type;
	List<Task> tasks;
	
	public SavgAlgroithm(Cluster cluster, int type,List<Task> tasks) {
		this.cluster=cluster;
		this.type=type;
		this.tasks=tasks;
	}
	public void caculte() {
		BaseLogger.INFO("===========================执行savg算法 开始===========================");
		for(int i=0;i<tasks.size();i++){
			int node = searchNode();
			int res = cluster.doTask(node, tasks.get(i), type);
			if(res!=0){
				BaseLogger.ERROR("------------ 失败 执行任务------------");
				cluster.pDoTaskError(node,i,res,tasks.get(i));
				continue;
			}			
		}
		printRes();
		BaseLogger.INFO("===========================执行savg算法 结束===========================");
	}
	public int searchNode(){		
		double min = load(0);
		int node = 0;
		int hostNum = cluster.getHostNum();
		for(int i=1;i<hostNum;i++){
			double tmp = load(i);
			if(tmp<min){
				min = tmp;
				node = i;
			}			
		}		
		return node;
	}
	public double load(int node){
		double[] allLoad = cluster.getAllLoad(node);
		double savg = (allLoad[0]+allLoad[1]+allLoad[3])/3;
		return savg;
	}
	public void printRes(){
		switch(type){
		case 0:
			cluster.monitor();
			break;
		case 1:
			cluster.mCpu();
			break;
		case 2:
			cluster.mMem();
			break;
		case 3:
			cluster.mDisk();
			break;
		case 4:
			cluster.mNet();
			break;
		}
		cluster.recover();
	}
}
