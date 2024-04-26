package ru.itmo.simulator.bluetooth.host;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.Device;
import ru.itmo.simulator.bluetooth.host.att.pdu.AttPdu;
import ru.itmo.simulator.bluetooth.host.gatt.GattClient;
import ru.itmo.simulator.bluetooth.host.gatt.GattServer;
import ru.itmo.simulator.bluetooth.host.hci.HciPacket;
import ru.itmo.simulator.bluetooth.host.hci.HostControllerInterface;
import ru.itmo.simulator.bluetooth.host.l2cap.L2ChannelManager;
import ru.itmo.simulator.bluetooth.host.l2cap.L2CapPdu;


@Component
@Scope("prototype")
@Getter
@Setter
public class Host {
    private Device device;
    private HostControllerInterface hci;
    private GattClient gattClient;
    private GattServer gattServer;
    private L2ChannelManager channelManager;

    private Host(Device device, HostControllerInterface hci) {
        this.device = device;
        this.hci = hci;
        this.gattServer = new GattServer(device);
        this.gattClient = new GattClient(device);
        this.channelManager = new L2ChannelManager(this);
    }


    public static Host initiateWithoutHci(Device device) {
        return new Host(device, null);
    }

    public void onPdu(int cid, byte[] data) {
        channelManager.onPdu(new L2CapPdu(cid, data));
    }

    public void sendL2CapPdu(int handle, int cid, byte[] data) {
        L2CapPdu pdu = new L2CapPdu(cid, data);
        int fragmentationFlag = 0;
        byte[] message = pdu.toBytes();
        hci.sendPdu(new HciPacket.HciAclDataPacket(handle, fragmentationFlag, message.length, message));
    }

    public void gattRequest(Connection connection, AttPdu attPdu) {
        gattServer.onGattPdu(connection, attPdu);
    }

    public void gattResponse(AttPdu pdu) {
        gattClient.onGattPdu(pdu);
    }
}
