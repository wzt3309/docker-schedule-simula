package com.github.dockerschesimu.device;

import static com.github.dockerschesimu.constant.DeviceConstants.*;


import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.tools.BaseMath;

public class HostFactory {

	public static Host simpleCreate(){
		Host host=new Host(new Cpu()
				,new Memory()
				,new Disk()
				,new Network(),DEVICE_LEVEL_MID);
		return host;
	}
	public static List<Host> simpleCreate(int size){
		List<Host> hosts=new ArrayList<>();
		for(int i=0;i<size;i++){
			hosts.add(simpleCreate());
		}
		return hosts;
	}
	/**
	 * seed代表机器配置是low mid high
	 * @param seed
	 * @return
	 */
	public static Host randomCreate(int seed){
		Host host=new Host(randomCpu(seed)
				,randomMem(seed)
				,randomDisk(seed)
				,randomNet(seed),seed);
		return host;
	}
	/**
	 * seed代表机器配置是low mid high
	 * @param seed
	 * @return
	 */
	public static List<Host> randomCreate(int size,int...seed){
		List<Host> hosts=new ArrayList<>();
		for(int i=0;i<size;i++){
			if(seed!=null){
				if(i<seed[0]){
					hosts.add(randomCreate(DEVICE_LEVEL_LOW));
				}
				else if(seed.length>1&&i<(seed[1]+seed[0])){
					hosts.add(randomCreate(DEVICE_LEVEL_MID));					
				}
				else if(seed.length>2&&i<(seed[2]+seed[1]+seed[0])){					
					hosts.add(randomCreate(DEVICE_LEVEL_HIGH));					
				}
				else{
					hosts.add(randomCreate(DEVICE_LEVEL_MID));
				}	
			}			
			else{
				hosts.add(randomCreate(DEVICE_LEVEL_MID));
			}				
		}
		return hosts;
	}
	public static void init(List<Host> hosts){
		for(Host host:hosts){
			host.init(0, 0, 0, 0);
		}
	}
	/**
	 * 随机产生一个cpu
	 * @param seed
	 * @return
	 */
	public static Cpu randomCpu(int seed){		
		int coreNum=(int)Math.pow(2, BaseMath.random(MIN_CORE_NUM
				, MAX_CORE_NUM, seed));
		float fre=BaseMath.random(MIN_CORE_FRE,MAX_CORE_FRE,seed);
		return new Cpu(coreNum,fre);		
	}
	/**
	 * 随机产生一个内存
	 * @param seed
	 * @return
	 */
	public static Memory randomMem(int seed){		
		int total=(int)(Math.pow(2
				,BaseMath.random(MIN_MEM_TOTAL,MAX_MEM_TOTAL,seed))*1000000);
		return new Memory(total);
	}
	/**
	 * 随机产生一个硬盘
	 * @param seed
	 * @return
	 */
	public static Disk randomDisk(int seed){
		int rpm=BaseMath.random(MIN_SATA_RPM,MAX_SATA_RPM,seed)*1000;
		int mtr=BaseMath.random(MIN_SATA_MTR,MAX_SATA_MTR,seed);
		float aat=BaseMath.random(MIN_SATA_AAT,MAX_SATA_AAT,seed);
		return new Disk(rpm,mtr,aat);
	}
	/**
	 * 随机产生一个网络
	 * @param seed
	 * @return
	 */
	public static Network randomNet(int seed){
		int total=BaseMath.random(MIN_NET_TOTAL, MAX_NET_TOTAL, seed);
		return new Network(total);
	}
	
	
	
	
	
	
	
}
