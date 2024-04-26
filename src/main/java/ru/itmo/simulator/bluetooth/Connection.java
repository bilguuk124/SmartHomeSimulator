package ru.itmo.simulator.bluetooth;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.simulator.bluetooth.controller.Address;

@Getter
@Setter
public class Connection {

    private int handle;
    private final Address selfAddress;
    private Address peerAddress;

    public Connection(int handle, Address selfAddress, Address peerAddress) {
        this.selfAddress = selfAddress;
        this.handle = handle;
        this.peerAddress = peerAddress;
    }
}
