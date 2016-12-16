package com.github.dockerschesimu.device;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.dockerschesimu.error.device.MemoryOverFlowError;

public class MemoryTest {

	private Memory mem=new Memory();
	
	@Test
	public void testSetUsed() throws MemoryOverFlowError {
		int used=1500000,total=mem.getTotal(),free;
		float use=0F;
		mem.malloc(1500000);
		free=total-used;
		use=(used+1F)/total*100;
		
		assertEquals(used,mem.getUsed());
		assertEquals(free,mem.getFree());
		assertEquals(use,mem.getUse(),0.001);
		mem.show();
	}

	@Test
	public void testShow() throws MemoryOverFlowError {
		mem.show();
		mem.malloc(1600);
		mem.show();
	}

}
