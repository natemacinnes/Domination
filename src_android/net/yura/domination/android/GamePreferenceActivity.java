package net.yura.domination.android;

import java.util.Arrays;
import java.util.ResourceBundle;

import net.yura.domination.engine.ai.AIManager;
import net.yura.domination.engine.translation.TranslationBundle;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.view.View;

public class GamePreferenceActivity extends PreferenceActivity {

    private static ResourceBundle resb = TranslationBundle.getBundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            setPreferenceScreen( makePreferenceScreen(getPreferenceManager(),this) );
        }
        else {
            // hack to get rid of strange square on honeycomb
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ((View)getListView().getParent()).setBackgroundDrawable(null);
            }
            getFragmentManager().beginTransaction().replace(android.R.id.content, new GamePreferenceFragment()).commit();
        }
    }

    private static PreferenceScreen makePreferenceScreen(PreferenceManager man,Context context) {
        PreferenceScreen root = man.createPreferenceScreen(context);

        PreferenceCategory inlinePrefCat = new PreferenceCategory(context);
        inlinePrefCat.setTitle( resb.getString("swing.menu.options") );
        root.addPreference(inlinePrefCat);

        CheckBoxPreference show_toasts = new CheckBoxPreference(context); // TwoStatePreference = new SwitchPreference(this);
        show_toasts.setTitle( resb.getString("game.menu.showtoasts") );
        show_toasts.setKey("show_toasts");
        inlinePrefCat.addPreference(show_toasts);

        CheckBoxPreference color_blind = new CheckBoxPreference(context); // TwoStatePreference = new SwitchPreference(this);
        color_blind.setTitle( resb.getString("game.menu.colorblind") );
        color_blind.setKey("color_blind");
        inlinePrefCat.addPreference(color_blind);

        ListPreference ai = new IntListPreference(context);
        ai.setTitle( resb.getString("game.menu.aiSpeed") );
        ai.setKey("ai_wait");
        final String[] aiSpeeds = new String[] {
                resb.getString("game.menu.aiSpeed.normal"),
                resb.getString("game.menu.aiSpeed.fast"),
                resb.getString("game.menu.aiSpeed.lightning"),
                resb.getString("game.menu.aiSpeed.instant")};
        final String[] aiSpeedsValues = new String[] {"500","300","100","0"};
        ai.setEntries(aiSpeeds);
        ai.setEntryValues(aiSpeedsValues);
        ai.setDefaultValue(aiSpeedsValues[0]);
        ai.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                AIManager.setWait(Integer.parseInt(String.valueOf(newValue)));
                return true;
            }
        });
        inlinePrefCat.addPreference(ai);

        return root;
    }

    /*
    // Called only on Honeycomb and later
    @Override
    public void onBuildHeaders(List<Header> target) {
        Header header = new Header();
        header.title = "header title";
        header.fragment = GamePreferenceFragment.class.getName();
        target.add(header);
    }
    */

    public static class GamePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setPreferenceScreen( makePreferenceScreen(getPreferenceManager(),getActivity()) );
        }
    }


    public static class IntListPreference extends ListPreference {

        public IntListPreference(Context context) {
            super(context);
        }
        public IntListPreference(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean persistString(String value) {
            if(value == null) {
                return false;
            } else {
                return persistInt(Integer.valueOf(value));
            }
        }

        @Override
        protected String getPersistedString(String defaultReturnValue) {
            if(getSharedPreferences().contains(getKey())) {
                int intValue = getPersistedInt(0);
                return String.valueOf(intValue);
            } else {
                return defaultReturnValue;
            }
        }
    }

}
