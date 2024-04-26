package ru.itmo.simulator.bluetooth.host.gatt;

import org.springframework.scheduling.annotation.Async;
import ru.itmo.simulator.bluetooth.host.CacheEntry;
import ru.itmo.simulator.bluetooth.Device;
import ru.itmo.simulator.bluetooth.host.att.Attribute;
import ru.itmo.simulator.bluetooth.host.att.pdu.AttPdu;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.Characteristic;
import ru.itmo.simulator.bluetooth.host.gatt.services.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GattClient {
    private final Device device;

    private List<Service> services;
    private Map<Integer, CacheEntry> cache;
    private volatile AttPdu pendingRequest;
    private volatile Future<AttPdu> pendingResponse;
    private Semaphore requestSemaphore;

    public GattClient(Device device) {
        this.device = device;
        services = new ArrayList<>();
        cache = new HashMap<>();
        pendingRequest = null;
        pendingResponse = null;
        requestSemaphore = new Semaphore(1);
    }

    public void sendGattPdu(byte[] pdu){
        //connection.sendL2CapPdu(ATT_CID, pdu);
        //TODO
    }

    @Async
    public void sendCommand(AttPdu command){
        sendGattPdu(command.toBytes());
    }

    @Async
    public Future<AttPdu> sendRequest(AttPdu request){
        AttPdu response = null;

        try{
            requestSemaphore.acquire();
            if (pendingRequest != null || pendingResponse != null) {
                throw new InterruptedException("The is already pending request/response");
            }

            pendingRequest = request;
            pendingResponse = new CompletableFuture<>();

            response = pendingResponse.get(GattDefaults.GATT_REQUEST_TIMEOUT, TimeUnit.SECONDS);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            pendingRequest = null;
            pendingResponse = null;
        }
        return CompletableFuture.completedFuture(response);
    }

    public void sendConfirmation(AttPdu pdu){
        sendGattPdu(pdu.toBytes());
    }

    public List<Service> getServicesByUUID(UUID uuid){
        return services.stream().filter(e -> e.getUuid().equals(uuid)).toList();
    }

    public List<Characteristic> getCharacteristicsByUUID(UUID uuid){
//        return services;
        throw new RuntimeException("Not implemented");
    }

    public void onServiceDiscovered(Service service){
        boolean alreadyKnown = false;
        for(Service s : services){
            if(s.getHandle() == service.getHandle()){
                alreadyKnown = true;
                break;
            }
        }
        if(!alreadyKnown){
            services.add(service);
        }
    }

    @Async
    public Future<List<Service>> discoverServices(){
        // TODO
        throw new RuntimeException();
    }

    @Async
    public void subscribe(Characteristic characteristic){

    }

    @Async
    public void unsubscribe(Characteristic characteristic){

    }

    @Async
    public CompletableFuture<byte[]> readValue(Attribute attribute, boolean noLongRead){
        throw new RuntimeException("Not implemented");
    }

    @Async
    public CompletableFuture<byte[]> readValue(int handle, boolean noLongRead){
        throw new RuntimeException("Not implemented");
    }

    @Async
    public CompletableFuture<byte[]> readValue(int handle){
        return readValue(handle, false);
    }

    @Async
    public CompletableFuture<byte[]> readValue(Attribute att){
        return readValue(att, false);
    }

    @Async
    public CompletableFuture<List<byte[]>> readCharacteristicsByUUID(UUID uuid){
        throw new RuntimeException("Not implemented");
    }

    @Async
    public void writeValue(Attribute attribute, byte[] value, boolean withResponse){}

    @Async
    public void writeValue(int handle, byte[] value, boolean withResponse){}

    @Async
    public void writeValue(int handle, byte[] value){
        writeValue(handle, value, false);
    }

    @Async
    public void writeValue(Attribute attribute, byte[] value){
        writeValue(attribute, value, false);
    }

    public void onGattPdu(AttPdu pdu){

    }

    public void handleNotification(){

    }

    public void handleIndication(){}

    private void cacheValue(int handle, byte[] value){}

}
