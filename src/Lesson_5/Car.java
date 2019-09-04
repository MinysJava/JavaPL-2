package Lesson_5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static int win = 0;
    public static CountDownLatch cdl;
    public static CountDownLatch cdl2;
    public static CountDownLatch cdl3;
    static Semaphore smp;
    static Semaphore smp1 = new Semaphore( 1);

    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, int carsN) {

        cdl = new CountDownLatch(carsN);
        cdl2 = new CountDownLatch(carsN);
        cdl3 = new CountDownLatch(carsN);
        smp = new Semaphore(carsN / 2 );

        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            cdl.countDown();
            cdl.await();

            System.out.println(this.name + " готов");
            cdl2.countDown();
            cdl2.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {

            if (race.getStages().get(i).getClass().equals(Tunnel.class) ){
                race.getStages().get(i).go(this);
            }else {
                race.getStages().get(i).go(this);
            }
        }
        cdl3.countDown();
        try {
            smp1.acquire();
            if (win == 0){
                System.out.println(this.name + " WIN!!!!!!!!!!");
                win++;
            }
            smp1.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
