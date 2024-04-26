package ru.itmo.simulator.bluetooth.host.gatt;

import org.javatuples.Pair;
import org.springframework.scheduling.annotation.Async;
import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.Device;
import ru.itmo.simulator.bluetooth.host.att.Attribute;
import ru.itmo.simulator.bluetooth.host.att.pdu.AttPdu;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.CharacteristicDeclaration;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.Characteristic;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.Descriptor;
import ru.itmo.simulator.bluetooth.host.gatt.services.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import static ru.itmo.simulator.bluetooth.host.att.ATT.ATT_CID;
import static ru.itmo.simulator.bluetooth.host.gatt.GattDefaults.GATT_CHARACTERISTIC_ATTRIBUTE_TYPE;
import static ru.itmo.simulator.bluetooth.host.gatt.GattDefaults.GATT_PRIMARY_SERVICE_ATTRIBUTE_TYPE;

public class GattServer {
    private Device device;
    private List<Attribute> attributes;
    private List<Service> services;
    private Map<Integer, Map<Integer, byte[]>> subscribers;
    private ConcurrentHashMap<Integer, Semaphore> indicationSemaphores;
    private Map<Integer, Future<AttPdu>> pendingConfirmations;

    public GattServer(Device device) {
        this.device = device;
        attributes = new ArrayList<>();
        services = new ArrayList<>();
        subscribers = new HashMap<>();
        indicationSemaphores = new ConcurrentHashMap<>();
        pendingConfirmations = new HashMap<>();
    }

    public void sendGattPdu(int connectionHandle, byte[] pdu){
        device.sendL2CapPdu(connectionHandle, ATT_CID, pdu);
    }

    private int nextHandle(){
        return attributes.size() + 1;
    }

    public Map<Attribute, byte[]> getAdvertisingServiceData() {
        Map<Attribute, byte[]> result = new HashMap<>();
        for(Attribute attribute : attributes){
            if (attribute instanceof Service){
                byte[] data = ((Service) attribute).getAdvertisingData();
                if (data != null) {
                    result.put(attribute, data);
                }
            }
        }
        return result;
    }

    public Optional<Attribute> getAttribute(int handle) {
        return Optional.ofNullable(attributes.stream().filter(att -> att.getHandle() == handle).findFirst().orElse(null));
    }

    public <T> Optional<T> getAttributeGroupType(int handle, Class<T> groupType){
        for (Attribute attribute : attributes) {
            if (groupType.isInstance(attribute) && attribute.getHandle() <= handle && handle >= attribute.getEndGroupHandle()) {
                return Optional.of(groupType.cast(attribute));
            }
        }
        return Optional.empty();
    }

    public Optional<Service> getServiceAttribute(UUID serviceUUID){
        for(Attribute attribute : attributes){
            if (attribute.getType() == GATT_PRIMARY_SERVICE_ATTRIBUTE_TYPE &&
            attribute instanceof Service &&
                    ((Service) attribute).getUuid().equals(serviceUUID)){
                return Optional.of((Service) attribute);
            }
        }
        return Optional.empty();
    }

    public Optional<Pair<CharacteristicDeclaration, Characteristic>> getCharacteristicAttributes(UUID serviceUUID, UUID characteristicUUID){
        Service service = getServiceAttribute(serviceUUID).orElseThrow(() -> new IllegalArgumentException("Service not found"));

        for (int handle = service.getHandle(); handle <= service.getEndGroupHandle(); handle++) {
            Optional<Attribute> attribute = getAttribute(handle);
            if (attribute.isPresent() &&
            attribute.get().getType() == GATT_CHARACTERISTIC_ATTRIBUTE_TYPE &&
            attribute.get() instanceof CharacteristicDeclaration characteristicDeclaration &&
            Objects.equals(((CharacteristicDeclaration) attribute.get()).getCharacteristic().getType(), characteristicUUID)) {
                Characteristic characteristic = (Characteristic) getAttribute(characteristicDeclaration.getCharacteristic().getHandle()).orElse(null);
                if (characteristic != null) {
                    return Optional.of(new Pair<>(characteristicDeclaration, characteristic));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Descriptor> getDescriptorAttribute(UUID serviceUUID, UUID characteristicsUUID, UUID descriptorUUID){
        Pair<CharacteristicDeclaration, Characteristic> pair = getCharacteristicAttributes(serviceUUID, characteristicsUUID).orElseThrow(() -> new IllegalArgumentException("Characteristic not found"));
        Characteristic characteristic = pair.getValue1();
        for (int handle = characteristic.getHandle() + 1; handle <= characteristic.getEndGroupHandle(); handle++) {
            Attribute attribute = getAttribute(handle).orElseThrow(() -> new IllegalArgumentException("Attribute not found"));
            if (attribute != null && attribute.getType() == descriptorUUID) {
                return Optional.of((Descriptor) attribute);
            }
        }
        return Optional.empty();
    }

    @Async
    public void notifySubscribers(Connection connection, Attribute attribute, byte[] value, boolean force){
//        CompletableFuture<Void> future = new CompletableFuture<>();
//        CompletableFuture.runAsync(() -> {
//            try{
//
//            }
//        })
    }

    @Async
    public void indicateSubscribers(Connection connection, Attribute attribute, byte[] value, boolean force){}

    @Async
    public void notifyOrIndicateSubscribers(Connection connection, Attribute attribute, byte[] value, boolean force){}

    public void onDisconnect(Connection connection){}

    public void onGattPdu(Connection connection, AttPdu attPdu){}

}
