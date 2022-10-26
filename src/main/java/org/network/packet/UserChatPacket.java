package org.network.packet;

public class UserChatPacket extends Packet{
    protected String chat;
    protected String target;
    public UserChatPacket(int id, String username,String chat, String target) {
        super(id, PacketType.USER_CHAT, username);
        this.target = target;
        this.chat = chat;
    }
}
