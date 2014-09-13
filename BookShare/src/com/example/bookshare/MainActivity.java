package com.example.bookshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Fragment fragment;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			if(position == 0)
			{
				fragment = new LibsFragment();
				Bundle args = new Bundle();
				args.putInt(LibsFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);			
			}
			else if(position == 1)
			{
				fragment = new FriendsFragment();
				Bundle args = new Bundle();
				args.putInt(FriendsFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
			}	
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			default:
				return getString(R.string.title_section1).toUpperCase(l);
			}
		}
	}

	/**
	 * A dummy fragment representing a section of the app,
	 */
	public static class LibsFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private  ListView  mlistView;
		private List<Map<String, Object>> list;
		private int libNums = 0;
		Set<String> libs;
		
		public LibsFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
//			BmobQuery<library> query = new BmobQuery<library>();
//			query.findObjects(getActivity(), new FindListener<library>() {
//				
//				@Override
//				public void onSuccess(List<library> arg0) {
//					// TODO Auto-generated method stub
//					libNums = arg0.size();
//					Log.d("OOOOOOOOOOOOOO", "arg1:" + libNums);
//					flag = true;
//					Toast.makeText(getActivity(), "查询成功："+ arg0.size(), Toast.LENGTH_SHORT).show();
//				}
//				
//				@Override
//				public void onError(int arg0, String arg1) {
//					// TODO Auto-generated method stub
//					Toast.makeText(getActivity(), "查询失败："+ arg1, Toast.LENGTH_SHORT).show();
//				}
//			});
			
			Log.d(ARG_SECTION_NUMBER, "Fragment" + Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			
				View rootView = inflater.inflate(R.layout.fragment_main_dummy,container, false);
				/* 获取之前预加载的数据 */
				SharedPreferences prefs = rootView.getContext().getSharedPreferences("libs", MODE_PRIVATE);
				libNums = prefs.getInt("num", 0);
				libs = new HashSet<String>();			
				libs = prefs.getStringSet("libs", null);
				
				mlistView = (ListView) rootView.findViewById(R.id.list_view);
				list = buildListForSimpleAdapter();
				SimpleAdapter mAdapter = new SimpleAdapter(rootView.getContext(), list, R.layout.item_row,
	        			new String[] {"book_img", "book_name", "book_desc"},
	        			new int[] {R.id.book_img, R.id.book_name, R.id.book_desc});
	        	mlistView.setAdapter(mAdapter);
			
			//dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
		
		private List<Map<String, Object>> buildListForSimpleAdapter(){
		    List<Map<String, Object>> list = new  ArrayList<Map<String,Object>>(libNums);
		    
		    for(Iterator<String> it = libs.iterator(); it.hasNext(); ){
		    	Map<String, Object> map = new HashMap<String, Object>();
		    	map.put("book_img", R.drawable.headimg);
		    	String str = it.next().toString();
		    	int index =	str.indexOf("=#@$@#+");
		    	Log.d("==MainAcitvity==", "index:" + index + str.substring(0, index));
				map.put("book_name", str.substring(0, index));
				map.put("book_desc", str.substring(index+7, str.length()));
				list.add(map);
		    }
			return list;
		}
	}

	
	public static class FriendsFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		private  ListView  mlistView;
		private List<Map<String, Object>> list;

		public FriendsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			Log.d(ARG_SECTION_NUMBER, "Fragment" + Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			
				View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
				mlistView = (ListView) rootView.findViewById(R.id.list_view);
				list = buildListForSimpleAdapter();
				SimpleAdapter mAdapter = new SimpleAdapter(rootView.getContext(), list, R.layout.item_row_friend,
	        			new String[] {"friend_img", "friend_name"},
	        			new int[] {R.id.friend_img, R.id.friend_name});
	        	mlistView.setAdapter(mAdapter);
			
			//dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
		
		private List<Map<String, Object>> buildListForSimpleAdapter(){
		    List<Map<String, Object>> list = new  ArrayList<Map<String,Object>>(10);
		    for(int i = 0; i < 10; i++){
		    	Map<String, Object> map = new HashMap<String, Object>();
		    	map.put("friend_img", R.drawable.headimg);
				map.put("friend_name", "刘德华");
					list.add(map);
		    	}
			return list;
		}
	}
}
