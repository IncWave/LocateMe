package com.mikhailzaitsevfls.locateme.model;

import java.util.ArrayList;


public class Group{
    private int radius;
    private long groupId;
    private String groupName;
    private double latitude;
    private double longitude;
    private long ownerId;
    private ArrayList<Member> members;

    Group(long groupId, String groupName, ArrayList<Member> members,double latitude, double longitude,int radius,long ownerId){
        setGroupId(groupId);
        setGroupName(groupName);
        setMembers(members);
        setLatitude(latitude);
        setLongitude(longitude);
        setRadius(radius);
        setOwnerId(ownerId);
    }

    public int getRadius() {
        return radius;
    }
    public long getGroupId() {
        return groupId;
    }
    public double getLatitude() {
        return latitude;
    }
    public String getGroupName() {
        return groupName;
    }
    public double getLongitude() {
        return longitude;
    }
    public ArrayList<Member> getMembers() {
        return members;
    }
    public int getMemberSize(){
        return this.members.size();
    }
    public long getOwnerId() {
        return ownerId;
    }

    void setRadius(int radius) {
        this.radius = radius;
    }
    private void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    private void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
    private void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }


    public static class Member {
        private boolean memberIsOnline;
        private String memberName;
        private long memberId;
        private String memberUri;
        private String email;
        private String password;

        Member(long memberId, String memberName, String memberUri, boolean memberIsOnline, String email, String password) {
            setEmail(email);
            setMemberName(memberName);
            setOnline(memberIsOnline);
            setMemberUri(memberUri);
            setMemberId(memberId);
            setPassword(password);
        }

        String getEmail() {
            return email;
        }
        public boolean isOnline() {
            return memberIsOnline;
        }
        public String getMemberName() {
            return memberName;
        }
        public long getMemberId() {
            return memberId;
        }
        public String getMemberUri() {
            return memberUri;
        }
        public String getPassword() {
            return password;
        }

        private void setEmail(String email) { this.email = email; }
        void setOnline(boolean memberIsOnline) {
            this.memberIsOnline = memberIsOnline;
        }
        private void setMemberName(String memberName) {
            this.memberName = memberName;
        }
        private void setMemberUri(String memberUri) {
            this.memberUri = memberUri;
        }
        private void setMemberId(long memberId) {
            this.memberId = memberId;
        }
        void setPassword(String password) {
            this.password = password;
        }
    }
}

