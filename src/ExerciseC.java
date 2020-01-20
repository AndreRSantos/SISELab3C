import java.util.ArrayList;

//producer consumer test

public class ExerciseC {
    private final static int MaxTasks = 10;
    private final static int Limit = 1;

    static class TaskList {
        private ArrayList<Integer> contents = new ArrayList<Integer>();
        private int pos = -1;

        public synchronized int get() throws InterruptedException {
            while (contents.isEmpty()) {
                //throw new RuntimeException("The event queue is empty!");
                wait();
            }
            int value = contents.remove(pos);
            pos--;
            notifyAll();
            return value;
        }

        public synchronized void put(int value) throws InterruptedException {
            while (contents.size() >= Limit) {
                wait();
            }
            contents.add(value);
            pos++;
            notifyAll();
        }
    }
    
    static class Consumer extends Thread {
        private TaskList tasks;
        private int number;

        public Consumer(TaskList c, int number) {
            tasks = c;
            this.number = number;
        }
        public void run() {
            int value = 0;
            for (int i = 0; i < MaxTasks; i++) {
                try {
                    value = tasks.get();
                } catch (Exception e) {
                }
                System.out.println("Consumer #" + this.number + " got: " + value);
            }
        }
    }

    static class Producer extends Thread {
        private TaskList tasknumber;
        private int number;

        public Producer(TaskList c, int number) {
            tasknumber = c;
            this.number = number;
        }
        public void run() {
            for (int i = 0; i < MaxTasks; i++) {
                try {
                    tasknumber.put(i);
                } catch (Exception e) {
                }
                System.out.println("Producer #" + this.number + " put: " + i);
                try {
                    sleep((int)(Math.random() * 100));
                } catch (Exception e) { }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TaskList c = new TaskList();
        Producer p1 = new Producer(c, 1);
        Consumer c1 = new Consumer(c, 1);

        p1.start();
        c1.start();
        p1.join();
        c1.join();
    }
}
