package com.github.dockerschesimu.main;

import java.util.List;

import com.github.dockerschesimu.algorithm.DockerCluFunc;
import com.github.dockerschesimu.algorithm.FitnessFunction;
import com.github.dockerschesimu.algorithm.GeneticAlgorithm;
import com.github.dockerschesimu.manager.Cluster;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.manager.TaskManager;

public class Main {

	public static void main(String[] args) {
		
		Cluster cluster=new Cluster(4);
		List<Task> tasks=TaskManager.oneResTasks(10, 1);
		
		
		FitnessFunction func=new DockerCluFunc(cluster,tasks,1);
		
		GeneticAlgorithm gal=new GeneticAlgorithm(tasks.size(),5,0,3,func);
		gal.caculte();
	}

}
