package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation{
    private final int MEMBERS_QUANTITY = 10000;
    private final int GROUP_A_SIZE = 50, QUANTITY_FRIENDS_GROUP_A = 10;
    private final int GROUP_B_SIZE = 30, QUANTITY_FRIENDS_GROUP_B = 20;
    private final int GROUP_C_SIZE = 15, QUANTITY_FRIENDS_GROUP_C = 40;
    private final int GROUP_D_SIZE = 5, QUANTITY_FRIENDS_GROUP_D = 80;
    private final int FRIEND_QUANTITY_BORDER = 25;

    private ArrayList<SocialNetworkSimulationMember> members = new ArrayList<>();
    public class SocialNetworkSimulationMember extends SocialNetworkMember {
        SimulationGroup group = SimulationGroup.FREE;
        boolean isConnectedDuringSimulation = false;
    }
    ArrayList<SocialNetworkSimulationMember> toAddNewSimulationMembers = new ArrayList<>();
    private final Map<SimulationGroup, GroupCharacteristics> groupCharacteristicsMap =
            new HashMap<SimulationGroup, GroupCharacteristics>() {{
                put(SimulationGroup.A, new GroupCharacteristics(GROUP_A_SIZE, QUANTITY_FRIENDS_GROUP_A));
                put(SimulationGroup.B, new GroupCharacteristics(GROUP_B_SIZE, QUANTITY_FRIENDS_GROUP_B));
                put(SimulationGroup.C, new GroupCharacteristics(GROUP_C_SIZE, QUANTITY_FRIENDS_GROUP_C));
                put(SimulationGroup.D, new GroupCharacteristics(GROUP_D_SIZE, QUANTITY_FRIENDS_GROUP_D));
            }};

    private enum SimulationGroup {
        A, B, C, D, FREE
    }
    private class GroupCharacteristics {
        final int groupSize, quantityFriendsGroupX;
        private GroupCharacteristics(int groupSize, int quantityFriendsGroupX) {
            this.groupSize = groupSize;
            this.quantityFriendsGroupX = quantityFriendsGroupX;
        }
    }
    public void simulationProcess(int numberOfDays, int memberId) throws IOException {
        createUsers();
        timeSimulation(numberOfDays, memberId);
    }

    private void createUsers() {
        fillSocialNetwork();
        fillGroupWithFriends(SimulationGroup.A);
        fillGroupWithFriends(SimulationGroup.B);
        fillGroupWithFriends(SimulationGroup.C);
        fillGroupWithFriends(SimulationGroup.D);
    }

    private void fillSocialNetwork() {
        for(int i = 0; i<MEMBERS_QUANTITY; i++)
            members.add(new SocialNetworkSimulationMember());
        assignGroupToMembers();
    }

    private void assignGroupToMembers() {
        AtomicInteger counter = new AtomicInteger(0);
        assignMembersToCertainGroup(counter, GROUP_A_SIZE, SimulationGroup.A);
        assignMembersToCertainGroup(counter, GROUP_B_SIZE, SimulationGroup.B);
        assignMembersToCertainGroup(counter, GROUP_C_SIZE, SimulationGroup.C);
        assignMembersToCertainGroup(counter, GROUP_D_SIZE, SimulationGroup.D);
    }

    private void assignMembersToCertainGroup(AtomicInteger counter, int GROUP_X_SIZE, SimulationGroup group) {
        for(int i=0; i<GROUP_X_SIZE; i++, counter.incrementAndGet())
            members.get(counter.get()).group = group;
    }

    private void fillGroupWithFriends(SimulationGroup group) {
        int indexMemberOfGroupX = 0;
        while (members.get(indexMemberOfGroupX).group != group)
            indexMemberOfGroupX++;

        for (int i = indexMemberOfGroupX; i < indexMemberOfGroupX+groupCharacteristicsMap.get(group).groupSize; i++) {
            getFriendsForCertainMember(members.get(i));
        }
    }

    private void getFriendsForCertainMember(SocialNetworkSimulationMember groupMember) {
        for (int j = groupMember.getQuantityFriends(); j<groupCharacteristicsMap.get(groupMember.group).quantityFriendsGroupX; j++) {
            SocialNetworkSimulationMember randomMember = findRandomMember();
            while(!isValidFriendForGroup(groupMember, randomMember)) {
                randomMember = findRandomMember();
            }

            makeMembersFriends(randomMember, groupMember);
        }
    }
    private SocialNetworkSimulationMember findRandomMember() {
        int randomMemberId = ThreadLocalRandom.current().nextInt(0, members.size());
        return members.get(randomMemberId);
    }
    private boolean isValidFriendForGroup(SocialNetworkSimulationMember groupMember, SocialNetworkSimulationMember randomMember) {
        if (groupMember.friends.contains(randomMember.id))
            return false;

        if (randomMember.group == SimulationGroup.FREE)
            return true;

        if (randomMember.getQuantityFriends() < groupCharacteristicsMap.get(randomMember.group).quantityFriendsGroupX)
            return true;

        return false;
    }

    private void makeMembersFriends (SocialNetworkSimulationMember member1, SocialNetworkSimulationMember member2) {
        member1.addFriend(member2);
        member2.addFriend(member1);
    }

    private void timeSimulation(int numberOfDays, int senderId) throws IOException {
        FileWriter fileWriter = new FileWriter("simulation.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        outputInTxt(0, senderId, printWriter);
        for (int dayNumber = 1; dayNumber < numberOfDays; dayNumber++) {
            oneDaySimulation();
            outputInTxt(dayNumber, senderId, printWriter);
        }
        printWriter.close();
    }

    private void outputInTxt(int dayNumber, int senderId, PrintWriter printWriter) throws IOException {

        String outputString = generateOutputMessage(dayNumber, senderId);
        printWriter.print(outputString);

    }

    private String generateOutputMessage(int dayNumber, int senderId) {
        String messageBody = "Tag " + dayNumber + " – Anzahl Personen " + members.size() + "\n";
        messageBody += "Nachricht Variante 1 hat " + getReceiversQuantityVar1(senderId) + " Personen erreicht";
        messageBody += "und Nachricht Variante 2 hat " + getReceiversQuantityVar2(senderId) + " Personen erreicht\n";

        return messageBody;
    }

    private int getReceiversQuantityVar1(int senderId) {
        Set<Integer> receiversId = new HashSet<>();

        for (int friendId: getMemberById(senderId).getFriends()) {
            receiversId.add(friendId);
            SocialNetworkSimulationMember friend = getMemberById(friendId);
            for (int friendOfFriend : friend.getFriends())
                receiversId.add(friendOfFriend);
        }
        return receiversId.size() - 1;
    }

    private SocialNetworkSimulationMember getMemberById(int memberId) {
        for (SocialNetworkSimulationMember member: members) {
            if (member.getId() == memberId) {
                return member;
            }
        }
        return null;
    }

    public int getReceiversQuantityVar2(int senderId) {
        UnionFind uf = unionMembers();

        return uf.findComponentSize(senderId) - 1;
    }
    private UnionFind unionMembers() {
        UnionFind uf = new UnionFind(members.size());
        for (SocialNetworkSimulationMember member: members) {
            for (int friendId: member.getFriends()) {
                uf.union(member.getId(), friendId);
            }
        }
        return uf;
    }

    private void oneDaySimulation() {
        for (SocialNetworkSimulationMember member : members) {
            if (!member.isConnectedDuringSimulation)
                certainMemberDaySimulation(member);
        }
        addNewMembers();

    }

    private void certainMemberDaySimulation(SocialNetworkSimulationMember member) {
        SocialNetworkSimulationMember newExistingFriend = findValidFriend(member);
        makeSimulationDayMembersFriends(member, newExistingFriend);

        if (member.getQuantityFriends() - 1 <= FRIEND_QUANTITY_BORDER)
            creatingFriendSimulation(member);
        else
            for (int i = 0; i<3; i++) {
                creatingFriendSimulation(member);
            }
    }

    private SocialNetworkSimulationMember findValidFriend (SocialNetworkSimulationMember member) {
        SocialNetworkSimulationMember newExistingFriend = findRandomMember();
        while (!isValidFriend(member, newExistingFriend)) {
            newExistingFriend = findRandomMember();
        }
        return newExistingFriend;
    }

    private boolean isValidFriend(SocialNetworkSimulationMember member, SocialNetworkSimulationMember newExistingFriend) {
        if(newExistingFriend.isConnectedDuringSimulation)
            return false;

        for (int friendId: member.friends) {
            if (newExistingFriend.id == friendId)
                return false;
        }
        return true;
    }

    private void makeSimulationDayMembersFriends(SocialNetworkSimulationMember member1, SocialNetworkSimulationMember member2) {
        member1.addFriend(member2);
        member1.isConnectedDuringSimulation = true;

        member2.addFriend(member1);
        member2.isConnectedDuringSimulation = true;
    }

    private void creatingFriendSimulation(SocialNetworkSimulationMember member) {
        SocialNetworkSimulationMember newFriend = new SocialNetworkSimulationMember();
        makeMembersFriends(member, newFriend);
        toAddNewSimulationMembers.add(newFriend);
    }

    private void addNewMembers() {
        for (SocialNetworkSimulationMember member: members)
            member.isConnectedDuringSimulation = false;
        members.addAll(toAddNewSimulationMembers);
        toAddNewSimulationMembers.clear();
    }
}

