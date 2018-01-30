package com.github.wzt3309.dss.ga.tools;

import com.github.wzt3309.dss.ga.error.ValCantNegative;
import com.github.wzt3309.dss.ga.error.ValNeedPositiveError;

public class BaseValidate {

	
	public static void cantNegative(int... a)throws ValCantNegative {
		for(int i=0;i<a.length;i++)
			if(a[i]<0)
				throw new ValCantNegative();
	}
	public static void cantNegative(float... a)throws ValCantNegative{
		for(int i=0;i<a.length;i++)
			if(a[i]<0)
				throw new ValCantNegative();
	}
	public static void cantNegative(double... a)throws ValCantNegative{
		for(int i=0;i<a.length;i++)
			if(a[i]<0)
				throw new ValCantNegative();
	}
	
	public static void needPositive(int... a)throws ValNeedPositiveError {
		for(int i=0;i<a.length;i++)
			if(a[i]<=0)
				throw new ValNeedPositiveError();
	}
	public static void needPositive(float... a)throws ValNeedPositiveError{
		for(int i=0;i<a.length;i++)
			if(a[i]<=0)
				throw new ValNeedPositiveError();
	}
	public static void needPositive(double... a)throws ValNeedPositiveError{
		for(int i=0;i<a.length;i++)
			if(a[i]<=0)
				throw new ValNeedPositiveError();
	}
	public static boolean lessThan(int stand,int...vals){
		for(int val:vals)
			if(stand<=val)
				return false;
		return true;
	}
	public static boolean lessThan(float stand,float...vals){
		for(float val:vals)
			if(stand<=val)
				return false;
		return true;
	}
	public static boolean lessThan(double stand,double...vals){
		for(double val:vals)
			if(stand<=val)
				return false;
		return true;
	}
	public static boolean noMoreThan(int stand,int...vals){
		for(int val:vals)
			if(stand<val)
				return false;
		return true;
	}
	public static boolean noMoreThan(float stand,float...vals){
		for(float val:vals)
			if(stand<val)
				return false;
		return true;
	}
	public static boolean noMoreThan(double stand,double...vals){
		for(double val:vals)
			if(stand<val)
				return false;
		return true;
	}
	
	
	
	
}
