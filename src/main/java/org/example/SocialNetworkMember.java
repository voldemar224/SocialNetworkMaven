package org.example;
import java.util.HashSet;
import java.util.Set;

public class SocialNetworkMember {
    static int newMemberId = 0;

    protected int id;
    public int getId() {
        return id;
    }

    private Set<Integer> friends = new HashSet<>();
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

        if (existingFriends != null) {
            for (int friendId : existingFriends)
                friends.add(friendId);
        }
        name = newName;
    }
    public SocialNetworkMember() {
        this(null, "user_" + newMemberId);
    }

    public SocialNetworkMember(int[] existingFriends) {
        this(existingFriends, "user_" + newMemberId);
    }
    public SocialNetworkMember(String newName) {
        this(null, newName);
    }
    public void addFriend(SocialNetworkMember newFriend) {
        friends.add(newFriend.getId());
    }

    public int getQuantityFriends() {
        return friends.size();
    }
}

