/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.meshware.common.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 时间等待
 */
public interface Waiter {

    /**
     * 等待时间
     *
     * @param time     time
     * @param timeUnit time unit
     * @throws InterruptedException exception
     */
    void await(long time, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 唤醒
     */
    void wakeup();

    /**
     * 采用对象的await等待
     */
    class MutexWaiter implements Waiter {

        protected Object mutex;

        /**
         * 构造函数
         */
        public MutexWaiter() {
            this(new Object());
        }

        /**
         * 构造函数
         *
         * @param mutex mutex
         */
        public MutexWaiter(final Object mutex) {
            this.mutex = mutex == null ? new Object() : mutex;
        }

        @Override
        public void await(final long time, final TimeUnit timeUnit) throws InterruptedException {
            synchronized (mutex) {
                mutex.wait(timeUnit.toMillis(time));
            }
        }

        @Override
        public void wakeup() {
            synchronized (mutex) {
                mutex.notifyAll();
            }
        }

    }

    /**
     * 采用线程Sleep等待
     */
    class SleepWaiter implements Waiter {

        @Override
        public void await(final long time, final TimeUnit timeUnit) throws InterruptedException {
            Thread.sleep(timeUnit.toMillis(time));
        }

        @Override
        public void wakeup() {

        }

    }
}
