package com.github.dockerschesimu.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneticAlgorithm {

	private List<Chromosome> population;//种群，多个任务分配列表
	private int popSize=100;			//种群数量
	private int geneSize;				//基因中有效片段的个数，需要分配的任务数目
	private int geneFraglen;			//基因有效片段的长度，以多长的二进制来标识被分配任务的容器序号
	private int geneDecMin;				//基因有效片段解码时，结果的最小范围
	private int geneDecMax;				//基因有效片段解码时，结果的最大范围
	private int maxIterNum=20;			//最大迭代次数
	private double mutationRate=0.01;	//基因变异概率
	private int maxMutationNum=3;		//最大变异步长
	
	private int generation=1;			//当前遗传到第几代
	
	private double bestFitness;			//当前最好适应度，当前docker容器集群最佳负载结果
	private double worstFitness;		//当前最差适应度，当前docker容器集群最差负载结果
	private double totalFitness;		//当前总适应度，当前所有任务分配后docker集群负载结果之和
	private double averageFitness;		//当前平均适应度，当前所有任务分配后docker集群负载结果平均值
	
	private int[] hBestTaskList;		//记录历史种群中最好的个体，最好的任务分配列表（索引代表任务序号、值代表被分配任务的节点序号）
	private double hBestFitness;		//记录历史种群中最好的适应度，历史上最好的docker容器集群负载结果
	private int geneI;					//historyBest所在的代数
	
	private FitnessFunction func;		//适应度计算函数
	public GeneticAlgorithm(int geneSize,int geneFraglen
			,int geneDecMin,int geneDecMax,FitnessFunction func){
		this.geneSize=geneSize;
		this.geneFraglen=geneFraglen;
		this.geneDecMin=geneDecMin;
		this.geneDecMax=geneDecMax;
		this.func=func;
	}
	/**
	 * 执行算法
	 */
	public void caculte(){
		//初始化种群
		generation=1;
		init();
		while(generation<maxIterNum){
			System.out.println("---------------第 "+generation+" 代开始---------------");
			evolve();
			print();
			generation++;
			System.out.println("---------------第 "+generation+" 代结束---------------");
		}
	}
	/**
	 * 打印每次迭代结果
	 */
	private void print(){
		System.out.println("--------------------------------");  
        System.out.println("the generation is:" + generation);  
        System.out.println("the best y is:" + bestFitness);  
        System.out.println("the worst fitness is:" + worstFitness);  
        System.out.println("the average fitness is:" + averageFitness);  
        System.out.println("the total fitness is:" + totalFitness);
        System.out.println("geneI:" + geneI + 
        		"\nhBestTaskList:" + Arrays.toString(hBestTaskList) + 
        		"\nhBestFitness:" + hBestFitness);
	}
	/**
	 * 初始化种群
	 */
	private void init(){
		System.out.println("---------------种群初始化开始---------------");
		population=new ArrayList<>();
		for(int i=0;i<popSize;i++){
			Chromosome chro
				=new Chromosome(geneSize,geneFraglen);
			population.add(chro);
		}
		caculteFitness();
		print();
		System.out.println("---------------种群初始化结束---------------");
	}	
	/**
	 * 轮盘赌算法，获取可以遗传到下一代的染色体
	 * @return
	 */
	private Chromosome getParentChromosome(){
		double slice=Math.random()*totalFitness;
		double sum=0;
		for(Chromosome chro:population){
			sum+=chro.getFitness();
			if(sum>slice && canBeParent(chro)){
				return chro;
			}				
		}
		return null;
	}
	/**
	 * 种群遗传，产生新一代种群
	 */
	private void evolve(){
		List<Chromosome> childPopulation=new ArrayList<>();
		//产生下一代种群
		while(childPopulation.size()<popSize){
			Chromosome p1=getParentChromosome();
			Chromosome p2=getParentChromosome();
			List<Chromosome> children=Chromosome.genetic(p1, p2);
			if(children!=null){
				for(Chromosome child:children){
					childPopulation.add(child);
				}
			}
		}
		//新旧种群交替
		List<Chromosome> t=population;
		population=childPopulation;
		t.clear();
		t=null;
		
		//基因突变
		mutation();
		//计算新种群适应度
		caculteFitness();
	}
	/**
	 * 基因突变
	 */
	private void mutation(){
		for(Chromosome chro:population){
			if(Math.random()<mutationRate){
				int mutationNum=(int)(Math.random()*maxMutationNum);
				chro.mutation(mutationNum);
			}
		}
	}	
	/**
	 * 个体基因是否能够产生子代
	 * @param chro
	 * @return
	 */
	private boolean canBeParent(Chromosome chro){
		return func.canBeParent(chro.decodeGene(geneDecMin, geneDecMax))
				&&firstIsNoWorst(chro.getFitness(),averageFitness);
	}
	/**
	 * 计算种群适应度
	 */
	private void caculteFitness(){		
		setChromosomeFitness(population.get(0),func);
		bestFitness=population.get(0).getFitness();
		worstFitness=population.get(0).getFitness();
		totalFitness=0;
		//遗传第一代时，初始化历史最好种群与适应度
		if(generation==1){
			hBestTaskList=population.get(0).decodeGene(geneDecMin, geneDecMax);
			hBestFitness=bestFitness;
		}
		for(Chromosome chro:population){
			setChromosomeFitness(chro,func);
			if(firstIsBetter(chro.getFitness(),bestFitness)){
				bestFitness=chro.getFitness();
				if(firstIsBetter(bestFitness,hBestFitness)){
					hBestTaskList=chro.decodeGene(geneDecMin, geneDecMax);
					hBestFitness=bestFitness;
					geneI=generation;
				}
			}
			if(firstIsWorst(chro.getFitness(),worstFitness)){
				worstFitness=chro.getFitness();
			}
			totalFitness+=chro.getFitness();
		}
		averageFitness=totalFitness/popSize;
		//由于精度问题导致平均值大于最好值，将平均值设置为最好值
		averageFitness=firstIsBetter(averageFitness,bestFitness)?bestFitness:averageFitness;
	}
	/**
	 * 计算个体适应度
	 * @param chro
	 * @param func	适应度计算函数
	 */
	private void setChromosomeFitness(Chromosome chro,FitnessFunction func){
		if(chro==null){
			return ;
		}
		int[] taskList=chro.decodeGene(geneDecMin, geneDecMax);
		double fitness=func.fitness(taskList);
		chro.setFitness(fitness);
		
	}
	/**
	 * 获取计算适应度的函数
	 * @return
	 */
	public FitnessFunction getFitnessFunc(){
		return func;
	}
	/**
	 * f1适应度 > f2
	 * @param f1
	 * @param f2
	 * @return
	 */
	private boolean firstIsBetter(double f1,double f2){
		//此算法内，负载越小越好
		return f1<f2;
	}
	/**
	 * f1适应度 > f2
	 * @param f1
	 * @param f2
	 * @return
	 */
	private boolean firstIsWorst(double f1,double f2){
		//此算法内，负载越大越差
		return f1>f2;
	}
	/**
	 * f1适应度 >= f2
	 * @param f1
	 * @param f2
	 * @return
	 */
	private boolean firstIsNoWorst(double f1,double f2){
		//此算法内，负载越小越好
		return f1<=f2;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
