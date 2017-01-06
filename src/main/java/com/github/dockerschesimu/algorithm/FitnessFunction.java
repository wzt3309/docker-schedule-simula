package com.github.dockerschesimu.algorithm;

public interface FitnessFunction {

	public double fitness(int[] taskList);
	public boolean canBeParent(int[] taskList);
}
