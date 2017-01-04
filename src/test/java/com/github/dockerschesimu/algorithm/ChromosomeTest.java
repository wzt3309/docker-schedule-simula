package com.github.dockerschesimu.algorithm;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChromosomeTest {

	@Test
	public void testDecodeGene() {
		Chromosome some=new Chromosome(3,3);
		System.out.println(some);
		int[] res=some.decodeGene();
		for(int i:res)
			System.out.print(i+" ");
		System.out.println();
	}
	@Test
	public void testConstractArr() {
		Chromosome some=new Chromosome(new int[]{0,1,2,3,4},4);
		System.out.println(some);
		int[] res=some.decodeGene();
		for(int i:res)
			System.out.print(i+" ");
		System.out.println();
	}
//	@Test
//	public void testConstractMinMax() {
//		Chromosome some=new Chromosome(4,1,5);
//		System.out.println(some);
//		int[] res=some.decodeGene();
//		for(int i:res)
//			System.out.print(i+" ");
//		System.out.println();
//	}
	@Test
	public void testMutation() {
		Chromosome some=new Chromosome(4,10);
		System.out.println("未变异："+some);
		int[] res=some.decodeGene(5,9);
		for(int i:res)
			System.out.print(i+" ");
		System.out.println();
		
		some.mutation(4);
		System.out.println("变异后："+some);
		res=some.decodeGene(5,9);
		for(int i:res)
			System.out.print(i+" ");
		System.out.println();
	}
}
