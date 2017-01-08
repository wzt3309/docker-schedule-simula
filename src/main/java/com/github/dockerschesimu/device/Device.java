package com.github.dockerschesimu.device;

import com.github.dockerschesimu.manager.Task;

public interface Device {
	
	/**
	 * 执行任务
	 * @param task
	 * @return 是否成功分配任务
	 */
	public boolean doTask(Task task);
	/**
	 * 获取硬件负载
	 * @return
	 */
	public double getLoad();
	/**
	 * 记录检查点
	 */
	public void checkPoint();
	/**
	 * 硬件恢复到某个检查点，以便在检查点之后的动作不会影响新的任务执行
	 * 每次分配完任务后，获得硬件负载；
	 * 但并不是最终分配方案，采取新的分配方案时，要消除原先分配方案影响
	 * 
	 * 恢复到上一个检查点
	 * @param point
	 * @return 返回到第几个检查点
	 */
	public int recover();
	/**
	 * 返回到第i个检查点 从0开始
	 * @param i
	 */
	public void recover(int i);
	/**
	 * 返回硬件当前有多少个检查点
	 * @return
	 */
	public int getRecoverI();
	/**
	 * 获取硬件的详细运行信息
	 */
	public void monitor();
	/**
	 * 获取硬件本身的配置信息
	 */
	public void info();
	
}
