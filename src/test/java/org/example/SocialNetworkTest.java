package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SocialNetworkTest {

    static SocialNetwork testSocNet;
    @BeforeAll
    public static void createSocialNetwork() {
        Set<SocialNetworkMember> members = new HashSet<>();
        members.add(new SocialNetworkMember(new int[]{1,2})); // 0
        members.add(new SocialNetworkMember(new int[]{0,2,3})); // 1
        members.add(new SocialNetworkMember(new int[]{0,1,3})); // 2
        members.add(new SocialNetworkMember(new int[]{1,2,4})); // 3
        members.add(new SocialNetworkMember(new int[]{3})); // 4
        members.add(new SocialNetworkMember(new int[]{8})); // 5
        members.add(new SocialNetworkMember(new int[]{7,8})); // 6
        members.add(new SocialNetworkMember(new int[]{5,6})); // 7
        members.add(new SocialNetworkMember(new int[]{6})); // 8
        members.add(new SocialNetworkMember(new int[]{})); // 9
        members.add(new SocialNetworkMember(new int[]{11})); // 10
        members.add(new SocialNetworkMember(new int[]{10})); // 11

        testSocNet = new SocialNetwork(members);
    }

    @Test
    void getReceiversQuantityFromMemberWithZeroFriendsVar1() {
        assertEquals(testSocNet.getReceiversQuantityVar1(9), 0);
    }

    @Test
    void getReceiversQuantityFromMemberWithOneFriendWhoHas1FriendVar1() {
        assertEquals(testSocNet.getReceiversQuantityVar1(10), 1);

    }

    @Test
    void getReceiversQuantityFriendGroupWithoutCyclesReachingEveryoneVar1() {
        assertEquals(testSocNet.getReceiversQuantityVar1(8), 3);
    }
    @Test
    void getReceiversQuantityFriendGroupWithoutCyclesVar1() {
        assertEquals(testSocNet.getReceiversQuantityVar1(5), 2);
    }
    @Test
    void getReceiversQuantityFriendGroupWithCyclesVar1() {
        assertEquals(testSocNet.getReceiversQuantityVar1(1), 4);
    }

    @Test
    void getReceiversQuantityFromMemberWithZeroFriendsVar2() {
        assertEquals(testSocNet.getReceiversQuantityVar2(9), 0);
    }

    @Test
    void getReceiversQuantityFromMemberWithOneFriendWhoHas1FriendVar2() {
        assertEquals(testSocNet.getReceiversQuantityVar2(10), 1);
    }

    @Test
    void getReceiversQuantityFriendGroupWithoutCyclesVar2() {
        assertEquals(testSocNet.getReceiversQuantityVar2(5), 3);
    }
    @Test
    void getReceiversQuantityFriendGroupWithCyclesVar2() {
        assertEquals(testSocNet.getReceiversQuantityVar2(1), 4);
    }
}