package com.github.dockerschesimu.algorithm;

import static com.github.dockerschesimu.constant.TaskConstants.TASK_CPU;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.github.dockerschesimu.manager.Cluster;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.manager.TaskManager;

public class SpreadAlgorithmTest {

	@Test
	public void test() {
		int nodeNum=4,													//docker集群规模
			taskNum=10;													//需要并发分配的任务数量
		Cluster cluster=new Cluster(nodeNum);							//创建、初始化 模拟集群
		List<Task> tasks=TaskManager.oneResTasks(taskNum, TASK_CPU);	//随机创建任务
		FitnessFunction func=new DockerCluFunc(cluster,tasks,TASK_CPU);	//适应度计算函数	
		
		SpreadAlgorithm spread = new SpreadAlgorithm(cluster,taskNum,func);
		spread.caculte();
	}

}
