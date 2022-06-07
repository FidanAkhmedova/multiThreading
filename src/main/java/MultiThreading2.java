import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MultiThreading2 {
    public static final int CARS_COUNT = 10;
    public static Car[] cars = new Car[CARS_COUNT];

    public static void main(String[] args) {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
Thread thread;
        String str = "====================================";
        CyclicBarrier cb1 = new CyclicBarrier(5, () -> {
            System.out.println(str);
        });
        CyclicBarrier cb = new CyclicBarrier(10, () -> {
            System.out.println(str);
        });

        for (int i = 0; i < 10; i++) {
            int j = i;
           // new Car(race, 15)
        new Thread(() -> {
                try {
                    System.out.printf("Car %s готовится... \n", Thread.currentThread().getName());
                    Thread.sleep((long) (500 + 4000 * Math.random()));
                    System.out.printf("Car %s готов. \n", Thread.currentThread().getName());

                    cb.await(10, SECONDS);
                    if (j == 1)
                        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

                    cb.await();
                    for (int l = 0; l < race.getStages().size(); l++) {
                        race.getStages().get(l).go(cars[l]);
                     }
                }
                catch (InterruptedException e){//| BrokenBarrierException e) {
                    e.printStackTrace();
                }
                catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        }
    }
}
 class Car implements Runnable {

    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    public String str = "====================================";
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

     CyclicBarrier cb1 = new CyclicBarrier(5, () -> {
         System.out.println(str);
     });
     CyclicBarrier cb = new CyclicBarrier(10, () -> {
         System.out.println(str);
     });
     public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
       @Override
    public void run() {
    }
}
 abstract class Stage {
    protected int length;
    protected String description;
    public String getDescription() {
        return description;
    }
    public abstract void go(Car c);
}
 class Road extends Stage {
     int n = 0;
     String winner = "";
     private String str = "====================================";
     CyclicBarrier cb1 = new CyclicBarrier(5, () -> {
         System.out.println(str);
     });
     CyclicBarrier cb = new CyclicBarrier(10, () -> {
         System.out.println(str);
     });
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " 88888888888888начал этап: " + description);
            Thread.sleep((long) (500 + 4000 * Math.random()));
            Thread.sleep(length / c.getSpeed() * 1000);
            System.out.println(Thread.currentThread().getName() + " закончил этап: " + description);
            cb1.await();

            if (length == 40){
                if (n == 9)
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!\n" + winner);
                else if (n == 0)
                    winner = "ПОБЕДИЛ " + Thread.currentThread().getName();
                n++;
            }
        }

        catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
 class Tunnel extends Stage {

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

     private String str = "====================================";
     CyclicBarrier cb1 = new CyclicBarrier(5, () -> {
         System.out.println(str);
     });
     CyclicBarrier cb = new CyclicBarrier(10, () -> {
         System.out.println(str);
     });
    @Override
    public void go(Car c) {
        try {
            try {
               // cb1.await();
                System.out.println(Thread.currentThread().getName() + " готовится к этапу(ждет): " + description);// cb1.await();
                System.out.println(Thread.currentThread().getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println(Thread.currentThread().getName() + " закончил этап: " +
                        description);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 class Race {
    private ArrayList<Stage> stages;
    public ArrayList<Stage> getStages() { return stages; }
    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }
}
