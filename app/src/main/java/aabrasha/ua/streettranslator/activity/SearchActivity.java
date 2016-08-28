package aabrasha.ua.streettranslator.activity;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.ResultsFragment;
import aabrasha.ua.streettranslator.fragment.SearchFragment;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.sqlite.StreetsOpenHelper;
import aabrasha.ua.streettranslator.util.IOUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class SearchActivity extends AppCompatActivity {

    public static final String TAG = SearchActivity.class.getSimpleName();

    private EditText etSearch;

    private FragmentManager fragmentManager;
    private ResultsFragment resultsFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initFragments();
    }

    private void initFragments() {
        fragmentManager = getSupportFragmentManager();

        resultsFragment = new ResultsFragment();

        searchFragment = new SearchFragment();
        setSearchFragment();
        setResultsFragment();
    }

    private void setSearchFragment() {
        searchFragment = (SearchFragment) initFragment(R.id.fragment_search, searchFragment);
    }

    private void setResultsFragment() {
        resultsFragment = (ResultsFragment) initFragment(R.id.fragment_results, resultsFragment);
    }

    private Fragment initFragment(int fragmentId, Fragment fragmentToUse) {
        Fragment destination = fragmentManager.findFragmentById(fragmentId);
        if (destination == null) {
            fragmentManager.beginTransaction()
                    .add(fragmentId, fragmentToUse)
                    .commit();
            return fragmentToUse;
        }
        return destination;
    }

    private void initViews() {
        etSearch = (EditText) findViewById(R.id.et_search);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                findStreets();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                // TODO use android.os.AsyncTask
                // with cancelling it
                findStreets();
            }
        });


    }

    private void findStreets() {

        String nameLike = etSearch.getText().toString();
        if (nameLike.length() == 0) {
            // TODO
            resultsFragment.setItems(new StreetsOpenHelper(this).getAll());
            return;
        }
        List<StreetEntry> items = new StreetsOpenHelper(this).getByNameLike(nameLike);
        resultsFragment.setItems(items);

    }

    private void focusSearchField() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }
}
