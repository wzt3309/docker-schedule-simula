package com.github.wzt3309.dss.ga.device;

import java.util.List;

import com.github.wzt3309.dss.ga.manager.Task;
import com.github.wzt3309.dss.ga.manager.TaskManager;
import org.junit.Test;

public class HostFactoryTest {

	@Test
	public void test() {
		List<Host> hosts=HostFactory.randomCreate(4, 1,2,1);
		HostFactory.init(hosts);
		for(Host host:hosts){
			host.monitor();
		}
			
		
		
		List<Task> tasks= TaskManager.randomTasksFit(5);
		int[] taskTable=new int[]{3,3,0,1,2};
		
		for(Task task:tasks){
			task.show();
		}
		System.out.println("task 1");
		boolean over=false;
		for(int i=0;i<tasks.size();i++){
			Task task=tasks.get(i);
			int hostI=taskTable[i];
			Host host=hosts.get(hostI);
//			if(!host.doTask(task)){
//				over=true;
//				System.out.println("task"+i+"host"+hostI);
//				break;
//			}
		}
		if(over){
			for(Host host:hosts)
				host.recover();
		}
		for(Host host:hosts)
			host.monitor();
		
		System.out.println("recover 1");
		for(Host host:hosts)
			host.recover();
		for(Host host:hosts)
			host.monitor();
	}

}
