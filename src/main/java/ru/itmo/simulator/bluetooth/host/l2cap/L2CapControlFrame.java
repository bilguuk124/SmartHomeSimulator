package ru.itmo.simulator.bluetooth.host.l2cap;

public class L2CapControlFrame {

    public byte[] toBytes() {
        return null;
    }

    public static class L2CapControlConnectionRequest extends L2CapControlFrame{

    }

    public static class L2CapControlDisconnectRequest extends L2CapControlFrame {
    }
}
