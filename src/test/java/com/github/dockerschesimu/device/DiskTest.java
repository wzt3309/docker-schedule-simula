package com.github.dockerschesimu.device;


import org.junit.Test;

import com.github.dockerschesimu.error.ValError;
import com.github.dockerschesimu.manager.Task;

public class DiskTest {

	@Test
	public void test() throws ValError {
		Disk disk=new Disk();
//		System.out.println(disk.getUse(100, 400));
//		System.out.println(disk.getUse(120, 400));
		disk.init(10);
		disk.monitor();
		
		disk.doTask(new Task(100,400));
		disk.monitor();
		disk.checkPoint();
		
		disk.doTask(new Task(120,400));
		disk.monitor();
		
		disk.recover(1);
		disk.monitor();
		
	}

}
