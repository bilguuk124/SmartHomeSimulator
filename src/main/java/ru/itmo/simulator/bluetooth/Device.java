package ru.itmo.simulator.bluetooth;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itmo.simulator.bluetooth.controller.Address;
import ru.itmo.simulator.bluetooth.controller.Controller;
import ru.itmo.simulator.bluetooth.host.Host;
import ru.itmo.simulator.bluetooth.host.hci.HostControllerInterface;

@Component
@Scope("prototype")
public class Device {
    private Address address;
    private final Host host;
    private final Controller controller;
    private final HostControllerInterface hci;

    private Device(){
        this.address = Address.generateAddress();
        this.host = Host.initiateWithoutHci(this);
        this.controller = Controller.initiateWithoutHci(this);
        this.hci = HostControllerInterface.of(host, controller);
        this.host.setHci(hci);
        this.controller.setHci(hci);
    }

    public void sendL2CapPdu(int connectionHandle, int attCid, byte[] pdu) {

    }
}
