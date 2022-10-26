package org.network.packet;

public class CommandPacket extends Packet{
    protected String command;
    protected String[] targets;
    public CommandPacket(int id,String username,String command,String[] targets) {
        super(id,PacketType.USER_COMMAND ,username);
        this.command = command;
        this.targets = targets;
    }

}
