package ru.itmo.simulator.bluetooth.host.gap;

import ru.itmo.simulator.bluetooth.host.att.Permissions;
import ru.itmo.simulator.bluetooth.host.gatt.GattDefaults;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.Characteristic;
import ru.itmo.simulator.bluetooth.host.gatt.characteristics.CharacteristicsProperties;
import ru.itmo.simulator.bluetooth.host.gatt.services.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class GenericAccessService extends Service {
    public GenericAccessService(String deviceName) {
        Characteristic deviceNameCharacteristic = new Characteristic(
                GattDefaults.GATT_DEVICE_NAME_CHARACTERISTICS,
                Permissions.READABLE,
                deviceName.getBytes(StandardCharsets.UTF_8),
                CharacteristicsProperties.READ
                );

        super(GattDefaults.GATT_GENERIC_ACCESS_SERVICE,
                List.of(deviceNameCharacteristic));
    }
}
