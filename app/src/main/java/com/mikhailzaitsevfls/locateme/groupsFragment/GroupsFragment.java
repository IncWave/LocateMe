package com.mikhailzaitsevfls.locateme.groupsFragment;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikhailzaitsevfls.locateme.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class GroupsFragment extends Fragment implements GroupsFragmentInterface {

    private static boolean[] firstPressed = new boolean[]{true,true,true};

    @BindViews({
            R.id.fragment_group_delete_button,//0
            R.id.fragment_group_add_button,//1
            R.id.fragment_group_edit_button})//2
    public List<ImageButton> imageButtonsList;

    @BindView(R.id.fragment_group_expendable_listview)
    public ExpandableListView listView;

    private Unbinder unbinder;

    private ExpandableListAdapter listAdapter;

    private GroupsFragmentPresenterInterface presenter;

    private FusedLocationProviderClient fusedLocationProvider;


    public static GroupsFragment newInstance(){
        return new GroupsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (presenter == null){
            presenter = new GroupsFragmentPresenter();
        }
        presenter.attachView(this);
        listAdapter = new ExpandableListAdapter(getContext(), presenter.getGroupArray());
        presenter.isReady(listAdapter);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
    }

    @Override
    public void onPause() {
        super.onPause();
        firstPressed = new boolean[]{true,true,true};
        imageButtonsList.get(0).setImageResource(R.drawable.delete_grey_48dp);
        imageButtonsList.get(1).setImageResource(R.drawable.add_grey_48dp);
        imageButtonsList.get(2).setImageResource(R.drawable.edit_grey_48dp);
        imageButtonsList.get(0).setEnabled(true);
        imageButtonsList.get(1).setEnabled(true);
        imageButtonsList.get(2).setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        fusedLocationProvider = null;
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull final View view, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        presenter.onCreateContextMenu(menuInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        unbinder = ButterKnife.bind(this,view);

        registerForContextMenu(listView);
        listView.setAdapter(listAdapter);
        return view;
    }

    @OnClick({R.id.fragment_group_add_button,R.id.fragment_group_delete_button,R.id.fragment_group_edit_button})
    public void onClickBottomButtons(View view) {
        switch (view.getId()){
            case R.id.fragment_group_delete_button:
                if (firstPressed[0]){
                    if (presenter.isThereNoGroup()){break;}
                    imageButtonsList.get(0).setImageResource(R.drawable.delete_pink_48dp);
                    imageButtonsList.get(1).setEnabled(false);
                    imageButtonsList.get(2).setEnabled(false);
                    firstPressed[0] = !firstPressed[0];
                }else {
                    imageButtonsList.get(0).setImageResource(R.drawable.delete_grey_48dp);
                    imageButtonsList.get(1).setEnabled(true);
                    imageButtonsList.get(2).setEnabled(true);
                    firstPressed[0] = !firstPressed[0];
                }
                break;
            case R.id.fragment_group_add_button:
                if (firstPressed[1]){
                    imageButtonsList.get(0).setEnabled(false);
                    imageButtonsList.get(1).setImageResource(R.drawable.add_pink_48dp);
                    imageButtonsList.get(2).setEnabled(false);
                    firstPressed[1] = !firstPressed[1];
                    presenter.createChooseDialog();
                }else {
                    imageButtonsList.get(0).setEnabled(true);
                    imageButtonsList.get(1).setImageResource(R.drawable.add_grey_48dp);
                    imageButtonsList.get(2).setEnabled(true);
                    firstPressed[1] = !firstPressed[1];
                }
                break;
            case R.id.fragment_group_edit_button:
                if (firstPressed[2]){
                    if (presenter.isThereNoGroup()){break;}
                    imageButtonsList.get(0).setEnabled(false);
                    imageButtonsList.get(1).setEnabled(false);
                    imageButtonsList.get(2).setImageResource(R.drawable.edit_pink_48dp);
                    firstPressed[2] = !firstPressed[2];
                }else {
                    imageButtonsList.get(0).setEnabled(true);
                    imageButtonsList.get(1).setEnabled(true);
                    imageButtonsList.get(2).setImageResource(R.drawable.edit_grey_48dp);
                    firstPressed[2] = !firstPressed[2];
                }
                break;
        }
    }


    /////////////////////////////////////////createGroupDialog
    private MaterialEditText createGroupEditText;
    @Override
    public void setCreateGroupEditText(){
        createGroupEditText = Objects.requireNonNull(getView()).findViewById(R.id.create_group_dialog_set_name);
    }

    @Override
    public void setCreateGroupOkButton(){
        Objects.requireNonNull(getView()).findViewById(R.id.create_group_dialog_ok).setOnClickListener(v -> {
            if (createGroupEditText.getText().length() != 0){
                presenter.createGroup(createGroupEditText.getText().toString(),50);
            }
        });
    }
    ////////////createGroupDialog


    /////////////////////////////////////////addGroupDialog
    private MaterialEditText addGroupEditText;
    @Override
    public void setAddGroupEditText(){
        addGroupEditText = Objects.requireNonNull(getView()).findViewById(R.id.add_group_dialog_set_name);
    }

    @Override
    public void setAddGroupOkButton(){
        Objects.requireNonNull(getView()).findViewById(R.id.add_group_dialog_ok).setOnClickListener(v -> {

        });
    }
    ////////////addGroupDialog


/////////////////////////////////////////addMemberDialog
    @Override
    public void setAddMemberOkButton(){
        Objects.requireNonNull(getView()).findViewById(R.id.add_member_ok).setOnClickListener(v -> {

        });
    }

    @Override
    public void setAddMemberSpinner(ArrayAdapter<String> adapter){
        Spinner spinner = Objects.requireNonNull(getView()).findViewById(R.id.add_member_select_group_view);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private MaterialEditText addMemberEditText;
    @Override
    public void setAddMemberEditText(){
        addMemberEditText = Objects.requireNonNull(getView()).findViewById(R.id.add_member_id);
    }
    ////////////addMemberDialog


    @Override
    public void makeToast(String string) {
        Toast.makeText(getContext(),string,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean[] getFirstPressed() {
        return firstPressed;
    }

    @Override
    public AlertDialog createAlertDialog() {
        return new AlertDialog.Builder(getContext()).create();
    }

    @Override
    public AlertDialog.Builder createAlertDialogBuilder() {
        return new AlertDialog.Builder(getContext());
    }

    @Override
    public View createView(int resource) {
        return  getLayoutInflater().inflate(resource,null);
    }

    @Override
    public String findStringById(int resource) {
        return this.getResources().getString(resource);
    }

    @Override
    public List<ImageButton> getImageButtonsList(){
        return imageButtonsList;
    }

    @Override
    public Object getSystemService(String serviceName){
        return Objects.requireNonNull(getContext()).getSystemService(serviceName);
    }

    @Override
    public ArrayAdapter<String> getArrayAdapter(int resource,List<String> objects){
        return new ArrayAdapter<>(Objects.requireNonNull(getContext()), resource, objects);
    }

    @Override
    public void setOnSuccessListener(OnSuccessListener<Location> onSuccessListener){
        fusedLocationProvider.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()),onSuccessListener);
    }

    @Override
    public void dataHasBeenChanged(){
        presenter.dataHasBeenChanged();
    }
}