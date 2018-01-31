package com.github.wzt3309.dss.as.algorithm

class Ant {
    final int nodeNum
    final int taskNum
    final Node[] nodes         // 节点
    final Task[] tasks         // 任务
    final float alpha          // 信息启发式因子
    final float beta           // 期望启发式因子

    Set<Integer> tabu    // 禁忌表，不允许使用的节点
    Set<Integer> allow   // 允许使用的节点
    float[][] delta      // 信息素增量矩阵
    float[][] eta        // 分配任务i到节点j的期望矩阵

    List<Integer> result = []      // 分配结果
    int cur                        // 当前被分配的任务
    int next                       // 下一个需要分配的任务

    Ant(Node[] nodes, Task[] tasks, float a, float b) {
        alpha = a
        beta = b
        this.nodes = nodes
        this.tasks = tasks
        nodeNum = nodes.length
        taskNum = tasks.length
        init()
    }

    def init() {
        tabu = []
        allow = 0..nodes.length - 1
        delta = new float[taskNum][nodeNum]
        eta = new float[taskNum][nodeNum]

        cur = 0
        new Random().with {
            result << nextInt(nodeNum)
        }
        updateEta()
        updateTabuAndAllow()

        next = 1
    }

    def schedule(float[][] tha) {
        def p = []
        def sum = 0
        allow.each {
            sum += (tha[next][it] ** alpha) * (eta[next][it] ** beta)
        }

        int i = 0
        allow.each {
           if (i++ == it)
               p << ((tha[next][it] ** alpha) * (eta[next][it] ** beta)) / sum
           else
               p << 0
        }

        cur = next
        // 轮盘赌选择task[next] 分配到的节点编号
        result << Utils.roulette(p)
        updateEta()
        updateTabuAndAllow()

        next = cur + 1
    }

    float calBalance() {
        def cpub = [], memb = [], iob = [], netb = []

        result.each { i ->
            cpub << nodes[i].cpu.used
            memb << nodes[i].mem.used
            iob << nodes[i].io.used
            netb << nodes[i].net.used
        }
        Utils.standerAll(cpub, memb, iob, netb)
    }

    def updateEta() {
        nodes[result[cur]].handle(tasks[cur])
        for (int i = 0; i < taskNum; i++) {
            for (int j = 0; j < nodeNum; j++) {
                eta[i][j] = 1 / nodes[j].tryTask(tasks[i])
            }
        }
    }

    def updateTabuAndAllow() {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isTabu()) {
                tabu.add(i)
            }
        }
        allow -= tabu
    }
}
