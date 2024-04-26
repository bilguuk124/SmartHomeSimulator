package ru.itmo.simulator.bluetooth.exception;

import java.util.UUID;

public class DescriptorNotFoundException extends GattError {
    public DescriptorNotFoundException(UUID descriptorType) {
        super("GATT_DESCRIPTOR_NOT_FOUND","Descriptor with type not found =" + descriptorType);
    }
}
