package ru.itmo.simulator.bluetooth.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.itmo.simulator.bluetooth.Connection;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Component
@Scope("prototype")
public class PhysicalLayer {
    private final LinkLayer linkLayer;
    private final Map<Connection, ListenerThread> listenerThreads;
    private KafkaAdmin kafkaAdmin;
    private AdminClient adminClient;

    private PhysicalLayer(LinkLayer linkLayer) {
        this.linkLayer = linkLayer;
        listenerThreads = new ConcurrentHashMap<>();
    }

    protected byte[] modulate(byte[] pdu){
        throw new RuntimeException("Not implemented");
    }

    protected byte[] demodulate(byte[] pdu){
        throw new RuntimeException("Not implemented");
    }

    @KafkaListener(groupId = "advertisement", topics="advertising1")
    public void getAdvertisement1(byte[] pdu){
        linkLayer.getAdvertisement(pdu);
    }

    @KafkaListener(groupId = "advertisement", topics="advertising2")
    public void getAdvertisement2(byte[] pdu){
        linkLayer.getAdvertisement(pdu);

    }

    @KafkaListener(groupId = "advertisement", topics="advertising3")
    public void getAdvertisement3(byte[] pdu){
        linkLayer.getAdvertisement(pdu);
    }

    public void createListener(Connection connection, String topic){
        try{
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");
            props.put("group.id", "data");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

            if (listenerThreads.values().stream().anyMatch(e -> e.getTopic().equalsIgnoreCase(topic))) {
                throw new TopicExistsException(topic);
            }

            if (adminClient.listTopics().names().get().contains(topic)) {
                NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
                adminClient.createTopics(List.of(newTopic));
            }

            KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
            ListenerThread listenerThread = ListenerThread.createListenerThread(consumer, topic, connection, this);
            listenerThreads.put(connection, listenerThread);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendFrame(Connection connection, byte[] pdu){
        byte[] modulatedPdu = modulate(pdu);
        String topic = listenerThreads.get(connection).getTopic();

    }

    public void onReceivedPdu(Connection connection, byte[] message) {
        linkLayer.getFrame(connection, message);
    }

    @Autowired
    public void setKafkaAdmin(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @Autowired
    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    protected static PhysicalLayer create(LinkLayer linkLayer) {
        return new PhysicalLayer(linkLayer);
    }
}
