package ru.itmo.simulator.bluetooth.host.gatt;

import java.nio.ByteBuffer;
import java.util.UUID;

public final class GattDefaults {

    public static final UUID GATT_PRIMARY_SERVICE_ATTRIBUTE_TYPE = from16Bits(0x2800, "Primary Service");
    public static final UUID GATT_SECONDARY_SERVICE_ATTRIBUTE_TYPE = from16Bits(0x2801, "Secondary Service");
    public static final UUID GATT_INCLUDE_ATTRIBUTE_TYPE = from16Bits(0x2802, "Include");
    public static final UUID GATT_CHARACTERISTIC_ATTRIBUTE_TYPE = from16Bits(0x2803, "Characteristic");

    public static final UUID GATT_DEVICE_NAME_CHARACTERISTICS = from16Bits(0x2A00, "Device Name");
    public static final UUID GATT_GENERIC_ACCESS_SERVICE = from16Bits(0x1800, "Generic Access");
    public static final long GATT_REQUEST_TIMEOUT = 30;


    private static UUID fromBytes(byte[] uuidBytes, String name){
        if (uuidBytes.length == 2 || uuidBytes.length == 4 || uuidBytes.length == 16) {
            return UUID.nameUUIDFromBytes(uuidBytes);
        }
        throw new IllegalArgumentException("Only 2, 4 and 16 bytes are allowed");
    }


    public static UUID from16Bits(int uuid16Bits, String name){
        byte[] uuidBytes = ByteBuffer.allocate(2).putShort((short) uuid16Bits).array();
        return fromBytes(uuidBytes, name);
    }
}
