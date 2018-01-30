package com.github.wzt3309.dss.ga.tools;

import org.apache.log4j.Logger;

public class BaseLogger {

	private static Logger logger = Logger.getLogger("BaseLogger");
	
	public static void DEBUG(Object args){
		logger.debug(args);
	}
	public static void INFO(Object args){
		logger.debug(args);
	}
	public static void WARN(Object args){
		logger.debug(args);
	}
	public static void ERROR(Object args){
		logger.debug(args);
	}
}
