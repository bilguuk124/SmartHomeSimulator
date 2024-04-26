package ru.itmo.simulator.bluetooth.host.gatt.services;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.simulator.bluetooth.host.att.Attribute;
import ru.itmo.simulator.bluetooth.host.att.Permissions;
import ru.itmo.simulator.bluetooth.host.gatt.GattDefaults;

import java.nio.ByteBuffer;

@Getter
@Setter
public class IncludedServiceDeclaration extends Attribute {
    private Service service;

    public IncludedServiceDeclaration(Service service) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putShort(service.getHandle());
        buffer.putShort(service.getEndGroupHandle());
        buffer.put(service.getUuid().toString().getBytes());

        super(GattDefaults.GATT_INCLUDE_ATTRIBUTE_TYPE, Permissions.READABLE, buffer.array());
        this.service = service;
    }

    @Override
    public String toString() {
        return String.format(
                "IncludedServiceDefinition(handle=0x%04X, group_ending_handle=0x%04X, uuid=%s)",
                service.getHandle(),
                service.getEndGroupHandle(),
                service.getUuid().toString()
        );
    }
}
