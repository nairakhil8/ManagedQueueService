import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Queue;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class ManagedQueueService<T> {

    private final Map<String, BlockingQueue<T>> topicQueues = new ConcurrentHashMap<>();
    private final Map<String, Map<Integer, Integer>> consumerDataPositions = new ConcurrentHashMap<>();

    public ManagedQueueService(String[] topics) {
        for (String topic : topics) {
            topicQueues.put(topic, new LinkedBlockingQueue<>());
            consumerDataPositions.put(topic, new ConcurrentHashMap<>());
        }
    }

    public void addData(String topic, T data) {
        BlockingQueue<T> queue = topicQueues.get(topic);
        if (queue != null) {
            queue.offer(data);
            // Notify all waiting consumers about the new data
            synchronized (queue) {
                queue.notifyAll();
            }
        } else {
            throw new IllegalArgumentException("Topic does not exist: " + topic);
        }
    }

    public void subscribe(String topic, int consumerId) {
        if (!topicQueues.containsKey(topic)) {
            throw new IllegalArgumentException("Topic does not exist: " + topic);
        }
        Map<Integer, Integer> positions = consumerDataPositions.get(topic);
        if (positions != null) {
            synchronized (positions) {
                positions.put(consumerId, 0);
            }
        }
    }

    public T retrieveData(String topic, int consumerId) {
        BlockingQueue<T> queue = topicQueues.get(topic);
        Map<Integer, Integer> positions = consumerDataPositions.get(topic);

        if (queue == null || positions == null) {
            throw new IllegalArgumentException("Topic does not exist");
        }

        Integer position;
        synchronized (positions) {
            position = positions.get(consumerId);
            if (position == null) {
                throw new IllegalStateException("Consumer not subscribed to topic: " + topic);
            }
        }

        synchronized (queue) {
            while (queue.size() <= position) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }

            List<T> list = new ArrayList<>(queue);
            T data = list.get(position);
            synchronized (positions) {
                positions.put(consumerId, position + 1);
            }
            return data;
        }
    }

    public Set<String> listTopics() {
        return topicQueues.keySet();
    }
}
