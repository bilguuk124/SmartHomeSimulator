//package ru.itmo.simulator.bluetooth.host.gatt;
//
//import lombok.Getter;
//import lombok.Setter;
//import ru.itmo.simulator.bluetooth.Connection;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.Callable;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.function.Consumer;
//
//@Getter
//@Setter
//public abstract class CharacteristicAdapter {
//
//    private Callable<CompletableFuture<byte[]>> readValue;
//    private Consumer<byte[]> writeValue;
//    private Characteristics wrappedCharacteristics;
//    private Map<Consumer<byte[]>, Consumer<byte[]>> subscribers;
//
//    public CharacteristicAdapter(Characteristics characteristics) {
//        this.wrappedCharacteristics = characteristics;
//        subscribers = new HashMap<>();
//
//        this.readValue = this::readEncodedValue;
//        this.writeValue = this::writeEncodedValue;
//    }
//
//    private CompletableFuture<byte[]> readEncodedValue(Connection connection) throws ExecutionException, InterruptedException {
//        return CompletableFuture.completedFuture(encodeValue(wrappedCharacteristics.readValue().get()));
//    }
//
//    private void writeEncodedValue(Connection connection, byte[] value){
//        wrappedCharacteristics.writeValue(connection, this.decodeValue(value));
//    }
//
//    private byte[] encodeValue(byte[] value) {
//        return value;
//    }
//
//    private byte[] decodeValue(byte[] value){
//        return value;
//    }
//
//
//
//}
//TODO wtf?