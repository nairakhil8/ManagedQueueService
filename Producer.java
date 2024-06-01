public class Producer<T> implements Runnable {

    private final ManagedQueueService<T> queueService;
    private final String[] topics;
    private final T[] dataToProduce;

    @SafeVarargs
    public Producer(ManagedQueueService<T> queueService, String[] topics, T... dataToProduce) {
        this.queueService = queueService;
        this.topics = topics;
        this.dataToProduce = dataToProduce;
    }

    @Override
    public void run() {
        for (T data : dataToProduce) {
            for (String topic : topics) {
                queueService.addData(topic, data);
                System.out.println("Producer: Added " + data + " to topic " + topic);
            }
        }
    }
}
