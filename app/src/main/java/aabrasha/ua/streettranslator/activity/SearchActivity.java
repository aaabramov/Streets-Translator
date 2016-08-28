package aabrasha.ua.streettranslator.activity;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.ResultsFragment;
import aabrasha.ua.streettranslator.fragment.SearchFragment;
import aabrasha.ua.streettranslator.util.IOUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class SearchActivity extends AppCompatActivity {

    EditText etSearch;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initFragments();
    }

    private void initFragments() {
        fragmentManager = getSupportFragmentManager();
        setSearchFragment();
        setResultsFragment();
    }

    private void setResultsFragment() {
        initFragment(R.id.fragment_results, new ResultsFragment());
    }

    private void setSearchFragment() {
        initFragment(R.id.fragment_search, new SearchFragment());
    }

    private void initFragment(int fragmentId, Fragment fragmentToUse) {
        Fragment destination = fragmentManager.findFragmentById(fragmentId);
        if (destination == null) {
            fragmentManager.beginTransaction()
                    .add(fragmentId, fragmentToUse)
                    .commit();
        }
    }

    private void initViews() {
        etSearch = (EditText) findViewById(R.id.et_search);
        findViewById(R.id.btn_clear_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText(IOUtils.EMPTY_STRING);
                focusSearchField();
            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResultsFragment();
            }
        });

    }

    private void focusSearchField() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }
}
