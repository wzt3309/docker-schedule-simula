package com.github.wzt3309.dss.as.algorithm

import groovy.transform.AutoClone

/**
 * docker 节点
 */
@AutoClone
class Node {
    Cpu cpu
    Mem mem
    IO io
    Net net

    Node(co, hz, cu,         /* cpu */
         mt, ma,             /* mem */
         rpm, mtr, aat, iu,  /* io */
         nt, na              /* net */
         ) {
        cpu = [co, hz, cu]
        mem = [mt, ma]
        io = [rpm, mtr, aat, iu]
        net = [nt, na]
    }

    def handle(Task task) {
        cpu.handle(task.rcpu)
        mem.handle(task.rmem)
        io.handle(task.rio)
        net.handle(task.rnet)
    }

    float tryTask(Task task) {
        Node node = this.clone()
        node.handle(task)
        return (cpu.used + mem.used + io.used + net.used) / 4
    }

    boolean isTabu() {
        return cpu.used >= 1.0 && mem.used >= 1.0 && io.used >= 1.0 && net.used >= 1.0
    }

    @Override
    String toString() {
        sprintf('%.3f' * 3, cpu.used, mem.used, io.used, net.used)
    }
}

@AutoClone
class Cpu {
    final def core     // 逻辑核心数
    final def hz       // 主频
    def used           // 使用率0.0~1.0

    Cpu(core, hz, used) {
        this.core = core
        this.hz = hz
        this.used = used
    }

    def handle(RCpu rcpu) {
        def tused = ((rcpu.hz / hz) * rcpu.used) / core
        if (tused <= 1.0 - used) {
            used += tused
        } else {
            used = 1.0
        }
    }
}

@AutoClone
class Mem {
    final def total        // 单位字节
    def avail              // 未被使用的字节
    def used

    Mem(total, avail) {
        this.total = Utils.str2Bytes(total)
        this.avail = Utils.str2Bytes(avail)
    }

    def handle(RMem rmem) {
        if (avail >= rmem.val) {
            avail -= rmem.val
        } else {
            avail = 0
        }
    }

    def getUsed() {
        (total - avail) / total
    }

}

@AutoClone
class IO {
    final def rpm     // 硬盘转速
    final def mtr     // 硬盘最大持续传输速率
    final def aat     // 硬盘平均寻址时间
    def used

    IO(rpm, mtr, aat, used) {
        this.rpm = rpm
        this.mtr = mtr
        this.aat = aat
        this.used = used
    }

    def handle(RIO rio) {
        def iops = { wait, chunk ->
            def idle = (aat + 60 / (rpm * 2) + wait + chunk / (mtr * 1000))
            return 1000 / idle
        }
        def miops = { chunk ->
            iops(0, chunk)
        }
        def tused = iops(rio.wait, rio.chunk) / miops(rio.chunk)
        if (tused <= 1.0 - used) {
            used += tused
        } else {
            used = 1.0
        }
    }
}

@AutoClone
class Net {
    final def total
    def avail
    def used

    Net(total, avail) {
        this.total = total
        this.avail = avail
    }

    def handle(RNet rnet) {
        if (avail >= rnet.val) {
            avail -= rnet.val
        } else {
            avail = 0
        }
    }

    def getUsed() {
        (total - avail) / total
    }
}