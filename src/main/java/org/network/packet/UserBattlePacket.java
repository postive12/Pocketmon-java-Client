package org.network.packet;

import java.util.ArrayList;
import java.util.List;

public class UserBattlePacket extends Packet{
    public String userInput;
    public String target;
    public BattlePacketType battlePacketType;
    public List<Integer> args = new ArrayList<>();
    public UserBattlePacket(int id, String username, String userInput,String target,List<Integer> args) {
        super(id, PacketType.USER_BATTLE, username);
        this.userInput = userInput;
        this.args = args;
        this.target = target;
    }
}
