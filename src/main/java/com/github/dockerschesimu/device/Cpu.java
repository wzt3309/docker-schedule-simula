package com.github.dockerschesimu.device;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.dockerschesimu.constant.Device;
import com.github.dockerschesimu.error.device.CoreIsNotInitError;
import com.github.dockerschesimu.error.device.SetCoreUseError;
import com.github.dockerschesimu.error.device.SetCoreUseOverMaxError;
import com.github.dockerschesimu.error.device.SetCpuUseOutOfBoundError;
import com.github.dockerschesimu.tools.BaseComparators;
import com.github.dockerschesimu.tools.BaseUUID;

/**
 * 模拟cpu
 * @author wzt
 *
 */
public class Cpu {

	private final long uuid;	 	//cpu的设备uuid
	private final String nickname;	//cpu别名
	private Core[] cores;	     	//cpu核心组
	private int coreNum;		 	//cpu核心数	
	private int threadNum;	     	//cpu线程数
	private boolean hyperThread; 	//cpu是否支持超线程
	private float frequency;	 	//cpu主频	
	private float use;			 	//cpu利用率 百分数
	
	//-------------------------------------------静态方法区-------------------------------------------//
	/**
	 * 根据cpu每个核心的利用率进行排序
	 * @param c
	 * @throws CoreIsNotInitError 核心必须要是同一cpu的
	 */
	public static void sortCoresByUse(Core[] c) throws CoreIsNotInitError{
		if(isCoreInOneCpu(c))
			Arrays.sort(c, BaseComparators.byCoreUse());
	}
	/**
	 * 根据cpu每个核心的id进行排序
	 * @param c
	 * @throws CoreIsNotInitError 核心必须要是同一cpu的
	 */
	public static void sortCoresById(Core[] c) throws CoreIsNotInitError{
		if(isCoreInOneCpu(c))
			Arrays.sort(c, BaseComparators.byCoreId());
	}
	/**
	 * 根据cpu每个核心的uuid进行排序
	 * @param c
	 * @throws CoreIsNotInitError 核心必须要是同一cpu的
	 */
	public static void sortCoresByUuid(Core[] c) throws CoreIsNotInitError{
		if(isCoreInOneCpu(c))
			Arrays.sort(c, BaseComparators.byCoreUuid());
	}
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
		this.uuid=BaseUUID.uuid(Device.IDENTIFI_CPU);
		this.nickname=BaseUUID.randomstr(Device.CPU_NICKNAME_LENGTH);
	}
	/**
	 * 默认cpu 
	 * 6核12线程 2.00GHz
	 */
	public Cpu(){
		this(Device.DEFAULT_CPU_CORENUM);
	}
	/**
	 * 设定cpu核数n 
	 * n核2*n线程 2.00GHz
	 */
	public Cpu(int coreNum){
		this(coreNum,Device.DEFAULT_CPU_HYPERTHREAD);//默认一个核心模拟两个线程
	}
	/**
	 * 设定cpu核数n、是否支持超线程h
	 * n核 h？(默认超线程数)*n线程 2.00GHz
	 */
	public Cpu(int coreNum,boolean hyperThread){		
		this(coreNum,coreNum,hyperThread);//是否支持超线程
	}
	/**
	 * 设定cpu核数n、线程数t、是否超线程h
	 * n核t线程 2.00GHz h？超线程
	 */
	public Cpu(int coreNum,int threadNum,boolean hyperThread){
		this(coreNum,threadNum,Device.DEFAULT_CORE_FREQUENCY,hyperThread);
	}
	/**
	 * 设定cpu核数n、线程数t、主频f、是否超线程h
	 * n核t线程 fGHz h？超线程
	 * 如果h==true,t<=n,则自动调整t为默认超线程数
	 * 这里假设cpu所有核心都是同一频率—（事实上基本如此）
	 */
	public Cpu(int coreNum,int threadNum,float frequency,boolean hyperThread){
		this.cores=new Core[coreNum];
		this.coreNum=Math.abs(coreNum);//防止输入负数
		
		threadNum=Math.abs(threadNum);//防止输入负数
		if(hyperThread&&threadNum<=coreNum)//自动调整cpu线程数
			threadNum=Device.DEFAULT_CPU_HYPERTHREADN*coreNum;
		this.threadNum=threadNum;
		
		this.frequency=frequency;
		this.hyperThread=hyperThread;
		initCore(frequency);
	}
	/**
	 * 初始化cpu核心
	 * @param frequency
	 */
	private void initCore(float frequency){		
		for(int i=0;i<coreNum;i++)
			cores[i]=new Core(i,this.uuid,frequency);
	}
	
	//-------------------------------------------对象方法区-------------------------------------------//	
	/**
	 * cpu总利用率
	 * @return
	 */
	public float getUse() {
		updateUse();
		return this.use;
	}
	/**
	 * 更新cpu平均使用率
	 * ps 由于更新的方式会变，因此将它单独写成一个方法
	 * 避免代码污染
	 */
	private void updateUse(){
		for(Core core:cores)
			this.use+=core.getUse();
		this.use/=this.coreNum;
	}
	/**
	 * 获取详细cpu每个核的使用率
	 * @return
	 */
	public Map<Integer,Float> getDetalUse(){
		Map<Integer,Float> detalUse=new HashMap<>();
		for(Core core:cores)
			detalUse.put(core.getId(), core.getUse());
		return detalUse;
	}
	/**
	 * 设置cpu某个核心的利用率
	 * @param use
	 * @param i
	 * @throws SetCpuUseOutOfBoundError 
	 * @throws SetCoreUseOverMaxError 
	 */
	public void setUse(float use,int i) 
			throws SetCpuUseOutOfBoundError, SetCoreUseError{
		if(coreNum<=i)
			throw new SetCpuUseOutOfBoundError();
		this.cores[i].setUse(use);		
	}
	/**
	 * 传入一个use，将cpu所有核心的use值设为此值
	 * @param use
	 * @throws SetCoreUseOverMaxError
	 */
	public void setUse(float use) throws SetCoreUseError {
		for(int i=0;i<coreNum;i++){
			cores[i].setUse(use);
		}		
	}
	/**
	 * 设置cpu内前n个核心使用率都是use
	 * @param n
	 * @param use
	 * @throws SetCpuUseOutOfBoundError
	 * @throws SetCoreUseOverMaxError
	 */
	public void setUse(int n,float use) 
			throws SetCpuUseOutOfBoundError, SetCoreUseError{
		setUse(0,n-1,use);
	}
	/**
	 * 设置cpu内从第beg开始到第beg+offset核心，使用率都是use
	 * @param beg
	 * @param offset
	 * @param use
	 * @throws SetCpuUseOutOfBoundError
	 * @throws SetCoreUseError
	 */
	public void setUse(int beg,int offset,float use) 
			throws SetCpuUseOutOfBoundError, SetCoreUseError{
		int ifinal=beg+offset;
		if(coreNum<=ifinal)
			throw new SetCpuUseOutOfBoundError();
		for(int i=beg;i<=ifinal;i++)
			setUse(use,i);
	}
	/**
	 * 传入单个核心的use，分别赋值给cpu的所有核心
	 * @param uses
	 * @throws SetCpuUseOutOfBoundError
	 * @throws SetCoreUseOverMaxError
	 */
	public void setUse(float... uses) 
			throws SetCpuUseOutOfBoundError, SetCoreUseError{
		if(coreNum<uses.length)
			throw new SetCpuUseOutOfBoundError();			
		for(int i=0;i<coreNum;i++){
			if(uses.length<=i)
				cores[i].setUse(0.0F);
			else				
				cores[i].setUse(uses[i]);
		}		
	}	
	/**
	 * 返回字符串CPU[uuid nickname] c核t线程fGHz
	 */
	public String toString(){
		return String.format("CPU[%-18s %s] %2d核%3d线程%6.2fGHz total_use:%4.2f%%", String.valueOf(uuid)
				,this.nickname,coreNum,threadNum,frequency,getUse());
	}
	/**
	 * 打印cpu描述 CPU[uuid nickname] c核t线程fGHz
	 */
	public void show(){
		System.out.println(this);
	}
	/**
	 * 显示该cpu详细利用率信息
	 */
	public void showAll(){
		show();
		System.out.format("%-2s %19s  %4s %8s %19s\n"
				, "id","uuid","frequency","use","ref_cpu_uuid");
		System.out.format("%-3s %-18s  %4s %8s %19s\n", "--","------------------"
				,"---------","------","-------------------");		
		for(int i=0;i<cores.length;i++)
			System.out.println(cores[i]);
	}
	
	//-------------------------------------------getter/setter区-------------------------------------------//
	public Core[] getCores() {
		return cores;
	}
	public void setCores(Core[] cores) {
		this.cores = cores;
	}
	public int getCoreNum() {
		return coreNum;
	}
	public void setCoreNum(int coreNum) {
		this.coreNum = coreNum;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}	
	public boolean isHyperThread() {
		return hyperThread;
	}
	public void setHyperThread(boolean hyperThread) {
		this.hyperThread = hyperThread;
	}
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
}
