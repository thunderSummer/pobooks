package com.oureda.thunder.pobooks.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.person.PersonInformationActivity;
import com.oureda.thunder.pobooks.activity.person.ReadNoteActivity;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.manager.CacheManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonCenterActivity extends BaseActivity {

    @BindView(R.id.picture_person)
    CircleImageView picturePerson;
    @BindView(R.id.name_person_center)
    TextView namePersonCenter;
    @BindView(R.id.person_information_person)
    TextView personInformationPerson;
    @BindView(R.id.password_change_person)
    TextView passwordChangePerson;
    @BindView(R.id.my_note_person)
    TextView myNotePerson;
    @BindView(R.id.my_read_check_in_person)
    TextView myReadCheckInPerson;
    @BindView(R.id.my_read_record_person)
    TextView myReadRecordPerson;
    @BindView(R.id.toolbar_person)
    Toolbar toolbarPerson;
    @BindView(R.id.drawer_person_center)
    DrawerLayout drawerPersonCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_person, "个人中心", R.drawable.ic_menu);
        namePersonCenter.setText(CacheManager.getInstance().getUserName());
    }

    @OnClick({R.id.picture_person, R.id.name_person_center, R.id.person_information_person, R.id.password_change_person, R.id.my_note_person, R.id.my_read_check_in_person, R.id.my_read_record_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.picture_person:
                break;
            case R.id.name_person_center:
                break;
            case R.id.person_information_person:
                startActivity(new Intent(PersonCenterActivity.this, PersonInformationActivity.class));
                break;
            case R.id.password_change_person:
                break;
            case R.id.my_note_person:
                startActivity(new Intent(PersonCenterActivity.this, ReadNoteActivity.class));
                break;
            case R.id.my_read_check_in_person:
                break;
            case R.id.my_read_record_person:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerPersonCenter.openDrawer(Gravity.START);

        }
        return super.onOptionsItemSelected(item);
    }
}
