package com.mikhailzaitsevfls.locateme.groupsFragment;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.mikhailzaitsevfls.locateme.model.Db;
import com.mikhailzaitsevfls.locateme.model.Group;
import com.mikhailzaitsevfls.locateme.R;

import java.util.ArrayList;
import java.util.Objects;


public class GroupsFragmentPresenter implements GroupsFragmentPresenterInterface {

    private GroupsFragmentInterface context;
    private String currentUserIdThatCouldBeShowed;
    private ExpandableListAdapter listAdapter;

    @Override
    public void attachView(GroupsFragmentInterface context) {
        this.context = context;
    }

    @Override
    public void detachView() {
        this.context = null;
    }

    @Override
    public void isReady(ExpandableListAdapter listAdapter) {
        currentUserIdThatCouldBeShowed = findCurrentUserIdThatCouldBeShowed();
        this.listAdapter = listAdapter;
    }

    private String findCurrentUserIdThatCouldBeShowed(){
        /////////////////////////////////////////////////////something with BD
        return "545QQi";
    }

    private void deleteGroup(final int group) {
        Db.newInstance().deleteGroupByIndex(group);
    }

    @Override
    public void dataHasBeenChanged(){
        listAdapter.notifyDataSetChanged();
    }

///////////////////////////////////////////////////////////////
    private String getGroupName() {
        return Db.newInstance().getGroupNameByIndex(0);
    }
/////////////////////////////////////////////////////////////////


    private void changeGroupName(int group, String name) {
        Db.newInstance().changeGroupNameByIndex(group,name);
    }

    private void deleteMember(int group, int member) {
        Db.newInstance().deleteMemberFromGroupByIndex(group, member);
    }

    @Override
    public boolean isThereNoGroup(){
        try {
            //noinspection ResultOfMethodCallIgnored
            Db.newInstance().getGroups().get(0);
        }catch (Exception e){
            context.makeToast("There're no groups here...");
            return true;
        }
        return false;
    }

    @Override
    public void createGroup(String name, int radius){
        Db.newInstance().createOrUpdateGroup(name,radius);
    }

    @Override
    public ArrayList<Group> getGroupArray(){
        return Db.newInstance().getGroups();
    }

    private String getCurrentUserIdThatCouldBeShowed() {
        return currentUserIdThatCouldBeShowed;
    }

    @Override
    public void onCreateContextMenu(ContextMenu.ContextMenuInfo contextMenuInfo) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) contextMenuInfo;
        assert info != null;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        final int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        final int child = ExpandableListView.getPackedPositionChild(info.packedPosition);

        boolean[] firstPressed = context.getFirstPressed();

        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
            if (!firstPressed[0]){
                AlertDialog alertDialogDelete = context.createAlertDialog();

                alertDialogDelete.setMessage(context.findStringById(R.string.sure_delete_g));

                alertDialogDelete.setButton(AlertDialog.BUTTON_POSITIVE, context.findStringById(R.string.yes),
                        (dialogInterface, i) -> deleteGroup(group));

                alertDialogDelete.setButton(AlertDialog.BUTTON_NEGATIVE, context.findStringById(R.string.no),
                        (dialogInterface, i) -> alertDialogDelete.dismiss());

                alertDialogDelete.show();
            }

            if (!firstPressed[2]){
                AlertDialog.Builder alertDialogEdit = context.createAlertDialogBuilder();
                alertDialogEdit.setTitle(R.string.name);

                View customDialog = context.createView(R.layout.edit_dialog);
                alertDialogEdit.setView(customDialog);

                final EditText editText = customDialog.findViewById(R.id.edit_dialog_text);
                editText.setText(getGroupName());

                alertDialogEdit.setPositiveButton(context.findStringById(R.string.ok),
                        (dialogInterface, i) -> changeGroupName(group,editText.getText().toString()));
                alertDialogEdit.show();
            }


        }else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){

            if (!firstPressed[0]){
                AlertDialog alertDialog = context.createAlertDialog();
                alertDialog.setMessage(context.findStringById(R.string.sure_delete_m));
                /////////////////////////////////////////////////////////////////////////////////////////////////
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.findStringById(R.string.yes),
                        (dialogInterface, i) -> deleteMember(group,child));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.findStringById(R.string.no),
                        (dialog,num) -> dialog.cancel());
                alertDialog.show();
            }
        }
    }

    @Override
    public void createChooseDialog(){
        String [] choose = {
                context.findStringById(R.string.get_my_id),
                context.findStringById(R.string.create_group),
                context.findStringById(R.string.add_group),
                context.findStringById(R.string.add_member)};
        AlertDialog.Builder builder = context.createAlertDialogBuilder()
                .setItems(choose, (dialogInterface, i) ->{
                    switch (i){
                        case 0:
                            dialogInterface.dismiss();
                            context.getImageButtonsList().get(0).callOnClick();
                            getMyIdDialog();
                            break;
                        case 1:
                            dialogInterface.dismiss();
                            context.getImageButtonsList().get(1).callOnClick();
                            createNewGroupDialog();
                            break;
                        case 2:
                            dialogInterface.dismiss();
                            context.getImageButtonsList().get(2).callOnClick();
                            addGroupDialog();
                            break;
                        case 3:
                            dialogInterface.dismiss();
                            context.getImageButtonsList().get(3).callOnClick();
                            addMemberDialog();
                            break;
                    }
                });
        builder.show();
    }

    private void addMemberDialog(){
        final AlertDialog builder1 = context.createAlertDialog();
        final View dialogView = context.createView(R.layout.add_member);

        ArrayAdapter<String> adapter = context.getArrayAdapter(android.R.layout.simple_spinner_item, Db.newInstance().getGroupNamesArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        context.setAddMemberEditText();
        context.setAddMemberOkButton();
        context.setAddMemberSpinner(adapter);

        builder1.setView(dialogView);
        builder1.show();
    }

    private void addGroupDialog(){
        final AlertDialog builder1 = context.createAlertDialog();
        final View dialogView = context.createView(R.layout.add_group_dialog);

        context.setAddGroupEditText();
        context.setAddGroupOkButton();

        builder1.setView(dialogView);
        builder1.show();
    }

    private void createNewGroupDialog(){
        if (Db.newInstance().getGroups().size()>=100){
            context.makeToast(context.findStringById(R.string.theres_a_limit_to_100_groups));
        }else { context.setOnSuccessListener((location -> {
            if (location != null){
                Db.newInstance().setLocation(location);
                final AlertDialog builder1 = context.createAlertDialog();
                final View dialogView = context.createView(R.layout.create_group_dialog);

                context.setCreateGroupEditText();
                context.setCreateGroupOkButton();

                builder1.setView(dialogView);
                builder1.show();
            }else {
                context.makeToast(context.findStringById(R.string.your_last_known_location_wasnt_defined));
            }
        }));
        }
    }


    //copy id to cash
    private void getMyIdDialog(){
        ClipboardManager clipboard = (ClipboardManager)  context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", getCurrentUserIdThatCouldBeShowed());
        Objects.requireNonNull(clipboard).setPrimaryClip(clip);
        context.makeToast(context.findStringById(R.string.your_id_was_copied));
    }

}
