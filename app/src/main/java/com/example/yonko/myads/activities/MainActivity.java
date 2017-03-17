package com.example.yonko.myads.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.yonko.myads.R;
import com.example.yonko.myads.adapters.ViewPagerAdapter;
import com.example.yonko.myads.fragments.AdInfoFragment;
import com.example.yonko.myads.fragments.AdvertisingContainerFragment;
import com.example.yonko.myads.fragments.AdvertisingListFragment;
import com.example.yonko.myads.fragments.AdPhotosFragment;
import com.example.yonko.myads.fragments.CategoryFragment;
import com.example.yonko.myads.fragments.ContactsFragment;
import com.example.yonko.myads.gson.AdvImageSerializer;
import com.example.yonko.myads.interfaces.OnFragmentActionListener;
import com.example.yonko.myads.fragments.StateFragment;
import com.example.yonko.myads.gson.AdvertisingSerializer;
import com.example.yonko.myads.model.Advertising;
import com.example.yonko.myads.model.AdvertisingImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AdvertisingListFragment.OnListFragmentInteractionListener,
        AdvertisingContainerFragment.OnAdCreatedListener,
        OnFragmentActionListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String KEY_TAB_POSITION = "key-tab-position";
    private static final String KEY_BACK_BUTTON_ENABLED = "back-button-enabled";
    private static final String KEY_CURRENT_ADV = "current-adv";
    private static final String TAG_STATE_FRAGMENT = "tag-state-fragment";

    private static final String TAG_AD_INFO_FRAGMENT = "tag-ad-info-fragment";
    private static final String TAG_AD_PHOTOS_FRAGMENT = "tag-ad-photos-fragment";
    private static final String TAG_AD_CATEGORY_FRAGMENT = "tag-ad-category-fragment";
    private static final String TAG_AD_CONTACTS_FRAGMENT = "tag-ad-contacts-fragment";
    private static final String FRAGMENT_TAGS[] = {TAG_AD_INFO_FRAGMENT, TAG_AD_PHOTOS_FRAGMENT, TAG_AD_CATEGORY_FRAGMENT, TAG_AD_CONTACTS_FRAGMENT};

    private AdvertisingListFragment mAdvertisingListFragment;
    private AdvertisingContainerFragment mAdvertisingContainerFragment;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private Advertising mCurrentAdv;

    private boolean mIsBackButtonEnabled = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_my_ads:
                    setActionBarTitle(R.string.title_my_ads);
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_place_ad:
                    setActionBarTitle(R.string.title_ad_info);
                    mViewPager.setCurrentItem(1);
                    return true;
            }
            return true;
        }

    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setActiveTab(position);
            if (position == 0) {
                setActionBarTitle(R.string.title_my_ads);
                setBackButton(false);
            } else if (position == 1) {
                setActionBarTitle(R.string.title_place_ad);
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0 && !manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName().equals(TAG_AD_INFO_FRAGMENT)) {
                    setBackButton(true);
                }
                handleContentFragment();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setupViewPager(mViewPager);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_TAB_POSITION)) {
                navigateToTab(savedInstanceState.getInt(KEY_TAB_POSITION));
            }
            mIsBackButtonEnabled = savedInstanceState.getBoolean(KEY_BACK_BUTTON_ENABLED, false);
        } else {
            navigateToTab(0);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_ADV)) {
            mCurrentAdv = savedInstanceState.getParcelable(KEY_CURRENT_ADV);
        } else {
            mCurrentAdv = new Advertising();
        }
    }

    @Override
    public void onAdCreated(Advertising ad) {
        if (mAdvertisingListFragment != null) {
            mAdvertisingListFragment.addItem(ad);
        }

        navigateToTab(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_TAB_POSITION, mViewPager.getCurrentItem());
        outState.putBoolean(KEY_BACK_BUTTON_ENABLED, mIsBackButtonEnabled);
        outState.putParcelable(KEY_CURRENT_ADV, mCurrentAdv);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemSelected(Advertising item) {

    }

    @Override
    public void onFragmentAction(Bundle bundle, String sender) {
        if (sender.equals(AdInfoFragment.SENDER_ID)) {
            View container = findViewById(R.id.fragment_container);
            if (container != null) {
                replaceFragment(container.getId(), AdPhotosFragment.newInstance(), TAG_AD_PHOTOS_FRAGMENT);
                setBackButton(true);
            }
            mCurrentAdv.setTitle(bundle.getString(AdInfoFragment.EXTRA_TITLE, getString(R.string.not_specified)));
            mCurrentAdv.setDescription(bundle.getString(AdInfoFragment.EXTRA_DESCRIPTION, getString(R.string.not_specified)));
            mCurrentAdv.setPrice(bundle.getFloat(AdInfoFragment.EXTRA_PRICE, 0.0f));

        } else if (sender.equals(AdPhotosFragment.SENDER_ID)) {
            View container = findViewById(R.id.fragment_container);
            if (container != null) {
                replaceFragment(container.getId(), CategoryFragment.newInstance(), TAG_AD_CATEGORY_FRAGMENT);
                setBackButton(true);
            }
            mCurrentAdv.setImages((ArrayList)bundle.getParcelableArrayList(AdPhotosFragment.EXTRA_LIST_PHOTOS));

        } else if (sender.equals(CategoryFragment.SENDER_ID)) {

            View container = findViewById(R.id.fragment_container);
            if (container != null) {
                replaceFragment(container.getId(), ContactsFragment.newInstance(), TAG_AD_CONTACTS_FRAGMENT);
                setBackButton(true);
            }

            mCurrentAdv.setCategory(bundle.getString(CategoryFragment.EXTRA_CATEGORY, getString(R.string.not_specified)));
            mCurrentAdv.setSubcategory(bundle.getString(CategoryFragment.EXTRA_SUBCATEGORY, getString(R.string.not_specified)));

        } else if (sender.equals(ContactsFragment.SENDER_ID)) {
            mCurrentAdv.setPhone(bundle.getString(ContactsFragment.EXTRA_PHONE, getString(R.string.not_specified)));
            mCurrentAdv.setShouldFacebookShare(bundle.getBoolean(ContactsFragment.EXTRA_FACEBOOK, false));
            mCurrentAdv.setShouldShowLocation(bundle.getBoolean(ContactsFragment.EXTRA_LOCATION, false));
            getSupportFragmentManager().popBackStackImmediate(TAG_AD_PHOTOS_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            setBackButton(false);
            navigateToTab(0);
            mAdvertisingListFragment.addItem(mCurrentAdv);
        }
    }

    @Override
    public void onListSavePressed(List<Advertising> items) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Advertising.class, new AdvertisingSerializer());
        gsonBuilder.registerTypeAdapter(AdvertisingImage.class, new AdvImageSerializer());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        final String json = gson.toJson(items);
        Log.d(LOG_TAG, json);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mIsBackButtonEnabled && fragmentManager.getBackStackEntryCount() > 0) {
            if (fragmentManager.getBackStackEntryCount() == 1) {
                setBackButton(false);
            }
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackButton(boolean isVisible) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);
            getSupportActionBar().setDisplayShowHomeEnabled(isVisible);
            mIsBackButtonEnabled = isVisible;
        }
    }

    private void replaceFragment(int container, Fragment fragment, String tag) {
        getSupportFragmentManager().
                beginTransaction().
                replace(container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private void navigateToTab(int position) {
        if (position == 0 || position == 1) {
            setActiveTab(position);
            mViewPager.setCurrentItem(position);
        }
    }

    private void setActiveTab(int position) {
        if (position == 0) {
            mBottomNavigationView.getMenu().getItem(1).setChecked(false);
            mBottomNavigationView.getMenu().getItem(0).setChecked(true);
        } else if (position == 1) {
            mBottomNavigationView.getMenu().getItem(0).setChecked(false);
            mBottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
    }

    private void setActionBarTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

    private void setActionBarTitle(int resTitle) {
        setActionBarTitle(getResources().getString(resTitle));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        StateFragment stateFragment = (StateFragment) getSupportFragmentManager().findFragmentByTag(TAG_STATE_FRAGMENT);

        if (stateFragment == null) {
            stateFragment = new StateFragment();
            getSupportFragmentManager().beginTransaction().add(stateFragment, TAG_STATE_FRAGMENT).commit();
        }

        if (stateFragment.getAdvertisingListFragment() == null) {
            mAdvertisingListFragment = AdvertisingListFragment.newInstance(1);
            stateFragment.setAdvertisingListFragment(mAdvertisingListFragment);
        }
        adapter.addFragment(mAdvertisingListFragment);

        if (stateFragment.getAdvertisingContainerFragment() == null) {
            mAdvertisingContainerFragment = AdvertisingContainerFragment.newInstance("1");
            stateFragment.setAdvertisingContainerFragment(mAdvertisingContainerFragment);
        }
        adapter.addFragment(mAdvertisingContainerFragment);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    private Fragment getActiveAdFragment() {
        Fragment fragment = null;
        for (String tag : FRAGMENT_TAGS) {
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment != null) break;
        }
        return fragment;
    }

    private void handleContentFragment() {
        Log.d(LOG_TAG, "begin");
        Fragment fragment = getActiveAdFragment();
        if (fragment == null) {
            View container = findViewById(R.id.fragment_container);
            if (container != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(container.getId(), new AdInfoFragment(), TAG_AD_INFO_FRAGMENT)
                        .commit();

                Log.d(LOG_TAG, "new fragment");
            }
        }
    }
}
