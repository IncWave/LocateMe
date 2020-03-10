package com.mikhailzaitsevfls.locateme.model;

import android.location.Location;

import com.google.android.gms.maps.model.Circle;
import com.mikhailzaitsevfls.locateme.checking.db.CheckLocalDbChanges;
import com.mikhailzaitsevfls.locateme.login.LoginActivityPresenterInterface;
import com.mikhailzaitsevfls.locateme.registrationActivity.RegistrationActivityPresenterInterface;

import java.util.ArrayList;
import java.util.Calendar;


public class Db implements DbModelInterface {
    private static Group.Member owner;
    private static ArrayList<Group> groupsArrayList;
    private static Db db;
    private Location location;

    private Db() {
        if (groupsArrayList == null){
            groupsArrayList = new ArrayList<>();
            downloadListOfGroups();
        }
    }

    public static Db newInstance() {
        if (db == null){
            db = new Db();
        }
        return db;
    } //+

    public ArrayList<String> getGroupNamesArray(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < groupsArrayList.size(); i++){
            arrayList.add(i, groupsArrayList.get(i).getGroupName());
        }
        return arrayList;
    }

    public String getGroupNameByIndex(int groupIndex){
        return groupsArrayList.get(groupIndex).getGroupName();
    }

    private void setOwner(Group.Member owner) {
        Db.owner = owner;
    }

    public void changeMyOnlineByUserId(ArrayList<String> arrayIdList, Boolean inOrNot){
        /*x:for (int j = 0; j < arrayIdList.size(); j++) {
            for (int i = 0; i < groupsArrayList.size(); i++) {
                if (Long.parseLong(arrayIdList.get(j)) == (groupsArrayList.get(i).getGroupId())) {
                    for (int k = 0; k < groupsArrayList.get(i).getMembers().size(); k++){
                        if (currentUserFirebaseId.equals(groupsArrayList.get(i).getMembers().get(k).getEmail())){
                            groupsArrayList.get(i).getMembers().get(k).setOnline(inOrNot);
                            continue x;
                        }
                    }
                }
            }
        }
        CheckLocalDbChanges.onlineChanged();*/
    }


    private ArrayList<Group.Member> createMemberList(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(owner);
        return array;
    }


    private void downloadListOfGroups(){
        downloadGroups();
    }//delete


    public ArrayList<Group> saveCirclesChanges(ArrayList<Circle> circleArrayList) {
        Circle circle;
        for (int i=0; i<circleArrayList.size(); i++ ){
            circle = circleArrayList.get(i);
            groupsArrayList.get(i).setLongitude(circle.getCenter().longitude);
            groupsArrayList.get(i).setLatitude(circle.getCenter().latitude);
            groupsArrayList.get(i).setRadius((int)circle.getRadius());
        }
        CheckLocalDbChanges.dataChanged();
        return groupsArrayList;
    }



    /*public String calculateCurrentUserIdThatCouldBeShowed(){
        String[] alphab = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
                "r", "s", "t", "u", "v", "w", "x", "y", "z", "A","B","C","D","E","F","G","H","I","J","K","L",
                "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] codes = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", " 10 ", " 11 ", " 12 "
                , " 13 ", " 14 ", " 15 ", " 16 ", " 17 ", " 18 ", " 19 ", " 20 ", " 21 ", " 22 ", " 23 ", " 24 "
                , " 25 ", " 26 "," 27 "," 28 "," 29 "," 30 "," 31 "," 32 "," 33 "," 34 "," 35 "," 36 "
                ," 37 "," 38 "," 39 "," 40 "," 41 "," 42 "," 43 "," 44 "," 45 "," 46 "," 47 "," 48 "," 49 "," 50 "
                ," 51 "," 52 "};
        String result = currentUserId.substring(0,5);
        String strnum = " " + currentUserId.substring(5) + " ";
        for (int i = 0; i < alphab.length; i++) {
            strnum = strnum.replaceAll(alphab[i], codes[i]);
        }
        String[] words = strnum.split(" ");
        int num = 0;
        for (int i = 0; i <words.length; i++){
            if (words[i].length() != 0) {
                num += (Integer.parseInt(words[i]))*i;
            }
        }
        return result + num + words.length + currentUserName.length();
    }
*/

    @Override
    public void createOrUpdateCurrentUser(Group.Member member) {
        CheckLocalDbChanges.onlineChanged();
    }

    @Override
    public ArrayList<Group.Member> addMember(Group group, String idThatCouldBeShowed){
        CheckLocalDbChanges.onlineChanged();
        return null;////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void createOrUpdateGroup(String groupName, int radius){
        groupsArrayList.add(new Group(
                Math.round(radius + Calendar.getInstance().getTimeInMillis())/41
                ,groupName
                ,createMemberList()
                ,location.getLatitude()
                ,location.getLongitude()
                ,radius,owner.getMemberId()));
        /////////////////////////////////////////////////////////////////////////send to the server
        CheckLocalDbChanges.dataChanged();
    }

    @Override
    public ArrayList<Group> saveGroupsCircleChanges(ArrayList<Circle> circleArrayList){
        Circle circle;
        for (int i=0; i<circleArrayList.size(); i++ ){
            circle = circleArrayList.get(i);
            groupsArrayList.get(i).setLongitude(circle.getCenter().longitude);
            groupsArrayList.get(i).setLatitude(circle.getCenter().latitude);
            groupsArrayList.get(i).setRadius((int)circle.getRadius());
        }
        CheckLocalDbChanges.dataChanged();
        return groupsArrayList;
    }

    @Override
    public void deleteDb(){
        groupsArrayList = null;
        db = null;
    }

    @Override
    public void downloadGroups() {
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(1, "1", "an", false, "RdKxfPTuHXRdNENmEd6vrg15dzTs1", "1"));
        array.add(new Group.Member(2, "2", "and", false, "asdasdsadasdENmEgd6vrg15dzyf", "2"));
        array.add(new Group.Member(3, "3", "an2d", false, "asdasdsadasdEzNmEd6vwwqrg15dzyf", "3"));
        array.add(new Group.Member(4, "4", "awnd", false, "asdasdsew3adaszdENmEd6vrg15dzyf", "4"));


        ArrayList<Group.Member> array1 = new ArrayList<>();
        array1.add(new Group.Member(5, "5", "ana", false, "RdKxfPTuHXRdNENmEd6vrg15dzTs1", "5"));
        array1.add(new Group.Member(6, "6", "andsx", false, "asdasdsadcasdENmEgd6vrg15dzyf", "6"));
        array1.add(new Group.Member(7, "7", "an2cd", false, "asdasdsadavsdEzNmEd6vwwqrg15dzyf", "7"));
        array1.add(new Group.Member(8, "8", "aasdwnd", false, "asdasdsewb3adaszdENmEd6vrg15dzyf", "8"));

        ArrayList<Group.Member> array2 = new ArrayList<>();
        array2.add(new Group.Member(9, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(10, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(11, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(12, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));

        array2.add(new Group.Member(13, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(14, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(15, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(16, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));

        array2.add(new Group.Member(17, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(18, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(19, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(20, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));

        array2.add(new Group.Member(21, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(22, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(23, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(24, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));

        array2.add(new Group.Member(25, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(26, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(27, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(28, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));

        array2.add(new Group.Member(9, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(10, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(11, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(12, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));

        array2.add(new Group.Member(9, "9", "anww", false, "RdKxfPTuHXRdNENmEd6vrg15dzTsw1", "9"));
        array2.add(new Group.Member(10, "10", "andeee", false, "asdasdsadasdENmEgd6vrg15dzsyf", "10"));
        array2.add(new Group.Member(11, "11", "an2rrrd", false, "asdasdsadasdEzNmEd6vwwqrag15dzyf", "11"));
        array2.add(new Group.Member(12, "12", "awnzzd", false, "asdasdsew3adaszdENmEd6vzrg15dzyf", "12"));


        groupsArrayList.add(new Group(111, "1", array, 53.894810, 27.509498, 50, 1));
        groupsArrayList.add(new Group(222, "2", array1, 53.894810, 27.509198, 60, 2));
        groupsArrayList.add(new Group(333, "3", array2, 53.894110, 27.509900, 70, 1));
    }

    @Override
    public void downloadOneGroup(){}

    @Override
    public void downloadOneMember(){}

    @Override
    public ArrayList<Group> saveGroupCircleChanges(Circle circle, int i){
        groupsArrayList.get(i).setLongitude(circle.getCenter().longitude);
        groupsArrayList.get(i).setLatitude(circle.getCenter().latitude);
        groupsArrayList.get(i).setRadius((int)circle.getRadius());
        CheckLocalDbChanges.dataChanged();
        return groupsArrayList;
    }

    @Override
    public void deleteMemberFromGroupByIndex(int groupI, int memberI){
        groupsArrayList.get(groupI).getMembers().remove(memberI);
        CheckLocalDbChanges.onlineChanged();
    }

    @Override
    public void deleteGroupByIndex(int groupI){
        groupsArrayList.remove(groupI);
        CheckLocalDbChanges.dataChanged();
    }

    @Override
    public void changeGroupNameByIndex(int groupIndex, String name){
        groupsArrayList.get(groupIndex).setGroupName(name);
        CheckLocalDbChanges.dataChanged();
    }

    @Override
    public ArrayList<Group> getGroups(){
        return groupsArrayList;
    }


    @Override
    public ArrayList<Group.Member> getMembers(int groupIndex){
        return groupsArrayList.get(groupIndex).getMembers();
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    //registration & login
    @Override
    public void checkForAccountExisting(String email, String password, LoginActivityPresenterInterface presenter) {
        /////////////////////////////////////////////////////////////////delete
        if (password.equals("111111Qq") && email.equals("1111@11.com")){
            presenter.inputConfirmed(true);
        /////////////////////////////////////////////////////////////////
            setOwner(new Group.Member(0,"1111Q1","q",true,email,password));
        }else {
            presenter.inputConfirmed(false);
        }
        //retrofit 2

    }

    //registration & login
    @Override
    public void checkForAccountExistingAndSaveIfNot(String name, String email, String password, RegistrationActivityPresenterInterface presenter) {

        ///////////////////////////////////////////////////delete
        if (name.equals("1111Qq") && email.equals("1111@11.com") && password.equals("111111Qq")){
            presenter.inputConfirmed(true);
        ///////////////////////////////////////////////////
            setOwner(new Group.Member(0,"1111Q1","q",true,email,password));
        }else {
            presenter.inputConfirmed(false);
        }
        //retrofit 2
    }
}