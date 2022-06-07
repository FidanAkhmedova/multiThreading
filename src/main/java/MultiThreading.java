import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiThreading {
    public static void main(String[] args) {
//        reentrantLockExample();
//        reentrantReadWriteLockExample();
//        countDownLatchExample();
//        semaphoreExample();
        cyclicBarrierExample();
        collections();


    }

    private static void collections() {
        List<String> list = new ArrayList<>();
        List<String> threadSafeList = new Vector<>();
        List<String> threadSafeList1 = new CopyOnWriteArrayList<>();
        List<String> threadSafeList2 = Collections.synchronizedList(list);

        Set<String> set = new HashSet<>();
        Set<String> threadSafeSet = new CopyOnWriteArraySet<>();
        Set<String> threadSafeSet1 = Collections.synchronizedSet(set);
        NavigableSet<String> navigableSet = new TreeSet<>();
        Set<String> threadSafeNavigableSet = new ConcurrentSkipListSet<>();
        Set<String> threadSafeNavigableSet1 = Collections.synchronizedNavigableSet(navigableSet);

        Map<String, String> map = new HashMap<>();
        Map<String, String> threadSafeMap = new Hashtable<>();
        Map<String, String> threadSafeMap1 = new ConcurrentHashMap<>();
        NavigableMap<String, String> navigableMap = new TreeMap<>();
        NavigableMap<String, String> threadSafeNavigableMap = new ConcurrentSkipListMap<>();
    }

    private static void cyclicBarrierExample() {
        int carsCount = 10;
        CyclicBarrier cb1 = new CyclicBarrier(carsCount/2);
        CyclicBarrier cb = new CyclicBarrier(carsCount, () -> {
            System.out.println("====================================");
            System.out.println("BARRIER RELEASED!!!");
            System.out.println("====================================");
        });

        for (int i = 0; i < carsCount; i++) {
            int j = i;
            new Thread(() -> {
                try {
                    System.out.printf("Car #%d preparing\n", j);
                    Thread.sleep((long) (500 + 4000 * Math.random()));
                    System.out.printf("Car #%d on the start line\n", j);
                    cb1.await();
                    System.out.printf("Car #%d riding\n", j);
                    Thread.sleep((long) (500 + 4000 * Math.random()));
                    System.out.printf("Car #%d finishes\n", j);
                    cb.await();
                    System.out.printf("Car #%d rides to box\n", j);
//                cb.getNumberWaiting();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void semaphoreExample() {
        Semaphore semaphore = new Semaphore(4);
        for (int i = 0; i < 20; i++) {
            int j = i;
            new Thread(() -> {
                try {
                    System.out.printf("Car #%d before bridge\n", j);
                    semaphore.acquire();
                    System.out.printf("Car #%d riding on the bridge\n", j);
                    Thread.sleep((long) (500 + 4000 * Math.random()));
                    System.out.printf("Car #%d finished the bridge\n", j);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }

    public static void countDownLatchExample() {
        int threads = 10;
        CountDownLatch cdl = new CountDownLatch(threads / 2);
        for (int i = 0; i < threads; i++) {
            int j = i;
            new Thread(() -> {
                try {
                    System.out.printf("Thread #%d start\n", j);
                    Thread.sleep((long) (500 + 4000 * Math.random()));
                    System.out.printf("Thread #%d finish\n", j);
                    cdl.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        try {
            if ( cdl.await(15, TimeUnit.SECONDS)) {
                System.out.println("All done");
            } else {
                System.out.println("Done nothing");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void reentrantReadWriteLockExample() {
        ReentrantReadWriteLock rrwl = new ReentrantReadWriteLock();
        new Thread(() -> {
            try {
                rrwl.readLock().lock();
                System.out.println("Read 1 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Read 1 finish");
                rrwl.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                rrwl.readLock().lock();
                System.out.println("Read 2 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Read 2 finish");
                rrwl.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                rrwl.readLock().lock();
                System.out.println("Read 3 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Read 3 finish");
                rrwl.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                rrwl.writeLock().lock();
                System.out.println("Write 1 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Write 1 finish");
                rrwl.writeLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                rrwl.writeLock().lock();
                System.out.println("Write 2 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Write 2 finish");
                rrwl.writeLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                rrwl.readLock().lock();
                System.out.println("Read 4 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Read 4 finish");
                rrwl.readLock().unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                rrwl.readLock().lock();
                System.out.println("Read 5 start");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Read 5 finish");
                rrwl.readLock().unlock();
            }
        }).start();
    }

    private static void reentrantLockExample() {
        Lock lock = new ReentrantLock();

        new Thread(() -> {
            System.out.println("Before lock 1");
            try {
                lock.lock();
                System.out.println("Got lock 1");
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Releasing lock 1");
                lock.unlock();
            }
        }).start();

//        new Thread(() -> {
//            System.out.println("Before lock 2");
//            try {
//                lock.lock();
//                System.out.println("Got lock 2");
//                Thread.sleep(3500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                System.out.println("Releasing lock 2");
//                lock.unlock();
//            }
//        }).start();

        new Thread(() -> {
            System.out.println("Before lock 3");
//            if (lock.tryLock()) {
            try {
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Got lock 3");
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("Releasing lock 3");
                        lock.unlock();
                    }
                } else {
                    System.out.println("Do nothing");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}