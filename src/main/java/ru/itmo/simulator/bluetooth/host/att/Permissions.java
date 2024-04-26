package ru.itmo.simulator.bluetooth.host.att;

import lombok.Getter;
import ru.itmo.simulator.bluetooth.exception.PermissionNotFoundException;

import java.util.Arrays;

@Getter
public enum Permissions {
    READABLE(0x01),
    WRITABLE(0x02),
    READABLE_WRITABLE(0x04);

    private final int value;

    Permissions(int value) {
        this.value = value;
    }

    public static Permissions fromString(String permission) {
        return Arrays.stream(values()).filter(p -> p.name().equalsIgnoreCase(permission))
                .findFirst()
                .orElseThrow(() -> new PermissionNotFoundException(permission));
    }

}
