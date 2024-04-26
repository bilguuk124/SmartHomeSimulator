package ru.itmo.simulator.bluetooth.host.gatt.characteristics;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.simulator.bluetooth.host.att.Attribute;
import ru.itmo.simulator.bluetooth.host.att.Permissions;
import ru.itmo.simulator.bluetooth.host.gatt.GattDefaults;

import java.nio.ByteBuffer;

@Getter
@Setter
public class CharacteristicDeclaration extends Attribute {
    private Characteristic characteristic;
    private int valueHandle;

    public CharacteristicDeclaration(Characteristic characteristic, int valueHandle) {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte) characteristic.getProperties().getValue());
        buffer.putShort((short) valueHandle);
        byte[] declarationBytes = ByteBuffer.allocate(buffer.capacity() + characteristic.getType().toString().getBytes().length)
                .put(buffer.array())
                .put(characteristic.getType().toString().getBytes())
                .array();

        super(GattDefaults.GATT_CHARACTERISTIC_ATTRIBUTE_TYPE, Permissions.READABLE, declarationBytes);
        this.characteristic = characteristic;
        this.valueHandle = valueHandle;
    }

    @Override
    public String toString() {
        return String.format("CharacteristicDeclaration(handle=0x%04X, value_handle=0x%04X, uuid=%s, %s)",
                this.getHandle(), this.valueHandle, this.characteristic.getType().toString(), this.characteristic.getProperties().toString());
    }

}
