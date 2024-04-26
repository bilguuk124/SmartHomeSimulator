package ru.itmo.simulator.bluetooth.controller;

import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itmo.simulator.bluetooth.Device;
import ru.itmo.simulator.bluetooth.host.hci.HciPacket;
import ru.itmo.simulator.bluetooth.host.hci.HostControllerInterface;

@Component
@Scope("prototype")
@Getter
@Setter
public class Controller {
    private HostControllerInterface hci;
    private LinkLayer linkLayer;
    private PhysicalLayer physicalLayer;

    private Controller(HostControllerInterface hci, LinkLayer linkLayer, PhysicalLayer physicalLayer) {
        this.hci = hci;
        this.linkLayer = linkLayer;
        this.physicalLayer = physicalLayer;
    }


    public static Controller initiateWithoutHci(Device device) {
        Controller controller = new Controller(null,null, null);
        Pair<LinkLayer, PhysicalLayer> pair = initiateLowerLayers(controller);
        controller.setLinkLayer(pair.getValue0());
        controller.setPhysicalLayer(pair.getValue1());
        return controller;
    }

    private static Pair<LinkLayer, PhysicalLayer> initiateLowerLayers(Controller controller) {
        LinkLayer linkLayer = new LinkLayer(controller, null);
        PhysicalLayer physicalLayer = PhysicalLayer.create(linkLayer);
        linkLayer.setPhysicalLayer(physicalLayer);
        return new Pair<>(linkLayer, physicalLayer);
    }

    public void onPduReceived(int cid,  byte[] data) {
        hci.pduReceived(cid, data);
    }

    public void sendPdu(HciPacket packet) {
        linkLayer.sendFrame(packet);
    }
}
