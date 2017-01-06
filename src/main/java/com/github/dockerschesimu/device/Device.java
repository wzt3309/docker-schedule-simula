package com.github.dockerschesimu.device;

import com.github.dockerschesimu.manager.Task;

public interface Device {
	
	/**
	 * 硬件初始化
	 */
	public void init(double use);
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
	 * 硬件恢复到某个检查点，以便在检查点之后的动作不会影响新的任务执行
	 * 每次分配完任务后，获得硬件负载；
	 * 但并不是最终分配方案，采取新的分配方案时，要消除原先分配方案影响
	 * @param point
	 */
	public void recover(CheckPoint point);
	/**
	 * 获取硬件的详细运行信息
	 */
	public void monitor();
	/**
	 * 获取硬件本身的配置信息
	 */
	public void info();
	
}
