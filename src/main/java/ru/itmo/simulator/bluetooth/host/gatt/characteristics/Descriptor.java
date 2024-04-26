package ru.itmo.simulator.bluetooth.host.gatt.characteristics;

import ru.itmo.simulator.bluetooth.host.att.AttUtils;
import ru.itmo.simulator.bluetooth.host.att.Attribute;

public class Descriptor extends Attribute {

    private Descriptor(){
        super();
    }

    @Override
    public String toString(){
        String valueStr = AttUtils.bytesToHex(this.getValue());
        return String.format("Descriptor(handle=0x%04X, type=%s, value=%s)",
                getHandle(),
                getType(),
                valueStr);
    }
}
