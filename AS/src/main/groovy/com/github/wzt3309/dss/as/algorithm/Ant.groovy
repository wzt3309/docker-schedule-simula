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

    List<Integer> result           // 分配结果
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
        nodes.each {
            it.reset()
        }
        delta = new float[taskNum][nodeNum]
        eta = new float[taskNum][nodeNum]

        cur = 0
        result = []
        new Random().with {
            result << nextInt(nodeNum)
        }
        updateEta()
        updateTabuAndAllow()

        next = 1
    }

    def schedule(float[][] tha) {
        def p = []
        float sum = 0
        allow.each {
            sum += (tha[next][it] ** alpha) * (eta[next][it] ** beta)
        }

        for (int j = 0; j < nodeNum; j++) {
            if (allow.contains(j)) p << ((tha[next][j] ** alpha) * (eta[next][j] ** beta)) / sum
            else p << 0
        }


        cur = next
        // 轮盘赌选择task[next] 分配到的节点编号
        result << Utils.roulette(p)
        updateEta()
        updateTabuAndAllow()

        next = cur + 1
    }

    float antb() {
        def cpub = [], memb = [], iob = [], netb = []

        nodes.each {
            cpub << it.cpu.used
            memb << it.mem.used
            iob << it.io.used
            netb << it.net.used
        }
        Utils.standerAll(cpub, memb, iob, netb)
    }

    def updateEta() {
        nodes[result[cur]].handle(tasks[cur])
        for (int i = 0; i < taskNum; i++) {
            for (int j = 0; j < nodeNum; j++) {
                eta[i][j] = 1.0f - nodes[j].tryTask(tasks[i])
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
