//package ru.itmo.simulator.bluetooth.host.att;
//
//import ru.itmo.simulator.bluetooth.exception.AttributeNotFoundException;
//import ru.itmo.simulator.bluetooth.exception.NotPermittedException;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//public class AttributeProtocol {
//    private short handle;
//    private final Map<Short, Attribute> attributes;
//
//    public AttributeProtocol() {
//        handle = 0;
//        this.attributes = new HashMap<>();
//    }
//
//    public List<Attribute> getAttributes() {
//        return new ArrayList<>(attributes.values());
//    }
//
//    public Attribute getAttribute(short handle) {
//        if (attributes.containsKey(handle)) {
//            return attributes.get(handle);
//        }
//        throw new AttributeNotFoundException(AttDefaults.ATT_ATTRIBUTE_NOT_FOUND_ERROR, handle);
//    }
//
//    public void createAttribute(UUID type, AttributePermissions permissions, byte[] value){
//        if(value == null || value.length == 0) throw new IllegalArgumentException("Value cannot be null!");
//        Attribute attribute = new Attribute(type, permissions, value);
//        attributes.put(++handle, attribute);
//    }
//
//    public void addAttribute(Attribute attribute) {
//        if (attribute == null) {
//            throw new IllegalArgumentException("Attribute cannot be null");
//        }
//        attributes.put(++handle, attribute);
//    }
//
//    public void addAttribute(short handle, Attribute attribute) {
//        if (attribute == null) {
//            throw new IllegalArgumentException("Attribute cannot be null");
//        }
//        if (handle < 0 || handle >= attributes.size()) {
//            throw new IllegalArgumentException("Handle out of range");
//        }
//        attributes.put(handle, attribute);
//    }
//
//    public void updateAttributeValue(short handle, byte[] value){
//        if (value == null || value.length == 0) {
//            throw new IllegalArgumentException("Value cannot be null");
//        }
//        if (handle < 0 || handle >= attributes.size()) {
//            throw new IllegalArgumentException("Handle out of range");
//        }
//        Attribute attribute = attributes.get(handle);
//        if (attribute.getAttributePermissions().getValue() < 0x02){
//            throw new NotPermittedException(AttDefaults.ATT_WRITE_NOT_PERMITTED_ERROR, handle);
//        }
//        attribute.setValue(value);
//    }
//
//    public void removeAttribute(short handle) {
//        attributes.remove(handle);
//    }
//
//}
