package ru.itmo.simulator.bluetooth.host.gatt.services;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.simulator.bluetooth.host.att.Attribute;
import ru.itmo.simulator.bluetooth.host.att.Permissions;
import ru.itmo.simulator.bluetooth.host.gatt.GattDefaults;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.Characteristic;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class Service extends Attribute {
    private static final byte[] BASE_UUID = {
            0x00, 0x00, 0x10, 0x00,
            (byte) 0x80, 0x00, 0x00, (byte) 0x80,
            0x5F, (byte) 0x9B, 0x34, (byte) 0xFB
    };


    private boolean primary;
    private UUID uuid;
    private List<Characteristic> characteristics;
    private List<Service> includedServices;

    public Service(UUID uuid, List<Characteristic> characteristics, boolean primary, List<Service> services) {
        UUID serviceType = primary ? GattDefaults.GATT_PRIMARY_SERVICE_ATTRIBUTE_TYPE : GattDefaults.GATT_SECONDARY_SERVICE_ATTRIBUTE_TYPE;
        super(serviceType, Permissions.READABLE, toPduBytes(uuid));
        this.uuid = uuid;
        this.primary = primary;
        this.characteristics = characteristics;
        this.includedServices = services;
    }

    public Service(UUID uuid, List<Characteristic> characteristics) {
        this(uuid, characteristics, true, new ArrayList<>());
    }

    public Service(UUID uuid, List<Characteristic> characteristics, boolean primary) {
        this(uuid, characteristics, primary, new ArrayList<>());
    }

    public Service(UUID uuid, List<Characteristic> characteristics, List<Service> services) {
        this(uuid, characteristics, true, services);
    }

    public byte[] getAdvertisingData(){
        return null;
    }

    @Override
    public String toString(){
        return String.format(
                "Service(handle=0x%04X, end=0x%04X, uuid=%s)%s",
                getHandle(),
                uuid.toString(),
                primary ? "" : "*"
        );
    }

    private static byte[] toPduBytes(UUID uuid){
        byte[] uuidBytes = uuid.toString().getBytes();
        return toBytes(uuidBytes.length == 4, uuidBytes);
    }

    private static byte[] toBytes(boolean force128, byte[] uuid){
        if(!force128){
            return uuid;
        }

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(BASE_UUID);
        buffer.put(uuid);

        if (uuid.length == 2){
            buffer.put(new byte[]{0x00, 0x00});
        }

        return buffer.array();
    }


}
