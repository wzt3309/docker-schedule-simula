package com.github.wzt3309.dss.as.algorithm

class Utils {
    static final long KB = 1l << 10
    static final long MB = 1l << 20
    static final long GB = 1l << 30
    static final long TB = 1l << 40
    def static final P = ~/(\d+)((?i)[K|M|G|T]?B)/

    static long str2Bytes(a) {
        def v = (a =~ P)[0][1] as long
        def u = 'B,KB,MB,GB,TB'.split(',').find { x ->
            x =~ ~/(?i)${(a =~ P)[0][2]}/
        }
        switch (u) {
            case 'KB':
                v *= KB
                break
            case 'MB':
                v *= MB
                break
            case 'GB':
                v *= GB
                break
            case 'TB':
                v *= TB
                break
            default: break
        }
        return v
    }

    static String bytes2Str(final a) {
        if (a == 0) return '0B'
        def res
        long h = Long.highestOneBit(a)
        long tz = Long.numberOfTrailingZeros(h)
        if (tz < 10) res = a + 'B'
        else if (tz >= 10 && tz < 20) res = a / KB + 'KB'
        else if (tz >= 20 && tz < 30) res = a / MB + 'MB'
        else if (tz >= 30 && tz < 40) res = a / GB + 'GB'
        else res = a / TB + 'TB'
    }

    static int roulette(p) {
        float pos = new Random().nextFloat()
        float sum = 0
        for (int i = 0; i < p.size(); i++) {
            sum += p[i]
            if (sum >= pos) {
                return i
            }
        }
    }

    static float stander(arr) {
        def n = arr.size()
        def avg = avg(arr)
        def st = 0
        arr.each {
            st += Math.pow(it - avg, 2) / n
        }
        return st
    }

    static float standerAll(Object[] arrs) {
        def tmp = []
        arrs.each {
            tmp << stander(it)
        }
        return avg(tmp)
    }

    static float avg(arr) {
        return arr.sum() / arr.size()
    }
}

//print Utils2.standerAll([1, 2, 3], [1, 2, 3], [1, 2, 3])