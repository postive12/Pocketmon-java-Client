package org.network.packet;

public class UserInputPacket extends Packet{
    protected String userInput;
    public UserInputPacket(int id, String username,String userInput) {
        super(id, PacketType.USER_INPUT, username);
        this.userInput = userInput;
    }
}
