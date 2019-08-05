//package com.punuo.pet.cirlce;
//
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.punuo.pet.cirlce.adapter.CircleFragmentPagerAdapter;
//import com.punuo.pet.cirlce.fragment.AddFragment;
//import com.punuo.pet.cirlce.fragment.ContactsFragment;
//import com.punuo.pet.cirlce.fragment.MyCircleFragment;
//import com.punuo.pet.cirlce.fragment.NearbyFragment;
//import com.punuo.pet.cirlce.fragment.WorldFragment;
//import com.punuo.pet.router.CircleRouter;
//
//import java.util.ArrayList;
//
//
///**
// * Created by han.chen.
// * Date on 2019-06-25.
// * 宠友圈
// **/
////@Route(path = CircleRouter.ROUTER_CIRCLE_ACTIVITY)
//public class CircleActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
//
//    Button button1;
//    Button button2;
//    Button button3;
//    Button button4;
//    Button button5;
//    ImageView cursor;
//    ViewPager myViewpager;
//    //指示标签的横坐标
//    float cursorX = 0;
//    //按钮的宽度的数组
//    int[] widthArgs;
//    //标题按钮的数组
//    Button[] btnArgs;
//    //fragment的集合，对应的每个子界面
//    ArrayList<Fragment> fragments;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_main_circle);
//
//        initView();
//    }
//
//    //初始化布局
//    public void initView(){
//        myViewpager = (ViewPager)this.findViewById(R.id.circle_viewpager);
//        button1 = findViewById(R.id.button_contacts);
//        button2= findViewById(R.id.button_mycircle);
//        button3 = findViewById(R.id.button_nearby);
//        button4 = findViewById(R.id.button_world);
//        button5 =findViewById(R.id.button_add);
//
//        //初始化按钮数组
//        btnArgs = new Button[]{button1,button2,button3,button4,button5};
//        //指示标签设置成蓝色
//        cursor = (ImageView)this.findViewById(R.id.image1);
//        cursor.setBackgroundColor(Color.BLUE);
//
//        button1.setOnClickListener(this);
//        button2.setOnClickListener(this);
//        button3.setOnClickListener(this);
//        button4.setOnClickListener(this);
//        button5.setOnClickListener(this);
//        myViewpager.setOnPageChangeListener(this);
//
//        fragments = new ArrayList<Fragment>();
//        fragments.add(new ContactsFragment());
//        fragments.add(new MyCircleFragment());
//        fragments.add(new NearbyFragment());
//        fragments.add(new WorldFragment());
//        fragments.add(new AddFragment());
//
//        CircleFragmentPagerAdapter circleFragmentPagerAdapter = new CircleFragmentPagerAdapter(getSupportFragmentManager(),fragments);
//
//        myViewpager.setAdapter(circleFragmentPagerAdapter);
//
//        //重置按钮的颜色
//        resetButtonColor();
//        //将我的圈按钮设置成蓝色，表示默认选中
//        button2.setTextColor(Color.BLUE);
//    }
//
//    public void resetButtonColor(){
//        button2.setBackgroundColor(Color.parseColor("#DCDCDC"));
//        button3.setBackgroundColor(Color.parseColor("#DCDCDC"));
//        button4.setBackgroundColor(Color.parseColor("#DCDCDC"));
//        button2.setTextColor(Color.BLACK);
//        button3.setTextColor(Color.BLACK);
//        button4.setTextColor(Color.BLACK);
//    }
//
//
//    @Override
//    public void onPageSelected(int position) {
//
//        //widthArgs的实例化
//        if(widthArgs==null){
//            widthArgs = new int[]{button1.getWidth(),button2.getWidth(),button3.getWidth(),button4.getWidth(),button5.getWidth()};
//        }
//
//        //每次滑动首先重置按钮的颜色
//        resetButtonColor();
//        //将滑动到的当前按钮设置成蓝色
//        btnArgs[position].setTextColor(Color.BLUE);
//        cursorAnim(position);
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if(id==R.id.button_contacts){
//            myViewpager.setCurrentItem(0);
//            cursorAnim(0);
//        } else if(id==R.id.button_mycircle){
//            myViewpager.setCurrentItem(1);
//            cursorAnim(1);
//        }else if(id==R.id.button_nearby){
//            myViewpager.setCurrentItem(2);
//            cursorAnim(2);
//        }else if(id==R.id.button_world){
//            myViewpager.setCurrentItem(3);
//            cursorAnim(3);
//        }else if(id==R.id.button_add){
//            myViewpager.setCurrentItem(4);
//            cursorAnim(4);
//        }
//    }
//
//    //指示器的跳转，传入当前所处当前所处的页面的下标
//    public void cursorAnim(int curItem){
//        //每次调用，都将指示器的横坐标设置为0，即开始的位置
//        cursorX = 0;
//        //根据当前的curItem来设置指示器的宽度
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
//        //减去边距*2，对齐标题栏文字
//        lp.width = widthArgs[curItem]-btnArgs[0].getPaddingLeft()*2;
//        cursor.setLayoutParams(lp);
//        //循环获取当前页之前的所有页面的宽度
//        for(int i=0;i<curItem;i++){
//            cursorX = cursorX + btnArgs[i].getWidth();
//        }
//        //再加上当前页面的左边距，即为指示器当前应处的位置
//        cursor.setX(cursorX+btnArgs[curItem].getPaddingLeft());
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//
//}