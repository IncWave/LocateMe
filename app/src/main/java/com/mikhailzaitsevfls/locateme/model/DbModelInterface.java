package com.mikhailzaitsevfls.locateme.model;

import android.location.Location;

import com.google.android.gms.maps.model.Circle;
import com.mikhailzaitsevfls.locateme.login.LoginActivityPresenterInterface;
import com.mikhailzaitsevfls.locateme.model.Group;
import com.mikhailzaitsevfls.locateme.registrationActivity.RegistrationActivityPresenterInterface;

import java.util.ArrayList;

public interface DbModelInterface {

    void checkForAccountExisting(String inputEmail,String inputPassword, LoginActivityPresenterInterface presenter);

    void checkForAccountExistingAndSaveIfNot(String name, String email, String password, RegistrationActivityPresenterInterface presenter);

    void deleteDb();

    ArrayList<Group> saveGroupsCircleChanges(ArrayList<Circle> circleArrayList);

    ArrayList<Group> saveGroupCircleChanges(Circle circle, int i);

    void createOrUpdateGroup(String groupName, int radius);

    void deleteGroupByIndex(int groupI);

    void changeGroupNameByIndex(int groupIndex, String name);


    void createOrUpdateCurrentUser(Group.Member member); //create/update owner information

    void deleteMemberFromGroupByIndex(int groupI, int memberI);

    ArrayList<Group.Member> addMember(Group group, String idThatCouldBeShowed);//add user by shown id in some group

    void downloadGroups();

    void downloadOneGroup();//mb

    void downloadOneMember();//mb

    ArrayList<Group> getGroups(); //get downloaded groups

    ArrayList<Group.Member>  getMembers(int groupIndex); //get downloaded members from group


    void setLocation(Location location);

}
