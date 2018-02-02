package com.github.wzt3309.dss.as.algorithm

import groovy.transform.Field

@Field Ant[] ants
@Field int antNum
@Field int MAX_GEN
@Field float[][] tha
@Field List<Node> nodes = []
@Field int nodeNum
@Field List<Task> tasks = []
@Field int taskNum

@Field float bestBalance
@Field List<Integer> bestResult
@Field List<Node> bestNodes

@Field float alpha
@Field float beta
@Field float rho
@Field File out

def init(n, g, a, b, r, fnodes = 'nodes.txt', ftasks = 'tasks.txt', fout = 'out.txt') {
    antNum = n
    MAX_GEN = g
    alpha = a
    beta = b
    rho = r
    out = new File(fout)

    new File(fnodes).eachLine {
        def s = it.split()
        def args = []
        args << (s[0] as Integer)
        args << (s[1] as float)
        args << (s[2] as float)
        args << (s[3])
        args << (s[4])
        args << (s[5] as Integer)
        args << (s[6] as Integer)
        args << (s[7] as float)
        args << (s[8] as float)
        args << (s[9])
        args << (s[10])
        nodes << (args as Node)
    }

    new File(ftasks).eachLine {
        def s = it.split()
        def args = []
        args << (s[0] as float)
        args << (s[1] as float)
        args << (s[2])
        args << (s[3] as Integer)
        args << (s[4] as Integer)
        args << (s[5])
        11.times {
            tasks << (args as Task)
        }

    }

    nodeNum = nodes.size()
    taskNum = tasks.size()

    // 初始化信息素矩阵
    tha = new float[taskNum][nodeNum]
    for (int i = 0; i < taskNum; i++)
        for (int j = 0; j < nodeNum; j++)
            tha[i][j] = 0.1f

    bestBalance = Integer.MAX_VALUE
    bestResult = []
    bestNodes = []

    ants = new Ant[antNum]

    for (int i = 0; i < antNum; i++) {
        Node[] clone = new Node[nodeNum]
        for (int j = 0; j < nodeNum; j++)
            clone[j] = nodes[j].clone()
        assert !clone[0].is(nodes[0])
        assert !clone[0].is(clone[1])
        assert !clone[0].cpu.is(nodes[0].cpu)
        assert !clone[0].cpu.is(clone[1].cpu)
        ants[i] = new Ant(clone, (tasks as Task[]), alpha, beta)
    }

}

def solve() {
    // 迭代MAX_GEN
    for (int g = 0; g < MAX_GEN; g++) {
        for (int k = 0; k < antNum; k++) {
            float antb
            // 第一个任务随机分配了一个节点
            // 当一只蚂蚁分配完全部的任务，则完成一个完整的TSP

            for (int i = 1; i < taskNum; i++) {
                ants[k].schedule(tha)
            }


            antb = ants[k].antb()
            if (antb < bestBalance) {
                bestBalance = antb
                bestResult = ants[k].result

                ants[k].nodes.eachWithIndex { Node entry, int i ->
                    def a = entry.clone()
                    assert !a.is(entry)
                    assert !a.cpu.is(entry.cpu)
                    bestNodes[i] = a
                }
            }
            // 更新该蚂蚁的信息素变化矩阵
            ants[k].result.eachWithIndex { int j, int i ->
                ants[k].delta[i][j] = antb
            }
        }

        // 更新信息素
        updateTha()

        printOptimal(g)
        // 重新初始化蚂蚁
        for (int k = 0; k < antNum; k++)
            ants[k].init()
    }
}

def updateTha() {
    for (int i = 0; i < taskNum; i++)
        for (int j = 0; j < nodeNum; j++)
            tha[i][j] = tha[i][j] * (1 - rho)

    for (int i = 0; i < taskNum; i++)
        for (int j = 0; j < nodeNum; j++)
            for (int k = 0; k < antNum; k++)
                tha[i][j] += ants[k].delta[i][j]
}

def printOptimal(gen) {
    def s = "The best balance is ${bestBalance}\n" +
            "The best result si ${bestResult}\n"

    out.append(
            '='*8 + gen + '='*8 + '\n' +
            "${s}\n" +
            bestNodes.each {
                it.toString() + '\n'
            }+
            '\n'
    )
}

init(1, 30, 1.0f, 5.0f, 0.5f)
def start = System.currentTimeMillis()
solve()
def end = System.currentTimeMillis()
println (end - start)

