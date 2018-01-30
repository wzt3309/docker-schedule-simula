package com.github.wzt3309.dss.ga.algorithm;

public interface FitnessFunction {

	public double fitness(int[] taskList);
	public boolean canBeParent(int[] taskList);
	public void pfitness(int[] sche);
}
