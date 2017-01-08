package com.github.dockerschesimu.device;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.dockerschesimu.constant.DeviceConstants.*;
import com.github.dockerschesimu.error.device.CoreIsNotInitError;
import com.github.dockerschesimu.error.device.SetCoreUseOverMaxError;
import com.github.dockerschesimu.error.device.SetCpuUseOutOfBoundError;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.tools.BaseComparators;
import com.github.dockerschesimu.tools.BaseUUID;

/**
 * 模拟cpu
 * @author wzt
 *
 */
public class Cpu implements Device{

	private final long uuid;	 			//cpu的设备uuid
	private final String nickname;			//cpu别名
	private final Core[] cores;	     		//cpu核心组
	private final int coreNum;		 		//cpu核心数	
	private final float frequency;	 		//cpu主频	
	private double use;			 			//cpu利用率 百分数
	
	private int recoverI=0;					//当前是第几个保存点
	/**
	 * 判断传入的核心组是否属于同个cpu
	 * @param c
	 * @return
	 * @throws CoreIsNotInitError
	 */
	private static boolean isCoreInOneCpu(Core[] c) throws CoreIsNotInitError{
		Core c0=c[0];
		if(c0!=null){
			long refuuid=c[0].getRef_cpu_uuid();
			for(Core ci:c){
				if(ci==null)
					throw new CoreIsNotInitError();
				if(ci.getRef_cpu_uuid()!=refuuid)
					return false;
			}				
			return true;
		}
		throw new CoreIsNotInitError();
	}
	
	//-------------------------------------------初始化区-------------------------------------------//
	{
		this.uuid=BaseUUID.uuid(IDENTIFI_CPU);
		this.nickname=BaseUUID.randomstr(CPU_NICKNAME_LENGTH);
	}
	/**
	 * 默认cpu 
	 * 6核12线程 2.00GHz
	 */
	public Cpu(){
		this(DEFAULT_CPU_CORENUM);
	}
	/**
	 * 设定cpu核数n 
	 * n核 2.00GHz
	 */
	public Cpu(int coreNum){
		this(coreNum,DEFAULT_CORE_FREQUENCY);
	}	
	/**
	 * 设定cpu核数n、主频f
	 * n核t线程 fGHz 
	 */
	public Cpu(int coreNum,float frequency){
		this.coreNum=Math.abs(coreNum);//防止输入负数
		this.cores=new Core[coreNum];		
		this.frequency=frequency;
		initCore(frequency);
	}
	/**
	 * 初始化cpu核心
	 * @param frequency
	 */
	private void initCore(float frequency){		
		for(int i=0;i<coreNum;i++){
			cores[i]=new Core(i,this.uuid,frequency);
		}	
	}
	
	//-------------------------------------------对象方法区-------------------------------------------//	
	/**
	 * 传入单个核心的use，分别赋值给cpu的所有核心
	 * @param uses
	 * @throws SetCpuUseOutOfBoundError
	 * @throws SetCoreUseOverMaxError
	 */
	public void init(double use) {		
		for(int i=0;i<coreNum;i++){			
			cores[i].init(use);
		}
		//checkPoint(); core init时自动保存记录点
		recoverI++;
	}	
	@Override
	public boolean doTask(Task task) {
		sortCoresByUse();
		int needThreadNum=task.getNeedThreadNum();
		needThreadNum=(needThreadNum<=coreNum?
					needThreadNum:coreNum);
		for(int i=0;i<needThreadNum;i++){
			if(!cores[i].doTask(task))
				return false;
		}		
		return true;
	}
	@Override
	public double getLoad() {
		this.use=0;
		for(Core core:cores)
			this.use+=core.getLoad();
		this.use/=this.coreNum;
		return this.use;
	}
	@Override
	public int recover() {
		int k=-1;
		if(recoverI>0){
			for(int i=0;i<cores.length;i++){
				if(cores[i].recover()==-1)
					break;
			}
			k=recoverI-1;
		}
		return k;
	}
	@Override
	public void recover(int k) {
		if(k<recoverI&&k>-1){
			for(int i=0;i<cores.length;i++){
				cores[i].recover(k);
			}
			recoverI=k+1;
		}
		
	}
	@Override
	public int getRecoverI(){
		return recoverI;
	}
	@Override
	public void checkPoint() {
		for(Core core:cores){
			core.checkPoint();
		}
		recoverI++;
	}	
	@Override
	public void monitor() {
		info();
		System.out.format("%-2s %19s  %4s %8s %19s\n"
				, "id","uuid","frequency","use","ref_cpu_uuid");
		System.out.format("%-3s %-18s  %4s %8s %19s\n", "--","------------------"
				,"---------","------","-------------------");	
		sortCoresById();
		for(int i=0;i<cores.length;i++)
			cores[i].monitor();
	}
	@Override
	public void info() {
		System.out.println(this);
	}	
	
	/**
	 * 根据cpu每个核心的利用率进行排序
	 * @param c
	 * @throws CoreIsNotInitError 核心必须要是同一cpu的
	 */
	private void sortCoresByUse(){
		Arrays.sort(cores, BaseComparators.byCoreUse());
	}
	/**
	 * 根据cpu每个核心的id进行排序
	 * @param c
	 * @throws CoreIsNotInitError 核心必须要是同一cpu的
	 */
	private void sortCoresById(){
		Arrays.sort(cores, BaseComparators.byCoreId());
	}
	/**
	 * 根据cpu每个核心的uuid进行排序
	 * @param c
	 * @throws CoreIsNotInitError 核心必须要是同一cpu的
	 */
	private void sortCoresByUuid(){
		Arrays.sort(cores, BaseComparators.byCoreUuid());
	}	
	/**
	 * 返回字符串CPU[uuid nickname] c核t线程fGHz
	 */
	public String toString(){
		return String.format("CPU[%-18s %s] %2d核%6.2fGHz total_use:%4.2f%%", String.valueOf(uuid)
				,this.nickname,coreNum,frequency,getLoad());
	}	
	//-------------------------------------------getter/setter区-------------------------------------------//
	public Core[] getCores() {
		return cores;
	}	
	public int getCoreNum() {
		return coreNum;
	}	
	public float getFrequency() {
		return frequency;
	}	
	public String getName(){
		return nickname;
	}
	/**
	 * 获取详细cpu每个核的使用率
	 * @return
	 */
	public Map<Integer,Double> getDetalUse(){
		Map<Integer,Double> detalUse=new HashMap<>();
		for(Core core:cores)
			detalUse.put(core.getId(), core.getLoad());
		return detalUse;
	}	
}
