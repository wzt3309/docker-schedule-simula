package com.github.wzt3309.dss.ga.tools;

import java.math.BigInteger;
import java.util.ArrayList;

import com.github.wzt3309.dss.ga.error.math.FactorialCantBeNegativeError;
import com.github.wzt3309.dss.ga.error.math.FactorialOutOfcapabError;
import com.github.wzt3309.dss.ga.error.math.ValInputIsnRightError;


/**
 * 基础数学计算静态方法
 * @author wzt
 *
 */
public class BaseMath {
	
	/**
	 * 计算阶乘,大数阶乘
	 * @param n
	 * @return
	 * @throws FactorialCantBeNegativeError
	 */
	public static BigInteger factorialBig(int n)throws FactorialCantBeNegativeError{
		if(n<0)
			throw new FactorialCantBeNegativeError();
		if(n==0||n==1)
			return new BigInteger("1");
		BigInteger big=new BigInteger(String.valueOf(n));
		return big.multiply(factorialBig(n));
	}
	/**
	 * 计算阶乘，小数阶乘
	 * @param n
	 * @return
	 * @throws FactorialCantBeNegativeError
	 * @throws FactorialOutOfcapabError
	 */
	public static long factorial(int n)
			throws FactorialCantBeNegativeError,FactorialOutOfcapabError{
		if(n<0)
			throw new FactorialCantBeNegativeError();
		if(n>10)
			throw new FactorialOutOfcapabError();
		if(n==0||n==1)
			return 1L;		
		return n*factorial(n-1);
	}
	/**
	 * 约分，分子，分母都是形如a*b*c*d*...*n的形态
	 * @param la
	 * @param lb
	 * @return
	 * @throws ValInputIsnRightError
	 */
	public static ArrayList<int[]> arrDivid(int[] la,int[] lb) throws ValInputIsnRightError{
		return arrDivid(la,lb,la.length);
	}
	/**
	 * 约分
	 * @param la
	 * @param lb
	 * @param isclear
	 * @return
	 * @throws ValInputIsnRightError
	 */
	private static ArrayList<int[]> arrDivid(int[] la,int[] lb,int isclear) throws ValInputIsnRightError{		
		ArrayList<int[]> res=new ArrayList<int[]>();
		for(int i=0;i<la.length;i++){
			if(isclear==0){
				res.add(la);
				res.add(lb);
				return res;
			}				
			int lbl=lb.length;
			for(int j=0;j<lb.length;j++){
				if(lb[j]==1){
					lbl--;
					continue;
				}	
				//约分
				int maxComDivi=maxComDivi(la[i],lb[j]);
				if(maxComDivi!=1){
					la[i]=la[i]/maxComDivi;
					lb[j]=lb[j]/maxComDivi;
					break;
				}
				lbl--;
				continue;
			}
			if(lbl==0)
				isclear--;
		}		
		return arrDivid(la,lb,isclear);
	}
	/**
	 * 普通约分 
	 * @param n
	 * @param m
	 * @return
	 * @throws ValInputIsnRightError
	 */
	public static int[] clearDivi(int n,int m)throws ValInputIsnRightError{
		int[] res=new int[2];
		int maxComDivi=maxComDivi(n,m);
		res[0]=n/maxComDivi;
		res[1]=m/maxComDivi;
		return res;
	}
	/**
	 * 求最大公约数
	 * @param n
	 * @param m
	 * @return
	 * @throws ValInputIsnRightError
	 */
	public static int maxComDivi(int n,int m) throws ValInputIsnRightError{
		if(n<=0||m<=0)
			throw new ValInputIsnRightError();		
		int small=n;
		n=m<=n?n:m;
		m=n==small?m:small;
		while(n%m!=0){
			int tmp=n%m;
			n=m;
			m=tmp;
		}
		return m;
	}
	/**
	 * 限定范围的随机数
	 * @param min
	 * @param max
	 * @param seed
	 * @return
	 */
	public static int random(int min,int max,int seed){
		double len=(max-min)*1.0/3;
		double at=Math.random();
		switch(seed){
		case 1:return (int)(at*len+min);
		case 2:return (int)((at+1)*len+min);
		case 3:return (int)((at+2)*len+min);
		default :return -1;
		}
		
	}
	/**
	 * 限定范围的随机数
	 * @param min
	 * @param max
	 * @param seed
	 * @return
	 */
	public static float random(float min,float max,int seed){
		double len=(max-min)*1.0/3;
		double at=Math.random();
		switch(seed){
		case 1:return (float)(at*len+min);
		case 2:return (float)((at+1)*len+min);
		case 3:return (float)((at+2)*len+min);
		default :return -1;
		}
	}
	/**
	 * 限定范围的随机数
	 * @param min
	 * @param max
	 * @param seed
	 * @return
	 */
	public static double random(double min,double max,int seed){
		double len=(max-min)*1.0/3;
		double at=Math.random();
		switch(seed){
		case 1:return (at*len+min);
		case 2:return ((at+1)*len+min);
		case 3:return ((at+2)*len+min);
		default :return -1;
		}
	}
	/**
	 * 计算标准差
	 * @param params
	 * @return
	 */
	public static double stander(double[] params){
		if(params==null||params.length<1){
			return 0;
		}
		double sum=0;
		double len=params.length;
		double avg=0;
		double sumP=0;
		double avgP=0;
		
		for(double x:params){
			sum+=x;
		}
		avg=sum/len;
		for(double x:params){
			double p=Math.pow(x-avg, 2);
			sumP+=p;
		}
		avgP=Math.sqrt(sumP/len);
		return avgP;
		
	}
	
	
	
	
	
	
}
