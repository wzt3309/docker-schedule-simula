package com.github.dockerschesimu.device;


import org.junit.Test;

import com.github.dockerschesimu.error.ValError;

public class SATADiskTest {

	@Test
	public void test() throws ValError {
		SATADisk disk=new SATADisk();
//		System.out.println(disk.getUse(100, 400));
//		System.out.println(disk.getUse(120, 400));
		disk.initIO(10);
		System.out.println(disk.getUse());		
		disk.taskIO(1, 4);
		System.out.println(disk.getUse());
	}

}
