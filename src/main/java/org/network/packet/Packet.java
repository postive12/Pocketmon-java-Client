package org.network.packet;

public abstract class Packet {
    protected int id;
    protected PacketType packetType;
    protected String username;
    public Packet(int id,PacketType packetType, String username){
        this.id = id;
        this.packetType = packetType;
        this.username = username;
    }
}