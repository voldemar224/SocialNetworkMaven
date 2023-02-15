package org.example;
import java.util.*;

public class SocialNetwork {
    Set<SocialNetworkMember> members = new HashSet<>();

    public SocialNetwork(Set<SocialNetworkMember> members) {
        this.members.addAll(members);
        for (SocialNetworkMember member: this.members) {
            for (int friendsId: member.getFriends()) {
                member.addFriend(getMemberById(friendsId));
            }
        }
    }

    public void addMember(SocialNetworkMember member) {
        members.add(member);
        ArrayList<Integer> toDeleteNonExistentFriends = new ArrayList<>();

        for (int friendId: member.getFriends()) {
            if (getMemberById(friendId) == null)
                toDeleteNonExistentFriends.add(friendId);
            else
                member.addFriend(getMemberById(friendId));
        }
        for (int nonExistentFriend: toDeleteNonExistentFriends) {
            member.removeFriend(nonExistentFriend);
        }
    }

    public int getReceiversQuantityVar1(int senderId) {
        Set<Integer> receiversId = new HashSet<>();
        try {
            for (int friendId: getMemberById(senderId).getFriends()) {
                receiversId.add(friendId);
                SocialNetworkMember friend = getMemberById(friendId);
                for (int friendOfFriend : friend.getFriends())
                    if (friendOfFriend != senderId)
                        receiversId.add(friendOfFriend);
            }
            return receiversId.size();
        } catch (NullPointerException e) {
            throw (new IllegalArgumentException("There is no user with such id"));
        }
    }

    public SocialNetworkMember getMemberById(int memberId) {
        for (SocialNetworkMember member: members) {
            if (member.getId() == memberId)
                return member;
        }
        return null;
    }

    public boolean isAbleSendMessageVar1(int senderId, int receiverId) {
        SocialNetworkMember sender = getMemberById(receiverId);
        if (sender == null)
            throw (new IllegalArgumentException("Sender can't be non-existent user"));

        SocialNetworkMember receiver = getMemberById(receiverId);
        if (receiver == null)
            throw (new IllegalArgumentException("Receiver can't be non-existent user"));


        for (int receiversFriend: receiver.getFriends()) {
            if (getMemberById(receiversFriend).isFriendOfMember(sender))
                return true;
        }
        return false;
    }

    public int getReceiversQuantityVar2(int senderId) {
        UnionFind uf = unionMembers();

        int receiversQuantity = uf.findComponentSize(senderId);

        if (receiversQuantity == 0)
            return 0;
        else
            return receiversQuantity - 1;
    }

    private UnionFind unionMembers() {
        int membersQuantity = members.size();

        UnionFind uf = new UnionFind(membersQuantity);
        for (SocialNetworkMember member: members) {
            for (int friendId: member.getFriends()) {
                uf.union(member.getId(), friendId);
            }
        }
        return uf;
    }
    public boolean isAbleSendMessageVar2(int senderId, int receiverId) {
        UnionFind uf = unionMembers();

        return uf.find(senderId) == uf.find(receiverId);
    }
}



