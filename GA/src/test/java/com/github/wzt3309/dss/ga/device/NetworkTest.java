package com.github.wzt3309.dss.ga.device;

import com.github.wzt3309.dss.ga.manager.Task;
import org.junit.Test;

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
