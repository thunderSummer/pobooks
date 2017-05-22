package com.oureda.thunder.pobooks.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.MyViewPager;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.base.ColorChange;
import com.oureda.thunder.pobooks.fragment.bookRoom.FragmentRoomHasRead;
import com.oureda.thunder.pobooks.fragment.bookRoom.FragmentRoomShare;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookRoomActivity extends BaseActivity {

    @BindView(R.id.share_book_room)
    TextView shareBookRoom;
    @BindView(R.id.has_read_book_room)
    TextView hasReadBookRoom;
    @BindView(R.id.underline_book_room)
    ImageView underlineBookRoom;
    @BindView(R.id.viewPage_book_room)
    MyViewPager viewPageBookRoom;
    @BindView(R.id.drawer_book_room)
    DrawerLayout drawerBookRoom;
    private List<TextView> textViews;
    private List<Fragment> fragmentList;
    private FragmentRoomHasRead fragmentRoomHasRead;
    private FragmentRoomShare fragmentRoomShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        ButterKnife.bind(this);
        initViewPager();
        initToolbar(R.id.toolbar_book_room,"书房",R.drawable.ic_menu);
        shareBookRoom.setOnClickListener(new MyClickListen(0));
        hasReadBookRoom.setOnClickListener(new MyClickListen(1));
    }
    private void initViewPager(){
        fragmentRoomHasRead=new FragmentRoomHasRead();
        fragmentRoomShare = new FragmentRoomShare();
        textViews=new ArrayList<>();
        fragmentList=new ArrayList<>();
        fragmentList.add(fragmentRoomShare);
        fragmentList.add(fragmentRoomHasRead);
        textViews.add(shareBookRoom);
        textViews.add(hasReadBookRoom);
        viewPageBookRoom.initAll(2,R.id.underline_book_room,fragmentList,getSupportFragmentManager(),getWindow().getDecorView());
        viewPageBookRoom.setCurrentItem(0);
        viewPageBookRoom.setAdapter(viewPageBookRoom.myAdapter);
        viewPageBookRoom.setOnPageChangeListener(viewPageBookRoom.listener);
        viewPageBookRoom.setColorChange(new ColorChange() {
            @Override
            public void OnColorChange() {
                for(int i=0;i<textViews.size();i++){
                    if(i==viewPageBookRoom.getCurrentItem()){
                        textViews.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        textViews.get(i).setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }
            }
        });
    }
    private class MyClickListen implements View.OnClickListener{
        private int position;

        public MyClickListen(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            viewPageBookRoom.setCurrentItem(position);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerBookRoom.openDrawer(Gravity.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
