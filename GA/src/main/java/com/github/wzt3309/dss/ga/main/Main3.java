package com.github.wzt3309.dss.ga.main;

import static com.github.wzt3309.dss.ga.constant.DeviceConstants.DEVICE_LEVEL_LOW;
import static com.github.wzt3309.dss.ga.constant.DeviceConstants.DEVICE_LEVEL_MID;
import static com.github.wzt3309.dss.ga.constant.TaskConstants.DEF_NEED_CHUNK;
import static com.github.wzt3309.dss.ga.constant.TaskConstants.DEF_NEED_WAITT;
import static com.github.wzt3309.dss.ga.constant.TaskConstants.TASK_ALL;

import java.util.ArrayList;
import java.util.List;

import com.github.wzt3309.dss.ga.algorithm.DockerCluFunc;
import com.github.wzt3309.dss.ga.algorithm.FitnessFunction;
import com.github.wzt3309.dss.ga.algorithm.GeneticAlgorithm;
import com.github.wzt3309.dss.ga.algorithm.SavgAlgroithm;
import com.github.wzt3309.dss.ga.algorithm.SpreadAlgorithm;
import com.github.wzt3309.dss.ga.device.Cpu;
import com.github.wzt3309.dss.ga.device.Disk;
import com.github.wzt3309.dss.ga.device.Host;
import com.github.wzt3309.dss.ga.device.Memory;
import com.github.wzt3309.dss.ga.device.Network;
import com.github.wzt3309.dss.ga.manager.Cluster;
import com.github.wzt3309.dss.ga.manager.Task;

public class Main3 {
public static void main(String[] args) {
		
		Host host1 =
				new Host(new Cpu(1,2.8F),
				new Memory(1000000),
				new Disk(),
				new Network(1),DEVICE_LEVEL_LOW);
		Host host2 = 
				new Host(new Cpu(2,2.8F), 
				new Memory(2000000), 
				new Disk(), 
				new Network(2),DEVICE_LEVEL_MID);
		Host host3 = 
				new Host(new Cpu(4,2.8F), 
				new Memory(4000000), 
				new Disk(), 
				new Network(4),DEVICE_LEVEL_MID);
		
		Cluster cluster = new Cluster(host1,host2,host3);
		
		Task task1 = new Task(1,2.8F,15.0F,1000,0,0,0,"high cpu");
		Task task2 = new Task(1,2.8F,1.0F,100000,0,0,0,"high mem");
		Task task3 = new Task(1,2.8F,3.0F,4000,DEF_NEED_WAITT,DEF_NEED_CHUNK,0,"high io");
		Task task4 = new Task(1,2.8F,1.0F,2000,0,0,0.1F,"high net");
		
		List<Task> tasks = new ArrayList<>();
		int[] taskNum = new int[]{6,6,6,6};
//		for(int i=0;i<24;i++){
//			int a = (int)(Math.random()*4);
//			if(taskNum[a]!=0){
//				switch(a){
//					case 0:tasks.add(task1);break;
//					case 1:tasks.add(task2);break;
//					case 2:tasks.add(task3);break;
//					case 3:tasks.add(task4);break;
//				}
//				taskNum[a] = taskNum[a]-1;
//			}else{
//				i--;
//			}
//		}
		for(int i=0;i<24;i++){
			int a = i/6;
			switch(a){
				case 0:tasks.add(task1);break;
				case 1:tasks.add(task2);break;
				case 2:tasks.add(task3);break;
				case 3:tasks.add(task4);break;
			}
		}
//		for(Task task:tasks){
//			System.out.println(task.getLev());
//		}
		
		int geneSize=tasks.size(),											//种群数量
				geneFraglen=5,												//个体基因有效片段长度
				geneDecMin=0,												//基因有效片段解码最小值
				geneDecMax=3;												//基因有效片段解码最大值
			FitnessFunction func=new DockerCluFunc(cluster,tasks,TASK_ALL);	//适应度计算函数	
			GeneticAlgorithm gal=											//遗传算法
					new GeneticAlgorithm(geneSize,geneFraglen,
							geneDecMin,geneDecMax,func);					
			gal.caculte();													//执行算法
			
			SpreadAlgorithm spread = new SpreadAlgorithm(cluster,24,func);
			spread.caculte();
			
			SavgAlgroithm savg = new SavgAlgroithm(cluster,TASK_ALL,tasks);
			savg.caculte();
	}
}
