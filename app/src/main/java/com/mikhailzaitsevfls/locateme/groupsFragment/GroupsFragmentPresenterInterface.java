package com.mikhailzaitsevfls.locateme.groupsFragment;

import android.view.ContextMenu;

import com.mikhailzaitsevfls.locateme.model.Group;

import java.util.ArrayList;

public interface GroupsFragmentPresenterInterface {

    void attachView(GroupsFragmentInterface context);

    void detachView();

    void isReady(ExpandableListAdapter listAdapter);

    void onCreateContextMenu(ContextMenu.ContextMenuInfo contextMenuInfo);

    ArrayList<Group> getGroupArray();

    boolean isThereNoGroup();

    void createChooseDialog();

    void createGroup(String name, int radius);

    void dataHasBeenChanged();
}
