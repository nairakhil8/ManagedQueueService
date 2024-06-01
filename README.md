# Managed Queue Service

The Managed Queue Service is a Java-based application that facilitates a managed queue system, allowing producers to add data to multiple topics and consumers to subscribe to multiple topics. This system is designed to handle concurrent access, ensuring data consistency and proper synchronization between producers and consumers.

Topic Management: Easily manage multiple topics for data streams.
Producers: Add data to one or more topics concurrently.
Consumers: Subscribe to and retrieve data from one or more topics concurrently.
Thread-Safe: Utilizes Java’s concurrency utilities to ensure thread-safe operations.

The project consists of four main classes:

ManagedQueueService.java: Manages the core functionalities of topic creation, data addition, and consumer subscriptions.
Producer.java: Allows data producers to add data to specified topics.
Consumer.java: Allows consumers to subscribe to specified topics and retrieve data.
Main.java: The entry point of the application, demonstrating the usage of producers and consumers with multiple topics.

