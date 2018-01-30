package com.github.wzt3309.dss.ga.main;

import java.util.List;

import com.github.wzt3309.dss.ga.algorithm.DockerCluFunc;
import com.github.wzt3309.dss.ga.algorithm.FitnessFunction;
import com.github.wzt3309.dss.ga.algorithm.GeneticAlgorithm;
import com.github.wzt3309.dss.ga.manager.Cluster;
import com.github.wzt3309.dss.ga.manager.Task;
import com.github.wzt3309.dss.ga.manager.TaskManager;
import static com.github.wzt3309.dss.ga.constant.TaskConstants.*;

/**
 * docker集群调度算法主程序
 * @author wzt
 *
 */
public class Main {
	
	public static void main(String[] args) {
		int nodeNum=4,													//docker集群规模
			taskNum=10;													//需要并发分配的任务数量
		Cluster cluster=new Cluster(nodeNum);							//创建、初始化 模拟集群
		List<Task> tasks=TaskManager.oneResTasks(taskNum, TASK_CPU);	//随机创建任务
		
		int geneSize=tasks.size(),										//种群数量
			geneFraglen=5,												//个体基因有效片段长度
			geneDecMin=0,												//基因有效片段解码最小值
			geneDecMax=nodeNum;											//基因有效片段解码最大值
		FitnessFunction func=new DockerCluFunc(cluster,tasks,TASK_CPU);	//适应度计算函数	
		GeneticAlgorithm gal=											//遗传算法
				new GeneticAlgorithm(geneSize,geneFraglen,
						geneDecMin,geneDecMax,func);					
		gal.caculte();													//执行算法
		
		
		/**
		 * ouput
		 * 控制台输出
		 * 日志文件记录（本程序使用绝对路径）
		 * 要改变日志位置，需要修改/src/main/resources/log4j.properties 配置
		 * /home/wzt/git/docker-schedule-simula/logs/debug_{MM-dd HH-mm-ss}.log
		 */
	}

}
