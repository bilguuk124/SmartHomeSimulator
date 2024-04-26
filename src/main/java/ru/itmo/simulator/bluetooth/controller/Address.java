package ru.itmo.simulator.bluetooth.controller;

import java.util.Random;

public class Address {
    private final byte[] address;

    private Address(byte[] address){
        if (address.length != 6) {
            throw new IllegalArgumentException("Address must be 6 bytes");
        }
        this.address = address;
    }

    public String getStringAddress() {
        return new String(address);
    }

    public byte[] getByteAddress() {
        return address;
    }

    public static Address generateAddress() {
        Random random = new Random();
        byte[] addressBytes = new byte[6];
        random.nextBytes(addressBytes);
        return new Address(addressBytes);
    }

    public static Address fromBytes(byte[] addressBytes) {
        return new Address(addressBytes);
    }

    public static Address fromString(String address) {
        byte[] addressBytes = address.getBytes();
        if (addressBytes.length != 6) {
            throw new IllegalArgumentException("Address must be 6 bytes");
        }
        return new Address(addressBytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        for (int i = 0; i < 6; i++) {
            if (address1.address[i] != address[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 31 + address.length + address[0] + address[1] + address[2];
    }
}
