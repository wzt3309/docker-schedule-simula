package com.github.wzt3309.dss.ga.time;

public class TimeCompare {

	private double[][] tasks;
	private int taskNum;
	private int nodeNum;
	public TimeCompare(int taskNum, int nodeNum) {
		
		this.taskNum = taskNum;
		this.nodeNum = nodeNum;
		tasks = new double[taskNum][2];
		initTask();
	}
	
	public void initTask() {
		for(int i=0;i<taskNum;i++){
			tasks[i][0] = (Math.random()*1.2);
			tasks[i][1] = (Math.random()*5+2.56);
		}
	}
	
	public double spread(){		
		double allTime = 0;
		double taskAllTime = 0;
		for(int i=0;i<taskNum;i++){
			if(allTime<=taskAllTime){
				allTime = taskAllTime;
			}
			double infoTime = (nodeNum/3*3)*Math.random()+3.56;
			double algorTime = 2.56*Math.random()+2.43;
			double startTime = 0;
			startTime = tasks[i][1];
			allTime+=infoTime+algorTime+startTime;			
			taskAllTime +=tasks[i][0];
		}
		return allTime;
	}
	public double savg(){		
		double allTime = 0;
		double taskAllTime = 0;
		for(int i=0;i<taskNum;i++){
			if(allTime<=taskAllTime){
				allTime = taskAllTime;
			}
			double algorTime = (2.56*Math.random()+1.45)*nodeNum;
			double infoTime = (nodeNum/3*3)*Math.random()+1.56;
			double startTime = 0;
			startTime = tasks[i][1];
			allTime+=infoTime+algorTime+startTime;			
			taskAllTime +=tasks[i][0];
		}
		return allTime;
	}
	public double genetic(int taskOne){
		double allTime = 0;	
		double startTime = 0;		
		double algorTime = (2.56*Math.random()+90.16);
		double infoTime = (nodeNum/3*3)*Math.random()+1.56;
		for(int i=0;i<taskNum;i++){	
			algorTime+=(2.56*Math.random()+1.14)*nodeNum;
			if(i%taskOne!=0){
				if(startTime<tasks[i][1]){
					startTime = tasks[i][1];
				}
				continue;
			}
		}
		allTime += algorTime+infoTime+startTime;
		return allTime;
	}
	public static void main(String[] args){
		System.out.println("============== spread ==============");
		for(int i=0;i<31;i++){
			spreadLine(i,3);
		}
		System.out.println("=============== savg ===============");
		for(int i=0;i<31;i++){
			savgLine(i,3);
		}
		System.out.println("=============== genetic ===============");
		for(int i=0;i<31;i++){
			geneticLine(i,3);
		}
	}
	public static void spreadLine(int taskNum,int nodeNum){
		TimeCompare time = new TimeCompare(taskNum,nodeNum);
		double spreadSum = 0;
		for(int i=0;i<20;i++){
			spreadSum += time.spread();
		}
		System.out.println((double)spreadSum/20);
	}
	public static void savgLine(int taskNum,int nodeNum){
		TimeCompare time = new TimeCompare(taskNum,nodeNum);
		double savgSum = 0;
		for(int i=0;i<20;i++){
			savgSum += time.savg();
		}
		System.out.println((double)savgSum/20);
	}
	public static void geneticLine(int taskNum,int nodeNum){
		TimeCompare time = new TimeCompare(taskNum,nodeNum);
		double geneticSum = 0;
		for(int i=0;i<20;i++){
			geneticSum += time.genetic(15);
		}
		System.out.println((double)geneticSum/20);
	}
}
