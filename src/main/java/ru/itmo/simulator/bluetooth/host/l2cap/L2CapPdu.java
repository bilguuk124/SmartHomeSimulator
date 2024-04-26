package ru.itmo.simulator.bluetooth.host.l2cap;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.simulator.bluetooth.host.att.AttUtils;

import java.nio.ByteBuffer;

@Getter
@Setter
public class L2CapPdu {
    private int cid;
    private byte[] payload;

    public L2CapPdu(int cid, byte[] payload) {
        this.cid = cid;
        this.payload = payload;
    }

    public static L2CapPdu fromBytes(byte[] data) {
        if (data == null || data.length < 4) {
            throw new IllegalArgumentException("Not enough data");
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);

        int l2capPduCID = buffer.getShort() & 0xFFFF;
        byte[] l2capPduPayload = new byte[data.length - 4];
        buffer.get(l2capPduPayload);
        return new L2CapPdu(l2capPduCID, l2capPduPayload);
    }

    public byte[] toBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(4 + payload.length);
        buffer.putShort((short) payload.length);
        buffer.putShort((short) cid);
        buffer.put(payload);
        return buffer.array();
    }

    public String toString(){
        return "L2CAP [CID=" + cid + "]: " + AttUtils.bytesToHex(payload);
    }
}
