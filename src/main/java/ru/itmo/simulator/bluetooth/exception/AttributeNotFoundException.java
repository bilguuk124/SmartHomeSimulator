package ru.itmo.simulator.bluetooth.exception;

public class AttributeNotFoundException extends AttError{
    public AttributeNotFoundException(int errorCode, int attHandle) {
        super(errorCode, attHandle, "Attribute with given handle was not found");
    }
}
