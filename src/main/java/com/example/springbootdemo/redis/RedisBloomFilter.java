package com.example.springbootdemo.redis;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;

/**
 * @Description: 布隆过滤器
 * @Author: chujunjie
 * @Date: Create in 23:38 2019/3/11
 * @Modified By
 */
public class RedisBloomFilter {

    /**
     * 插入数据量
     */
    private static final int expectedInsertions = 100;

    /**
     * 期望的误判率
     */
    private static final double fpp = 0.01;

    /**
     * KEYWORD
     */
    private static final String KEYWORD = "bloom";

    /**
     * bit数组长度
     */
    private static long numBits;

    /**
     * hash函数数量
     */
    private static int numHashFunctions;

    static {
        numBits = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        for (int i = 0; i < 100; i++) {
            long[] indexes = getIndexes(String.valueOf(i));
            for (long index : indexes) {
                jedis.setbit(KEYWORD, index, true);
            }
        }
        for (int i = 0; i < 100; i++) {
            long[] indexes = getIndexes(String.valueOf(i));
            for (long index : indexes) {
                Boolean isContain = jedis.getbit(KEYWORD, index);
                if (!isContain) {
                    System.out.println(i + "肯定没有重复");
                }
            }
            System.out.println(i + "可能重复");
        }
    }

    /**
     * 根据key获取bitmap下标
     *
     * @param key key
     * @return long[]
     */
    private static long[] getIndexes(String key) {
        long hash1 = hash(key);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            long combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            result[i] = combinedHash % numBits;
        }
        return result;
    }

    /**
     * hash
     *
     * @param key key
     * @return long
     */
    private static long hash(String key) {
        return Hashing.murmur3_128().hashObject(key, Funnels.stringFunnel(StandardCharsets.UTF_8)).asLong();
    }

    /**
     * 计算hash函数个数
     *
     * @param n n
     * @param m m
     * @return int
     */
    private static int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    /**
     * 计算bit数组长度
     *
     * @param n n
     * @param p p
     * @return long
     */
    private static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
}
