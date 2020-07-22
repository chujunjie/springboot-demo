package com.example.springbootdemo.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 顺序节点实现分布式锁，公平锁避免羊群效应
 *
 * @author chujunjie
 * @date Create in 21:34 2020/7/22
 */
public class DistributedZkLock extends AbstractLock {

    private static final String PATH = "/zklock";
    private static final String SLASH = "/";
    private static final String LOCK = "lock";

    private String currentPath;
    private String beforePath;

    private CountDownLatch countDownLatch = null;

    public DistributedZkLock(ZkClient zkClient) {
        super(zkClient);
        if (!zkClient.exists(PATH)) {
            zkClient.createPersistent(PATH);
        }
    }

    @Override
    public void unlock() {
        if (null != zkClient) {
            zkClient.delete(currentPath);
            zkClient.close();
        }

    }

    @Override
    boolean tryLock() {
        if (StringUtils.isEmpty(currentPath)) {
            // 创建临时顺序节点
            currentPath = zkClient.createEphemeralSequential(PATH + SLASH, LOCK);
        }
        // 获取所有的临时节点，并排序
        List<String> children = zkClient.getChildren(PATH);
        Collections.sort(children);
        if (currentPath.equals(PATH + SLASH + children.get(0))) {
            return true;
        } else {
            // 获取前一位的节点
            int pathLength = PATH.length();
            int wz = Collections.binarySearch(children, currentPath.substring(pathLength + 1));
            beforePath = PATH + SLASH + children.get(wz - 1);
        }
        return false;
    }

    @Override
    void waitLock() {
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                if (null != countDownLatch) {
                    countDownLatch.countDown();
                }
            }

            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
        };
        // 监听前一个节点的变化
        zkClient.subscribeDataChanges(beforePath, listener);
        if (zkClient.exists(beforePath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(beforePath, listener);
    }
}
