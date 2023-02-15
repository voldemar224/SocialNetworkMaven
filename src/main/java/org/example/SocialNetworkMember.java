package org.example;
import java.util.HashSet;
import java.util.Set;

public class SocialNetworkMember {
    static int newMemberId = 0;
    protected int id;
    public int getId() {
        return id;
    }
    protected Set<Integer> friends = new HashSet<>();
    public Iterable<Integer> getFriends() {
        return friends;
    }
    private String name;
    public String getName() {
        return name;
    }

    public SocialNetworkMember(int[] existingFriends, String newName){
        id = newMemberId;
        newMemberId++;

        for (int friendId : existingFriends)
            friends.add(friendId);

        name = newName;
    }
    public SocialNetworkMember() {
        this(new int[0], "user_" + newMemberId);
    }
    public SocialNetworkMember(int[] existingFriends) {
        this(existingFriends, "user_" + newMemberId);
    }

    public SocialNetworkMember(String newName) {
        this(new int[0], newName);
    }

    public void addFriend(SocialNetworkMember newFriend) {
        writeNewFriendToFriends(newFriend.getId());
        newFriend.writeNewFriendToFriends(this.id);
    }

    private void writeNewFriendToFriends(int newFriendId) {
        friends.add(newFriendId);
    }

    public int getQuantityFriends() {
        return friends.size();
    }

    public boolean isFriendOfMember(SocialNetworkMember member) {
        boolean thisIsFriendOfMember = false, memberIsFriendOfThis = false;
        for (int friend: this.getFriends()) {
            if (friend == member.getId()) {
                thisIsFriendOfMember = true;
                break;
            }
        }
        for (int friend: member.getFriends()) {
            if (friend == this.getId()) {
                memberIsFriendOfThis = true;
                break;
            }
        }
        return thisIsFriendOfMember && memberIsFriendOfThis;
    }
}

