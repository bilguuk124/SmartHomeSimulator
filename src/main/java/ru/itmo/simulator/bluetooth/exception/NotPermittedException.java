package ru.itmo.simulator.bluetooth.exception;

import ru.itmo.simulator.bluetooth.host.att.ATT;

public class NotPermittedException extends AttError{
    public NotPermittedException(int errorCode, int attHandle) {
        String message;
        if (errorCode == ATT.ATT_WRITE_NOT_PERMITTED_ERROR){
            message = "Write not permitted";
        }
        else if (errorCode == ATT.ATT_READ_NOT_PERMITTED_ERROR){
            message = "Read not permitted";
        }
        else {
            message = "Not permitted";
        }
        super(errorCode, attHandle, message);
    }
}
