package com.github.dockerschesimu.tools;

import java.util.Comparator;

import com.github.dockerschesimu.device.Core;

public class BaseComparators {

	/**
	 * 根据Core（核心）的利用率use进行排序
	 * @return
	 */
	public static Comparator<Core> byCoreUse(){
		return new Comparator<Core>(){
			@Override
			public int compare(Core o1, Core o2) {
				
				if(o1.getUse()>o2.getUse())
					return 1;
				else if(o1.getUse()<o2.getUse())
					return -1;
				
				return 0;
			}			
		};
	}
	/**
	 * 根据Core（核心）的id号进行排序
	 * @return
	 */
	public static Comparator<Core> byCoreId(){
		return new Comparator<Core>(){
			@Override
			public int compare(Core o1, Core o2) {				
				if(o1.getId()>o2.getId())
					return 1;
				else if(o1.getId()<o2.getId())
					return -1;				
				return 0;
			}			
		};
	}
	/**
	 * 根据Core（核心）的uuid号进行排序
	 * @return
	 */
	public static Comparator<Core> byCoreUuid(){
		return new Comparator<Core>(){
			@Override
			public int compare(Core o1, Core o2) {				
				if(o1.getUuid()>o2.getUuid())
					return 1;
				else if(o1.getUuid()<o2.getUuid())
					return -1;				
				return 0;
			}			
		};
	}
}
