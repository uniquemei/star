package com.fly.jiejing.units;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorManager {

    private ExecutorManager() {
        init();
    }

    private static ExecutorManager instance;

    //
    public static ExecutorManager getInstance() {
        synchronized (ExecutorManager.class) {
                if (instance == null) {
                    // 锁住类，避免刚开始的时候多个线程创建多个对象
                    synchronized (ExecutorManager.class) {
                        if (instance == null) {
                            instance = new ExecutorManager();
                        }
                    }
                }
            }
            return instance;
        }

        private ExecutorService executorService;

        private void init () {
            int max = 8;
            int num = 2 * Runtime.getRuntime().availableProcessors() + 1;
            num = num > 8 ? max : num;
            // 创建一个定长的线程池，可控制现成的最大并发数，超出的线程会在队列中等待
            executorService = Executors.newFixedThreadPool(num);

        }

        public void execute (Runnable runable){

            this.executorService.execute(runable);
        }

        public ExecutorService getExecutors () {
            return this.executorService;
        }

    }