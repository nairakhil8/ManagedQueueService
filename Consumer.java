import java.util.HashSet;
import java.util.Set;

public class Consumer<T> implements Runnable {

    private final ManagedQueueService<T> queueService;
    private final Set<String> topics;
    private final int consumerId;

    public Consumer(ManagedQueueService<T> queueService, int consumerId, String... topics) {
        this.queueService = queueService;
        this.consumerId = consumerId;
        this.topics = new HashSet<>();
        for (String topic : topics) {
            this.topics.add(topic);
        }
    }

    public void subscribe() {
        for (String topic : topics) {
            queueService.subscribe(topic, consumerId);
            System.out.println("Consumer " + consumerId + ": Subscribed to topic " + topic);
        }
    }

    @Override
    public void run() {
        while (true) {
            for (String topic : topics) {
                T data = queueService.retrieveData(topic, consumerId);
                if (data != null) {
                    System.out.println("Consumer " + consumerId + ": Retrieved " + data + " from topic " + topic);
                }
            }
        }
    }
}
