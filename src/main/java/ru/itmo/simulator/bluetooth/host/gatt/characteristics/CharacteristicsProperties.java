package ru.itmo.simulator.bluetooth.host.gatt.characteristics;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CharacteristicsProperties {
    BROADCAST(0x01),
    READ(0x02),
    WRITE_WITHOUT_RESPONSE(0x04),
    WRITE(0x08),
    NOTIFY(0x10),
    INDICATE(0x20);

    private final int value;

    CharacteristicsProperties(int value) {
        this.value = value;
    }

    public static CharacteristicsProperties fromString(String string) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(string))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Unknown characteristics property: " + string)
                );
    }
}
