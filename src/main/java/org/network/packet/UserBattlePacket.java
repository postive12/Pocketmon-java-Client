package org.network.packet;

import java.util.ArrayList;
import java.util.List;

public class UserBattlePacket extends Packet{
    public String commandType;
    public String target;
    public List<Integer> args = new ArrayList<>();
    public UserBattlePacket(int id, String username, String commandType,String target,List<Integer> args) {
        super(id, PacketType.USER_BATTLE, username);
        this.commandType = commandType;
        this.args = args;
        this.target = target;
    }
    /*
    공격 패킷
    commandType : "ATTACK"
    args : 첫번째 인자가 공격 타입
    기본 공격 : -1
    스킬 번호 : 0-3
    서버에서 처리 후 체력에 반영
     */
}
