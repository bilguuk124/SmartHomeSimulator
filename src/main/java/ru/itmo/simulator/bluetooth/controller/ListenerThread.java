package ru.itmo.simulator.bluetooth.controller;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.itmo.simulator.bluetooth.Connection;

import java.time.Duration;
import java.util.Collections;

@Getter
public class ListenerThread extends Thread {
    private final KafkaConsumer<String, byte[]> consumer;
    private final String topic;
    private final Connection connection;
    private final PhysicalLayer physicalLayer;

    private ListenerThread(KafkaConsumer<String, byte[]> consumer, String topic, PhysicalLayer physicalLayer, Connection connection) {
        this.consumer = consumer;
        this.connection = connection;
        this.topic = topic;
        this.physicalLayer = physicalLayer;
        consumer.subscribe(Collections.singletonList(this.topic));
        start();
    }

    @Override
    public void run() {
        while (true) {
            for (ConsumerRecord<String, byte[]> record : consumer.poll(Duration.ofHours(Long.MAX_VALUE))) {
                byte[] message = physicalLayer.demodulate(record.value());
                physicalLayer.onReceivedPdu(connection, message);
            }
        }
    }

    public static ListenerThread createListenerThread(KafkaConsumer<String, byte[]> consumer, String topic, Connection connection, PhysicalLayer physicalLayer) {
        return new ListenerThread(consumer, topic, physicalLayer, connection);
    }
}
