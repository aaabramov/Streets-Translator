package aabrasha.ua.streettranslator.activity;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.ResultsFragment;
import aabrasha.ua.streettranslator.fragment.SearchFragment;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
import aabrasha.ua.streettranslator.util.IOUtils;
import aabrasha.ua.streettranslator.util.TextWatcherAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private EditText etSearch;

    private FragmentManager fragmentManager;
    private ResultsFragment resultsFragment;
    private SearchFragment searchFragment;
    private StreetsService streetsService;
    private AsyncStreetLoadTask lastAsyncLoadingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initFragments();
        initServices();

        findAllStreets();
    }

    private void initServices() {
        streetsService = StreetsService.getInstance();
        lastAsyncLoadingTask = new AsyncStreetLoadTask();
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
                findStreets(charSequence.toString());
            }
        });

        findViewById(R.id.btn_clear_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText(IOUtils.EMPTY_STRING);
                focusSearchField();
            }
        });
    }

    private void findStreets(String pattern) {
        String nameLike = pattern.trim();

        if (shouldCancelLastTask()) {
            lastAsyncLoadingTask.cancel(true);
        }

        lastAsyncLoadingTask = new AsyncStreetLoadTask();
        lastAsyncLoadingTask.execute(nameLike);
    }

    private void findAllStreets() {
        findStreets("");
    }

    private boolean shouldCancelLastTask() {
        return lastAsyncLoadingTask != null && !lastAsyncLoadingTask.isCancelled();
    }

    private void focusSearchField() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        focusSearchField();
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
                confirmPopulateWithDefaultDialog();
                break;
            case R.id.menu_item_clear_streets_database:
                confirmClearDatabaseDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void confirmPopulateWithDefaultDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Add streets")
                .setMessage("Are you sure to populate database with default streets? This will erase all previous streets!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        populateWithDefaultStreetList();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

    }

    private void confirmClearDatabaseDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Deleting streets")
                .setMessage("Are you sure to delete all streets?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearStreetsDatabase();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

    }

    private void populateWithDefaultStreetList() {
        clearStreetsDatabase();
        int numOfAddedRows = streetsService.fillWithSampleData();
        Toast.makeText(SearchActivity.this, "Added " + numOfAddedRows + " streets", Toast.LENGTH_SHORT).show();
        findAllStreets();
    }

    private void clearStreetsDatabase() {
        int numOfDeletedRows = streetsService.cleanDatabase();
        Toast.makeText(SearchActivity.this, "Deleted " + numOfDeletedRows + " streets", Toast.LENGTH_SHORT).show();
        findAllStreets();
    }

    private class AsyncStreetLoadTask extends AsyncTask<String, Void, List<StreetEntry>> {

        @Override
        protected List<StreetEntry> doInBackground(String... strings) {
            String nameLike = strings[0];

            List<StreetEntry> result;

            if (nameLike.length() == 0) {
                result = streetsService.getAll();
            } else {
                result = streetsService.getByNameLike(nameLike);
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<StreetEntry> items) {
            resultsFragment.setItems(items);
        }
    }

}
