package com.example.springbootdemo.datastructure;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @Description: 无锁Vector实现
 * @Author: chujunjie
 * @Date: Create in 14:25 2020/2/18
 * @Modified By
 */
public class LockFreeVector<E> {

    private static final int N_BUCKET = 30;
    private static final int FIRST_BUCKET_SIZE = 8;
    private static final int ZERO_NUM_FIRST = Integer.numberOfLeadingZeros(FIRST_BUCKET_SIZE);

    private AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
    private AtomicReference<Descriptor<E>> descriptor;

    public LockFreeVector() {
        buckets = new AtomicReferenceArray<>(N_BUCKET);
        buckets.set(0, new AtomicReferenceArray<>(FIRST_BUCKET_SIZE));
        descriptor = new AtomicReference<>(new Descriptor<>(0, null));
    }

    public void push(E e) {
        Descriptor<E> oldDesc;
        Descriptor<E> newDesc;
        do {
            oldDesc = descriptor.get();
            // 防止上一个线程设置完还未写入，预防性操作
            oldDesc.completeWrite();

            int pos = oldDesc.size + FIRST_BUCKET_SIZE;
            // 根据前导零判断落到哪个数组
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            int bucketIdx = ZERO_NUM_FIRST - zeroNumPos;
            if (null == buckets.get(bucketIdx)) {
                int newLen = buckets.get(bucketIdx - 1).length() << 1;
                buckets.compareAndSet(bucketIdx, null, new AtomicReferenceArray<>(newLen));
            }

            // 获取元素在当前数组中的位置
            int idx = (0x80000000 >>> zeroNumPos) ^ pos;
            newDesc = new Descriptor<>(oldDesc.size + 1, new WriteDescriptor<>(null, e, buckets.get(bucketIdx), idx));
        } while (!descriptor.compareAndSet(oldDesc, newDesc));
        descriptor.get().completeWrite();
    }

    public E get(int index) {
        int pos = index + FIRST_BUCKET_SIZE;
        int zeroNumPos = Integer.numberOfLeadingZeros(pos);
        int bucketIdx = ZERO_NUM_FIRST - zeroNumPos;
        int idx = (0x80000000 >>> zeroNumPos) ^ pos;
        return buckets.get(bucketIdx).get(idx);
    }

    private static class Descriptor<E> {

        public int size;
        volatile WriteDescriptor<E> writeOp;

        public Descriptor(int size, WriteDescriptor<E> writeOp) {
            this.size = size;
            this.writeOp = writeOp;
        }

        public void completeWrite() {
            WriteDescriptor<E> tmpOp = writeOp;
            if (null != tmpOp) {
                tmpOp.doIt();
                writeOp = null;
            }
        }
    }

    private static class WriteDescriptor<E> {

        public E oldV;
        public E newV;
        public AtomicReferenceArray<E> addr;
        public int addrIdx;

        public WriteDescriptor(E oldV, E newV, AtomicReferenceArray<E> addr, int addrIdx) {
            this.oldV = oldV;
            this.newV = newV;
            this.addr = addr;
            this.addrIdx = addrIdx;
        }

        public void doIt() {
            addr.compareAndSet(addrIdx, oldV, newV);
        }
    }
}
