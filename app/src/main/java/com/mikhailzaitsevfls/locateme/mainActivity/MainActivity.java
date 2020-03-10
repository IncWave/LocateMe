package com.mikhailzaitsevfls.locateme.mainActivity;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithCallback;
import com.mikhailzaitsevfls.locateme.checking.permission.CheckPermission;
import com.mikhailzaitsevfls.locateme.R;
import com.mikhailzaitsevfls.locateme.groupsFragment.GroupsFragment;
import com.mikhailzaitsevfls.locateme.mapFragment.MapFragment;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivity;
import com.mikhailzaitsevfls.locateme.statisticFragment.StatisticFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements MainActivityInterface {

    @BindViews({
            R.id.activity_main_go_zero_fragment_button,//0
            R.id.activity_main_go_first_fragment_button,//1
            R.id.activity_main_go_second_fragment_button//2
    })
    public List<ImageButton> imageButtonsList;

    @BindView(R.id.activity_main_view_pager)
    public ViewPager viewPager;


    private StatisticFragment statisticFragment;
    private GroupsFragment groupsFragment;
    private MapFragment mapFragment;

    private MainActivityPresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        groupsFragment = GroupsFragment.newInstance();
        mapFragment = MapFragment.newInstance();
        statisticFragment = StatisticFragment.newInstance();

        init();


        //ViewPager find and init
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        goZeroFragment();
                        break;
                    case 1:
                        goFirstFragment();
                        break;
                    case 2:
                        goSecondFragment();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void init(){
        presenter = new MainActivityPresenter();
        presenter.attachView(this);
        presenter.isReady();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detachView();
        viewPager.clearOnPageChangeListeners();
    }


    public void onClickOrientation(View view) {
        switch (view.getId()){
            case R.id.activity_main_go_zero_fragment_button:
                viewPager.setCurrentItem(0);
                goZeroFragment();
                break;
            case R.id.activity_main_go_first_fragment_button:
                viewPager.setCurrentItem(1);
                goFirstFragment();
                break;
            case R.id.activity_main_go_second_fragment_button:
                viewPager.setCurrentItem(2);
                goSecondFragment();
                break;
        }
    }

    private void goZeroFragment(){
        imageButtonsList.get(0).setImageResource(R.drawable.list_36dp);
        imageButtonsList.get(1).setImageResource(R.drawable.map_pin_grey36dp);
        imageButtonsList.get(2).setImageResource(R.drawable.chart_grey36dp);
    }
    private void goFirstFragment(){
        imageButtonsList.get(0).setImageResource(R.drawable.list_grey36dp);
        imageButtonsList.get(1).setImageResource(R.drawable.map_pin_36dp);
        imageButtonsList.get(2).setImageResource(R.drawable.chart_grey36dp);
    }
    private void goSecondFragment(){
        imageButtonsList.get(0).setImageResource(R.drawable.list_grey36dp);
        imageButtonsList.get(1).setImageResource(R.drawable.map_pin_grey36dp);
        imageButtonsList.get(2).setImageResource(R.drawable.chart_36dp);
    }


    private class CustomPagerAdapter extends FragmentPagerAdapter {
        CustomPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 2:
                    return statisticFragment;
                case 1:
                    return mapFragment;
                default:
                case 0:
                    return groupsFragment;
            }
        }
        @Override
        public int getCount() {
            return 3;
        }
    }


    //For MapFragment.requirePermissions & MainActivityPresenter.requirePermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PresenterInterfaceWithCallback presenter = sparseArrayPresenters.get(requestCode);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (presenter.isDetached())
            presenter.callBack(false);
        } else {
            presenter.callBack(true);
        }
    }


    @Override
    public void startNewActivity(Class cl){
        Intent intent = new Intent(this,cl);
        if (cl == NoInternetActivity.class){
            startActivity(intent.putExtra(NoInternetActivity.KEY_NAME,NoInternetActivity.MAIN_ACTIVITY));
        }else {
            startActivity(intent);
        }
        finish();
    }


    @Override
    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public Object getSysService(String serviceName){
        return this.getSystemService(serviceName);
    }


    @Override
    public NotificationCompat.Builder getNotificationBuilder(String whatGeofences, String chanelId){
        return
                new NotificationCompat.Builder(this,chanelId)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Title")
                        .setContentText(whatGeofences);
    }


    @Override
    public boolean checkPermission(){
        return CheckPermission.checkPermission(this);
    }


    private SparseArray<PresenterInterfaceWithCallback> sparseArrayPresenters;
    int presenterN = 0;
    @Override
    public void requirePermission(PresenterInterfaceWithCallback presenter){
        sparseArrayPresenters = new SparseArray<>();
        sparseArrayPresenters.put(presenterN,presenter);
        CheckPermission.requirePermission(this, presenterN);
        presenterN++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startCheckingDbChanges();
        presenter.startCheckingInternet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopCheckingDbChanges();
        presenter.stopCheckingInternet();
    }

    @Override
    public void dataHasBeenChanged(){
        mapFragment.dataHasBeenChanged();
        groupsFragment.dataHasBeenChanged();
    }

    @Override
    public void onlineHasBeenChanged(){
        groupsFragment.dataHasBeenChanged();
    }




}