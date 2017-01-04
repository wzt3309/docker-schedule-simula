package com.github.dockerschesimu.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.tools.BaseParse;

public class Chromosome {
	private boolean[] gene;	//基因序列
	private int fraglen;	//基因序列中每个有意义片段的长度
	private double fitness;	//拥有此基因个体的适应度
	
	public Chromosome(){
		
	}
//	/**
//	 * 根据最大最小值随机初始化基因序列
//	 * @param size	基因中有效片段的数目
//	 * @param min	基因有效片段最小值
//	 * @param max   基因有效片段最大值
//	 */
//	public Chromosome(int size,int min,int max){
//		if(size<=0
//				||max<min
//				||max<=0)
//			return;
//		this.fraglen=(int)(Math.log(max)/Math.log(2))+1;
//		int initlen=size*fraglen;
//		initGeneSize(initlen);
//		
//		for(int i=0;i<size;i++){
//			initGeneFrag(i,fraglen,min,max);
//		}
//	}
//	private void initGeneFrag(int i,int fraglen,int min,int max){
//		i*=fraglen;
//		int num=((int)(Math.random()*max))%max+min;		
//		char[] binary=Integer.toBinaryString(num).toCharArray();			
//		int diff=fraglen-binary.length;
//		if(diff<0)
//			return ;
//		while(diff>0){
//			gene[i++]=false;
//			diff--;
//		}
//		for(int j=0;j<binary.length;j++){
//			gene[i++]=binary[j]=='1';
//		}
//	}
	
	/**
	 * 随机初始化个体基因序列
	 * @param size		基因序列中有效片段的个数
	 * @param fraglen	基因序列中每个有效片段的长度
	 */
	public Chromosome(int size,int fraglen){
		if(size<=0||fraglen<=0)
			return;
		this.fraglen=fraglen;
		int initlen=size*fraglen;
		initGeneSize(initlen);
		for(int i=0;i<initlen;i++)
			gene[i]=Math.random()>=0.5;
	}
	private void initGeneSize(int size){
		if(size<=0)
			return;
		gene=new boolean[size];
	}
	/**
	 * 按固定值初始化个体基因序列
	 * @param nums		基因序列中每个有效片段代表的十进制数
	 * @param fraglen	基因序列中每个有效片段的长度
	 */
	public Chromosome(int[] nums,int fraglen){
		if(nums==null||fraglen<=0)
			return;
		this.fraglen=fraglen;
		initGene(nums,fraglen);
	}
	private void initGene(int[] nums,int fraglen){
		if(nums==null||fraglen<=0)
			return;
		gene=new boolean[nums.length*fraglen];
		int i=0;
		for(int num:nums){
			char[] binary=Integer.toBinaryString(num).toCharArray();			
			int diff=fraglen-binary.length;
			if(diff<0)
				return;
			while(diff>0){
				gene[i++]=false;
				diff--;
			}
			for(int j=0;j<binary.length;j++){
					gene[i++]=binary[j]=='1';
			}
				
		}
	}
	/**
	 * 种群个体基因克隆
	 * @param c
	 * @return
	 */
	public static Chromosome clone(final Chromosome c){
		if(c==null||c.gene==null)
			return null;
		Chromosome copy=new Chromosome();
		copy.fraglen=c.fraglen;
		copy.initGeneSize(c.gene.length);
		for(int i=0;i<c.gene.length;i++)
			copy.gene[i]=c.gene[i];
		return copy;
	}
	/**
	 * 父母双方基因交叉产生子代
	 * @param dad
	 * @param mom
	 * @return
	 */
	public static List<Chromosome> genetic(Chromosome dad,Chromosome mom){
		if(dad==null||mom==null)
			return null;
		if(dad.gene==null||mom.gene==null)
			return null;
		if(dad.gene.length!=mom.gene.length)	//基因不等长的双发无法产生下一代
			return null;
		
		Chromosome child1=clone(dad);
		Chromosome child2=clone(mom);
		//产生随机交叉互换位置
		int size=child1.gene.length;
		int a=((int)(Math.random()*size))%size;
		int b=((int)(Math.random()*size))%size;
		int beg= a>b?b:a;
		int end= a>b?a:b;
		for(int i=beg;i<=end;i++){
			boolean tmp=child1.gene[i];
			child1.gene[i]=child2.gene[i];
			child2.gene[i]=tmp;
		}
		List<Chromosome> list=new ArrayList<>();
		list.add(child1);
		list.add(child2);
		return list;
	}	
	/**
	 * 基因突变
	 * @param num	突变次数	
	 */
	public void mutation(int num){
		int size=gene.length;
		for(int i=0;i<num;i++){
			int at=((int)(Math.random()*size))%size;
			boolean bool=gene[at];	//该点位原来的值
			gene[at]=!bool;			
		}
	}	
	/**
	 * 完整基因解码
	 * @return
	 */
	public int[] decodeGene(){		
		int[] res=new int[gene.length/fraglen];		
		for(int i=0;i<res.length;i++){
			res[i]=getFragValFromGene(i);
		}
		return res;
	}	
	/**
	 * 完整基因解码 min<=解码值<=max
	 * @param min
	 * @param max
	 * @return
	 */
	public int[] decodeGene(int min,int max){
		if(max<min)
			return null;
		int[] res=new int[gene.length/fraglen];		
		for(int i=0;i<res.length;i++){
			res[i]=getFragValFromGene(i,min,max);
		}
		return res;
	}	
	public boolean[] getGene(){
		return this.gene;
	}
	public double getFitness(){
		return this.fitness;
	}
	public int getFraglen(){
		return this.fraglen;
	}
	/**
	 * 获取基因有效片段
	 * @param posi
	 * @return
	 */
	public boolean[] getFragFromGene(int posi){
		if(posi*fraglen>=gene.length)
			return null;
		boolean[] bool=new boolean[fraglen];
		for(int i=posi*fraglen,j=0;j<fraglen;i++,j++){
			bool[j]=gene[i];
		}
		return bool;
	}
	/**
	 * 基因有效片段解码
	 * @param posi
	 * @return
	 */
	public int getFragValFromGene(int posi){
		return BaseParse.boolsToInt(getFragFromGene(posi));
	}
	/**
	 * 基因有效片段解码 min<=解码值<=max
	 * @param posi
	 * @param min
	 * @param max
	 * @return
	 */
	public int getFragValFromGene(int posi,int min,int max){
		if(max<min)
			return -1;
		int MAX=1<<fraglen;
		int i=getFragValFromGene(posi);
		double x=(1.0*i/MAX)*(max-min)+min;
		return (int)x;
	}
	public String toString(){
		StringBuffer bf=new StringBuffer();
		bf.append("gene[");
		for(int i=0;i<gene.length;i++){
			if(gene[i])
				bf.append(1);
			else
				bf.append(0);
		}
		bf.append("]");	
		return bf.toString();
	}

}
