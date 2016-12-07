package com.github.dockerschesimu.tools;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.github.dockerschesimu.error.math.FactorialCantBeNegativeError;
import com.github.dockerschesimu.error.math.FactorialOutOfcapabError;
import com.github.dockerschesimu.error.math.ValInputIsnRightError;

public class TestBaseMath {

	@Test
	public void testFactorial(){
		int[] n=new int[]{-2,-1,0,1,2,3};		
		for(int i:n){
			int std=1;
			
			if(i<0)
				continue;
			else if(i>1)
				for(int j=i;j>1;j--)
					std*=j;
			else;
			try{
				long myres=BaseMath.factorial(i);
				assertEquals(std,myres);
			}catch(FactorialCantBeNegativeError e){
				continue;
			} catch (FactorialOutOfcapabError e) {
				e.printStackTrace();
			}			
		}
			
	}
	@Test
	public void testClearDivi(){
		int n=12,m=8;
		try {
			int[] res=BaseMath.clearDivi(n, m);
			assertEquals(3,res[0]);
			assertEquals(2,res[1]);
		} catch (ValInputIsnRightError e) {
			e.printStackTrace();
		}
		
	}
	@Test 
	public void testmaxComDivi(){
		int n=12,m=8;
		try {
			int res=BaseMath.maxComDivi(n, m);
			assertEquals(4,res);
		} catch (ValInputIsnRightError e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testArrDivid(){
		int[] a=new int[40];
		int[] b=new int[40];
		for(int i=0,p=100,q=40;i<40;i++){
			a[i]=p--;
			b[i]=q--;
		}
		try {
			ArrayList<int[]> res=BaseMath.arrDivid(a, b);
			for(int[] re:res){
				for(int i:re)
					System.out.print(i+",");
				System.out.println();
			}
				
		} catch (ValInputIsnRightError e) {
			e.printStackTrace();
		}
	}

}
