package com.github.dockerschesimu.device;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.dockerschesimu.manager.Task;

public class NetworkTest {

	@Test
	public void test() {
		Network net=new Network();
		net.init(10);
		net.monitor();
		
		Task task=new Task();
		task.setNeedNet(40);
		net.doTask(task);
		net.monitor();
		net.checkPoint();
		
		task.setNeedNet(60);
		net.doTask(task);
		net.monitor();
		net.checkPoint();
		
		net.recover(1);
		net.monitor();
		
	}

}
