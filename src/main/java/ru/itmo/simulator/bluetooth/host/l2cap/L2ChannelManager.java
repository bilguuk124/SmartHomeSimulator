package ru.itmo.simulator.bluetooth.host.l2cap;

import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.host.Host;
import ru.itmo.simulator.bluetooth.host.att.pdu.AttPdu;

import java.util.HashMap;
import java.util.Map;

public class L2ChannelManager {
    private Host host;
    private Map<Integer, Channel> channels;
    private static int currentChannelId = 0x01;
    private Map<Integer, L2CapControlFrame.L2CapControlConnectionRequest> requests;

    public L2ChannelManager(Host host) {
        this.host = host;
        channels = new HashMap<>();
        requests = new HashMap<>();
    }

    public Channel getChannelByConnection(Connection connection) {
        for (Channel channel : channels.values()) {
            if (channel.getConnection().equals(connection)) {
                return channel;
            }
        }
        return null;
    }

    public int nextIdentifier(){
        return currentChannelId++;
    }


    public L2CapControlFrame.L2CapControlConnectionRequest getConnectionRequestsByIdentifier(int id) {
        return requests.get(id);
    }
    public void addConnectionRequest(int identifier, L2CapControlFrame.L2CapControlConnectionRequest request) {
        requests.put(identifier, request);
    }

    public void sendPdu(Connection connection, int destinationCid, byte[] pdu) {
        host.sendL2CapPdu(connection.getHandle(), destinationCid, pdu);
    }

    public void sendControlFrame(Connection connection, int cid, L2CapControlFrame request) {
        host.sendL2CapPdu(connection.getHandle(), cid, request.toBytes());
    }


    public void onPdu(L2CapPdu pdu) {
        Channel channel = channels.get(pdu.getCid());
        channel.onPdu(pdu.getPayload());
    }

    public void onGattRequest(Connection connection, AttPdu pdu) {
        host.gattRequest(connection, pdu);
    }

    public void onGattResponse(AttPdu pdu) {
        host.gattResponse(pdu);
    }
}
