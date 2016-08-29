package aabrasha.ua.streettranslator.activity;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.ResultsFragment;
import aabrasha.ua.streettranslator.fragment.SearchFragment;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.model.StreetsService;
import aabrasha.ua.streettranslator.util.IOUtils;
import aabrasha.ua.streettranslator.util.TextWatcherAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    private StreetsService streetsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initFragments();
        initServices();
    }

    private void initServices() {
        streetsService = new StreetsService(this);
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

        etSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                findStreets();
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
                findStreets();
            }
        });


    }

    private void findStreets() {
        String nameLike = etSearch.getText().toString();
        if (nameLike.length() == 0) {
            resultsFragment.setItems(streetsService.getAll());
            return;
        }
        List<StreetEntry> items = streetsService.getByNameLike(nameLike);
        resultsFragment.setItems(items);
    }

    private void focusSearchField() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_populate_with_default_streets:
                populateWithDefaultStreetList();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void populateWithDefaultStreetList() {
        streetsService.fillWithSampleData();
    }
}
