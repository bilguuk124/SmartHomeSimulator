package ru.itmo.simulator.bluetooth.host.gatt.characteristics;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.simulator.bluetooth.exception.DescriptorNotFoundException;
import ru.itmo.simulator.bluetooth.host.att.Attribute;
import ru.itmo.simulator.bluetooth.host.att.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Characteristic extends Attribute {
    private CharacteristicsProperties properties;
    private List<Descriptor> descriptors;


    public Characteristic(UUID type,
                          Permissions permissions,
                          byte[] value,
                          CharacteristicsProperties properties,
                          List<Descriptor> descriptors) {
        super(type, permissions, value);
        this.properties = properties;
        this.descriptors = descriptors;
    }

    public Characteristic(UUID type,
                          Permissions permissions,
                          byte[] value,
                          CharacteristicsProperties properties,
                          Descriptor[] descriptor) {
        this(type, permissions, value, properties, Arrays.stream(descriptor).toList());
    }

    public Characteristic(UUID type,
                          Permissions permissions,
                          byte[] value,
                          CharacteristicsProperties properties) {
        this(type, permissions, value, properties, new ArrayList<>());
    }

    public Descriptor getDescriptors(UUID descriptorType) {
        return descriptors.stream().filter(e -> e.getType().equals(descriptorType)).findFirst().orElseThrow(() -> new DescriptorNotFoundException(descriptorType));
    }

    public boolean hasProperties() {
        return properties != null;
    }

    @Override
    public String toString() {
        return String.format("Characteristic(handle=0x%04X, uuid=%s, %s)",
                this.getHandle(), this.getType().toString(), this.properties.toString());
    }


}
