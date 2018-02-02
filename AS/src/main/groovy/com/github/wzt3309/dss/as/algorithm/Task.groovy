package com.github.wzt3309.dss.as.algorithm

class Task {
    RCpu rcpu
    RMem rmem
    RIO rio
    RNet rnet

    Task(hz, used, mem, wait, chunk, net) {
        rcpu = [hz, used]
        rmem = [mem]
        rio = [wait, chunk]
        rnet = [net]
    }


    @Override
    String toString() {
       sprintf('%1.1f %1.2f %6s %3d %3d %6s', rcpu.hz, rcpu.used,
               Utils.bytes2Str(rmem.val), rio.wait, rio.chunk, Utils.bytes2Str(rnet.val))
    }
}

class RCpu {
    def hz
    def used

    RCpu(hz, used) {
        this.hz = hz
        this.used = used
    }
}

class RMem {
    def val    // byte

    RMem(a) {
        val = Utils.str2Bytes(a)
    }
}

class RIO {
    def wait    // ms
    def chunk   // kb

    RIO(wait, chunk) {
        this.wait = wait
        this.chunk = chunk
    }
}

class RNet {
    def val    // byte

    RNet(a) {
        val = Utils.str2Bytes(a)
    }
}