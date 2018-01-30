package com.github.wzt3309.dss.ga.device;


import com.github.wzt3309.dss.ga.error.ValError;
import com.github.wzt3309.dss.ga.manager.Task;
import org.junit.Test;

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
