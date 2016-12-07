package com.github.dockerschesimu.tools;


import com.github.dockerschesimu.error.math.CombinValInputError;
import com.github.dockerschesimu.error.math.FactorialCantBeNegativeError;
import com.github.dockerschesimu.error.math.FactorialOutOfcapabError;
import com.github.dockerschesimu.error.math.ValInputIsnRightError;

/**
 * 计算排列组合中，组合的一系列运算
 * Combination(n,m)	从n个元素中选取m个元素有几种选择方式
 * @author wzt
 *
 */
public class Combination {	
	/**
	 * 获取组合个数 C(n,m)
	 * @param n 集合中元素个数
	 * @param m 选取几个元素
	 * @return
	 * @throws FactorialCantBeNegativeError
	 * @throws CombinValInputError
	 * @throws FactorialOutOfcapabError 
	 * @throws ValInputIsnRightError 
	 */
	public static long combina(int n,int m)
			throws FactorialOutOfcapabError, ValInputIsnRightError{
		if(n<m || n<=0)
			throw new ValInputIsnRightError("获取组合个数出错");
		if(m==0)
			return 1;
		long comb=1;	
		m=(n-m)<m?(n-m):m;	//组合C 转换
		int[] la=new int[m];
		int[] lb=new int[m];
		for(int i=0,p=m;i<p;i++){
			la[i]=n--;
			lb[i]=m--;
		}
		la=BaseMath.arrDivid(la, lb).get(0);
		for(int i=0;i<la.length;i++)
			comb*=la[i];

			
		
		
		return comb;
	}
	/**
	 * 组合结果相加 C(n,p)+...+C(n,q)
	 * @param n
	 * @param p
	 * @param q
	 * @return
	 * @throws FactorialCantBeNegativeError
	 * @throws CombinValInputError
	 */
	public static long combinaAdd(int n,int p,int q) 
			throws FactorialOutOfcapabError, ValInputIsnRightError{
		if(q<p)
			throw new CombinValInputError("组合结果相加出错");
		long sum=0;
		for(int i=p;i<=q;i++)
			sum+=combina(n,i);
		return sum;
	}
	/**
	 * n个元素里取k个的概率
	 * @param n
	 * @param k
	 * @param p
	 * @param q
	 * @return
	 * @throws FactorialCantBeNegativeError
	 * @throws CombinValInputError
	 */
	public static double combianProb(int n,int k,int p,int q)
			throws FactorialOutOfcapabError, ValInputIsnRightError{
		if(k<p||k>q)
			throw new CombinValInputError("n个元素里取k个的概率出错");
		long sum=combinaAdd(n,p,q);
		double prob=combina(n,k)/sum;
		return prob;
	}
	/**
	 * n个元素里取k个的概率，最小可以取到0
	 * @param n
	 * @param k
	 * @param q
	 * @return
	 * @throws ValInputIsnRightError 
	 * @throws FactorialOutOfcapabError 
	 */
	public static double combianProb(int n,int k,int q)
			throws FactorialOutOfcapabError, ValInputIsnRightError{
		return combianProb(n,k,0,q);
	}
	/**
	 * 求从n个元素中取p~q个元素的总的数学期望
	 * @param n
	 * @param p
	 * @param q
	 * @return
	 * @throws FactorialCantBeNegativeError
	 * @throws CombinValInputError
	 */
	public static double combianAvg(int n,int p,int q) 
			throws FactorialOutOfcapabError, ValInputIsnRightError{
		double sum=0.0;
		long deno=combinaAdd(n,p,q);
		for(int i=p;i<=q;i++){
			double pi=1.0*combina(n,i)/deno;
			double tmp=pi*i*1.0;
			sum+=tmp;
		}
		return sum;
	}
}
