package org.network.packet;

public enum PacketType {
    USER_DEFAULT(0),
    USER_COMMAND(1),
    USER_INPUT(2),
    USER_CHAT(3),
    SERVER_DEFAULT(100);
    private final int environments;

    PacketType(int environments){
        this.environments = environments;
    }
    public int toInt(){
        return environments;
    }
}