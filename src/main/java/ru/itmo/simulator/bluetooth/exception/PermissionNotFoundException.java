package ru.itmo.simulator.bluetooth.exception;

public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(String permission) {
        super("Permission not found: " + permission);
    }
}
