package ru.itmo.simulator.bluetooth.host.hci;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.controller.Controller;
import ru.itmo.simulator.bluetooth.host.Host;

@Component
@Scope("prototype")
public class HostControllerInterface {
    private final Host host;
    private final Controller controller;

    private HostControllerInterface(Host host, Controller controller) {
        this.host = host;
        this.controller = controller;
    }

    public static HostControllerInterface of(Host host, Controller controller) {
        return new HostControllerInterface(host, controller);
    }

    public void pduReceived(int cid, byte[] data) {
        host.onPdu(cid, data);
    }

    public void sendPdu(HciPacket packet) {
        if (packet.getPacketType() == HciPacket.HCI_ACL_DATA_PACKET){
            controller.sendPdu(packet);
        }
    }
}
