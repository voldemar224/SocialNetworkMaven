package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SocialNetworkMemberTest {
    private static SocialNetworkMember friend_x, friend_y, friend_z, friend_0, friend_1, friend_2, friend_3, friend_4;
    @BeforeAll
    static void init() {
        friend_0 = new SocialNetworkMember();
        friend_1 = new SocialNetworkMember();
        friend_2 = new SocialNetworkMember();
        friend_3 = new SocialNetworkMember();
        friend_4 = new SocialNetworkMember();
        friend_x = new SocialNetworkMember(new int[]{1,3,6});
        friend_y = new SocialNetworkMember(new int[]{0,5,3,4});
        friend_z = new SocialNetworkMember();
    }

    @Test
    @Order(1)
    void areTwoMembersFriends() {
        assertTrue(friend_x.isFriendOfMember(friend_y));
    }
    @Test
    @Order(2)
    void areTwoMembersNotFriends() {
        assertFalse(friend_0.isFriendOfMember(friend_1));
    }
    @Test
    @Order(3)
    void addFriendWithZeroFriendsToMemberWithZeroFriends() {
        friend_0.addFriend(friend_1);
        assertTrue(friend_0.isFriendOfMember(friend_1));
    }

    @Test
    @Order(3)
    void addFriendWithZeroFriendsToMemberWithOneFriend() {
        friend_0.addFriend(friend_1);
        friend_2.addFriend(friend_0);

        assertTrue(friend_0.isFriendOfMember(friend_2));
    }

    @Test
    @Order(4)
    void addFriendWithZeroFriendsToMemberWithFewFriends() {
        friend_0.addFriend(friend_1);
        friend_0.addFriend(friend_2);
        friend_0.addFriend(friend_3);

        friend_4.addFriend(friend_0);

        assertTrue(friend_0.isFriendOfMember(friend_4));
    }

    @Test
    @Order(5)
    void addFriendWithOneFriendsToMemberWithFewFriends() {
        friend_0.addFriend(friend_1);
        friend_0.addFriend(friend_2);

        friend_1.addFriend(friend_3);

        friend_0.addFriend(friend_3);

        assertTrue(friend_0.isFriendOfMember(friend_3));
    }

    @Test
    @Order(6)
    void addFriendWithFewFriendsToMemberWithFewFriends() {
        friend_0.addFriend(friend_1);
        friend_0.addFriend(friend_2);
        friend_0.addFriend(friend_3);

        friend_1.addFriend(friend_2);
        friend_1.addFriend(friend_4);

        friend_2.addFriend(friend_3);
        friend_2.addFriend(friend_4);

        friend_3.addFriend(friend_4);

        friend_0.addFriend(friend_4);

        assertTrue(friend_0.isFriendOfMember(friend_4));
    }

    @Test
    @Order(7)
    void getQuantityFriendsOfMemberWithZeroFriends() {
        assertEquals(0, friend_z.getQuantityFriends());
    }

    @Test
    @Order(8)
    void getQuantityFriendsOfMemberWithFewFriends() {
        assertEquals(4, friend_0.getQuantityFriends());
    }
}