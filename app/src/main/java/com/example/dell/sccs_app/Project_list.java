package com.example.dell.sccs_app;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.sccs_app.FragmentDesign.ControllerListFragment;
import com.example.dell.sccs_app.FragmentDesign.LampListFragment;
import com.example.dell.sccs_app.Widgets.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;
import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.basicURL;
import static com.example.dell.sccs_app.StaticValue.connectURL;
import static com.example.dell.sccs_app.StaticValue.first_in;

public class Project_list extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private NavigationView mNavigationView;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mFragments;
    private AppBarLayout  mAppBarLayout;
    private String[] mtest;

    private LampListFragment Fragment_Lamp;
    private ControllerListFragment Fragment_Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        initview();
        initset();
        config();

        Intent intent = getIntent();


        /*Toolbar bar=(Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(intent.getExtras().getString("name"));
        bar.setTitleTextColor(getResources().getColor(R.color.white));
        bar.setTitleTextAppearance(this,R.style.ToolBar);
        setSupportActionBar(bar);

        bar.setNavigationIcon(R.drawable.ic_action_back);
        bar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Project_list.this,Project.class);
                startActivity(intent);
                Toast.makeText(Project_list.this,"UP",Toast.LENGTH_SHORT).show();
            }
        });
        */

        initview();
        initset();

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mtest[position]);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageScrollStateChanged(int state) {

    }

    private void initview(){
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.project_lamp_list);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_new);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_new);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout_new);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_lamp);

    }

    private void initset(){
        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        //mTitles = getResources().getStringArray(R.array.tab_titles);
        mtest = getResources().getStringArray(R.array.test_list);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        Fragment_Controller = new ControllerListFragment();
        /*Bundle mBundle = new Bundle();
        mBundle.putInt("flag",0);
        Fragment_Project = new Fragments();
        Fragment_Project.setArguments(mBundle);
        */
        Fragment_Lamp = new LampListFragment();


        mFragments.add(0,Fragment_Controller);
        mFragments.add(1,Fragment_Lamp);
    }

    private void config(){

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Project_list.this,Project.class);
                startActivity(intent);
            }
        });


        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mtest, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }

    public ViewPager getmViewPager(){
        return mViewPager;
    }

   /* public void onOKButtonPressed(String ssid) {
        if(first_in)
            Fragment_Lamp.onBeitieSelected(ssid);
        else
            Fragment_Lamp.onBeitieSelected(null);
    }
    */


}
