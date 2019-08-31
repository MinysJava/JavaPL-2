package Lesson_4;

public class MyThreadClass {
    private final Object lock = new Object();
    private volatile char symbol = 'A';

    public static void main(String[] args) {
        MyThreadClass mc = new MyThreadClass();
        Thread t1 = new Thread(() -> {
            mc.printA();
        });
        Thread t2 = new Thread(() -> {
            mc.printB();
        });
        Thread t3 = new Thread(() -> {
            mc.printC();
        });
        t1.start();
        t2.start();
        t3.start();
    }

    public void printA() {
        synchronized (lock){
            try {
                for (int i = 0; i < 3; i++) {
                    while (symbol != 'A') {
                        lock.wait();
                    }
                    System.out.print("A");
                    symbol = 'B';
                    lock.notifyAll();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (lock){
            try {
                for (int i = 0; i < 3; i++) {
                    while (symbol != 'B') {
                        lock.wait();
                    }
                    System.out.print("B");
                    symbol = 'C';
                    lock.notifyAll();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (lock){
            try {
                for (int i = 0; i < 3; i++) {
                    while (symbol != 'C') {
                        lock.wait();
                    }
                    System.out.print("C");
                    symbol = 'A';
                    lock.notifyAll();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
