package com.github.dockerschesimu.device;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.dockerschesimu.error.device.SetCoreUseError;
import com.github.dockerschesimu.error.device.SetCpuUseOutOfBoundError;

public class CpuTest {


	@Test
	public void testSortCoresByUse() {
		fail("Not yet implemented");
	}

	@Test
	public void testSortCoresById() {
		fail("Not yet implemented");
	}

	@Test
	public void testSortCoresByUuid() {
		fail("Not yet implemented");
	}

	@Test
	public void testCpu() {
		Cpu cpu=new Cpu();
		try {
			cpu.setUse(30.56F, 2);
			cpu.setUse(37.5F, 1);
			cpu.setUse(37.5F, 3);
		} catch (SetCpuUseOutOfBoundError | SetCoreUseError e) {
			e.printStackTrace();
		}
		cpu.showAll();		
	}

	@Test
	public void testCpuInt() {
		Cpu cpu=new Cpu(4);
		try {
			cpu.setUse(30.56F, 2);
			cpu.setUse(37.5F, 1);
			cpu.setUse(37.5F, 3);
		} catch (SetCpuUseOutOfBoundError | SetCoreUseError e) {
			e.printStackTrace();
		}
		cpu.showAll();
	}

	@Test
	public void testCpuIntBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testCpuIntIntBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testCpuIntIntFloatBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUse() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDetalUse() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUseFloatInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUseFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUseIntFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUseFloatArray() {
		fail("Not yet implemented");
	}

}
