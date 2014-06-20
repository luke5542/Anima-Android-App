package com.intrivix.animaapp;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AnimaHome extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String SHIVTR_URL_BASE = "http://animatravelers.shivtr.com/";
    public static final String SHIVTR_FORUMS = SHIVTR_URL_BASE + "forums";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        new HttpRequestTask(new HttpRequestTask.Handler() {
            @Override
            public void onComplete(String result) {
                /*
                 * {
                    "forum_sections": [
                        {
                            "id": 123063,
                            "name": "Anima Games",
                            "forums": [
                                {
                                    "id": 621512,
                                    "name": "Somnia ex Gaia",
                                    "description": "GM Void, In Progress"
                                },
                                {
                                    "id": 1091513,
                                    "name": "The Awesome Adventure of Legendary Heroes who (Might) Save the World and Stuff",
                                    "description": "GMs Ephax and Shaman, awaiting character submissions"
                                },
                                {
                                    "id": 963688,
                                    "name": "Blood and Intrigue",
                                    "description": "GM Void, Preparing"
                                },
                                {
                                    "id": 1021398,
                                    "name": "mkdir linked",
                                    "description": "GM shaman, preparing"
                                },
                                {
                                    "id": 960560,
                                    "name": "Shadows and Strings",
                                    "description": "GM Ephax, on hold until I can afford the setup time"
                                }
                            ]
                        }
                    ]
                }
                 */

                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray forumSections = object.getJSONArray("forum_sections");

                    for(int i=0; i<forumSections.length(); i++) {
                        JSONObject section = forumSections.getJSONObject(i);
                        JSONArray forums = section.getJSONArray("forums");

                        for(int j=0; j<forums.length(); j++) {
                            JSONObject forum = forums.getJSONObject(j);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //TODO set the drawer list with valid data...
                mNavigationDrawerFragment.setDrawerList(null);
            }
        }, SHIVTR_FORUMS, null, HttpRequestTask.GET);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
            default:
                fragmentManager.beginTransaction()
                    .replace(R.id.container, ForumFragment.newInstance(position + 1))
                    .commit();
                break;

        }
    }

    public void onSectionAttached(int number) {
        mTitle = mNavigationDrawerFragment.getDrawerList().get(number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.anima_home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Spinner mSkillType;
        private Button btn;
        private EditText mSkillValue, mRollValue, mColorValue, mMessage;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        public void copyToClipboard(String textToCopy, Context c) {
            ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Anima Stuff", textToCopy);
            clipboard.setPrimaryClip(clip);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_anima_home, container, false);
            mSkillValue = (EditText) rootView.findViewById(R.id.et_skill_value);
            mRollValue = (EditText) rootView.findViewById(R.id.et_roll_value);
            mColorValue = (EditText) rootView.findViewById(R.id.et_color_value);
            mMessage = (EditText) rootView.findViewById(R.id.et_extraneous);

            mSkillType = (Spinner) rootView.findViewById(R.id.spinner);
            mSkillType.setAdapter(ArrayAdapter.createFromResource(this.getActivity(), R.array.skills,
                    android.R.layout.simple_list_item_1));


            btn = (Button) rootView.findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mFinalRollNum = Integer.parseInt(mRollValue.getText().toString()) +
                            Integer.parseInt(mSkillValue.getText().toString());
                    String mFinalRoll = mMessage.getText().toString() + "\n[u]" + mSkillType.getSelectedItem().toString() + "[/u]\nRoll: "
                            + mRollValue.getText().toString() + "\nSkill value: [color=#" + mColorValue.getText().toString()
                            + "]" + mSkillValue.getText().toString() + "[/color]\nFinal roll: [b]" + Integer.toString(mFinalRollNum)
                            + "[/b]";
                    copyToClipboard(mFinalRoll, getActivity());
                    //Use this when you want to show a small message to the user. idk why it's called Toast.
                    Toast.makeText(getActivity(), "Copied to clipboard:\n" + mFinalRoll, Toast.LENGTH_SHORT).show();

                }
            });

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AnimaHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ForumFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Spinner mSkillType;
        private Button btn;
        private EditText mSkillValue, mRollValue, mColorValue, mMessage;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ForumFragment newInstance(int sectionNumber) {
            ForumFragment fragment = new ForumFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ForumFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_anima_home, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AnimaHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}
