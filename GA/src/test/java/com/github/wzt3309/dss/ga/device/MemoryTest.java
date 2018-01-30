package com.github.wzt3309.dss.ga.device;

import com.github.wzt3309.dss.ga.error.device.MemoryOverFlowError;
import com.github.wzt3309.dss.ga.manager.Task;
import org.junit.Test;

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
