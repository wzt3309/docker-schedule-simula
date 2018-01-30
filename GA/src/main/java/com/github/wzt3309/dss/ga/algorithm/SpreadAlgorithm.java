package com.github.wzt3309.dss.ga.algorithm;

import java.util.Arrays;
import java.util.List;

import com.github.wzt3309.dss.ga.device.Host;
import com.github.wzt3309.dss.ga.manager.Cluster;
import com.github.wzt3309.dss.ga.tools.BaseLogger;

public class SpreadAlgorithm {
	private Cluster cluster;
	private int taskNum;
	private FitnessFunction func;		//适应度计算函数
	
	public SpreadAlgorithm(Cluster cluster, int taskNum, FitnessFunction func) {
		this.cluster=cluster;
		this.taskNum=taskNum;
		this.func=func;
	}
	public void caculte() {
		BaseLogger.INFO("===========================执行spread算法 开始===========================");
		List<Host> host = cluster.getHosts();
		int hostLen = host.size();
		int[] cores = new int[hostLen];
		int coreToal = 0;		
		int[] sche = new int[taskNum];
		
		for(int i=0;i<hostLen;i++){
			cores[i] = host.get(i).getCpu().getCoreNum();
			coreToal += cores[i];
		}
		for(int i=0;i<hostLen;i++){
			cores[i] =  Math.round((cores[i]*1.0F/coreToal)*taskNum);
		}
		
		int nodeNum = 0;
		for(int i=0;i<taskNum;i++){			 
			if(cores[nodeNum]!=0){
				sche[i] = nodeNum;
				cores[nodeNum] = cores[nodeNum]-1;
			}else{
				nodeNum++;
				i--;
			}
		}
		
		StringBuffer bf = new StringBuffer(); 
		bf.append("任务分配列表[ ");
		for(int sch:sche){
			bf.append(sch+" ");
		}
		bf.append(" ]");
		BaseLogger.INFO(bf);
		
		func.pfitness(sche);
		BaseLogger.INFO("===========================执行spread算法 结束===========================");
	}
}
