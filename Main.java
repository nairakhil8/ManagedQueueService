public class Main {
    public static void main(String[] args) {
        String[] topics = {"topic1", "topic2"};
        ManagedQueueService<Object> queueService = new ManagedQueueService<>(topics);

        // Listing available topics
        System.out.println("Available topics: " + queueService.listTopics());

        Producer<Object> producer1 = new Producer<>(queueService, new String[]{"topic1"}, 1, 2, 3);
        Producer<Object> producer2 = new Producer<>(queueService, new String[]{"topic2"}, "A", "B", "C");
        Producer<Object> producer3 = new Producer<>(queueService, new String[]{"topic1", "topic2"}, "X", "Y", "Z");

        new Thread(producer1).start();
        new Thread(producer2).start();
        new Thread(producer3).start();

        Consumer<Object> consumer1 = new Consumer<>(queueService, 1, "topic1");
        consumer1.subscribe();
        new Thread(consumer1).start();

        Consumer<Object> consumer2 = new Consumer<>(queueService, 2, "topic1", "topic2");
        consumer2.subscribe();
        new Thread(consumer2).start();

        Consumer<Object> consumer3 = new Consumer<>(queueService, 3, "topic2");
        consumer3.subscribe();
        new Thread(consumer3).start();
    }
}
