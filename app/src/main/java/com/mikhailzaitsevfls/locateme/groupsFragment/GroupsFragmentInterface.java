package com.mikhailzaitsevfls.locateme.groupsFragment;

import android.app.AlertDialog;
import android.location.Location;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public interface GroupsFragmentInterface {

    void setCreateGroupEditText();

    void setCreateGroupOkButton();

    void setAddGroupEditText();

    void setAddGroupOkButton();

    void setAddMemberOkButton();

    void setAddMemberSpinner(ArrayAdapter<String> adapter);

    void setAddMemberEditText();

    void makeToast(String string);

    boolean[] getFirstPressed();

    AlertDialog createAlertDialog();

    AlertDialog.Builder createAlertDialogBuilder();

    View createView(int resource);

    String findStringById(int resource);

    Object getSystemService(String serviceName);

    List<ImageButton> getImageButtonsList();

    ArrayAdapter<String> getArrayAdapter(int resource, List<String> objects);

    void setOnSuccessListener(OnSuccessListener<Location> onSuccessListener);

    void dataHasBeenChanged();
}
