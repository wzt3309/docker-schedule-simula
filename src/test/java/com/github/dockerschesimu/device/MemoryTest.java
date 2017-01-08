package com.github.dockerschesimu.device;

import org.junit.Test;

import com.github.dockerschesimu.error.device.MemoryOverFlowError;
import com.github.dockerschesimu.manager.Task;

public class MemoryTest {

	 
	
	@Test
	public void testSetUsed() throws MemoryOverFlowError {
		Memory mem=new Memory();
		int used=1500000;
		mem.init(used);		
		mem.monitor();
		
		mem.doTask(new Task(2000000));
		mem.checkPoint();
		mem.monitor();
		
		mem.doTask(new Task(1000000));
		mem.checkPoint();
		mem.monitor();
		
		mem.recover(0);
		mem.monitor();
		
	}

	

}
