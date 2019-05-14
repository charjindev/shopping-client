package top.charjin.shoppingclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import top.charjin.shoppingclient.R;
import top.charjin.shoppingclient.data.BottomNavResource;

/**
 * 主活动,滑动介绍页面(即SlideActivity)显示过后,app的视图
 */
public class AppActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private Fragment[] mFragments;

    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);

//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.init();

        Log.e("ACTIVITY", "dads9f9deaf9d");
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    private void initView() {
        mFragments = BottomNavResource.getFragments();
        mTabLayout = findViewById(R.id.bottom_tab_layout);

        // 添加底部Tab
        View[] tabViews = BottomNavResource.createTabView(this);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceShownFragment(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    // 获取自定义的Tab
                    View custom_tab = mTabLayout.getTabAt(i).getCustomView();

                    ImageView icon = custom_tab.findViewById(R.id.tab_content_image);
                    TextView text = custom_tab.findViewById(R.id.tab_content_text);
                    // 如果被选中,修改对应样式
                    if (i == tab.getPosition()) {
                        icon.setImageResource(BottomNavResource.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        // 未选中,修改对应样式
                        icon.setImageResource(BottomNavResource.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        for (View tabView : tabViews) mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView));
    }

    /**
     * 监听之后,Tab改变同时替换显示的Fragment.
     *
     * @param position
     */
    private void replaceShownFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mFragments[0];
                break;
            case 1:
                fragment = mFragments[1];
                break;

            case 2:
                fragment = mFragments[2];
                break;
            case 3:
                fragment = mFragments[3];
                break;
        }
        // 替换显示的Fragment.
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_container, fragment)
                    .commit();
        }
    }
}