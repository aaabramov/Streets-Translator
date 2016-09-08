package aabrasha.ua.streettranslator.fragment;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.adapter.OnLeftSwipeCallback;
import aabrasha.ua.streettranslator.fragment.adapter.SearchPatternProvider;
import aabrasha.ua.streettranslator.fragment.adapter.StreetEntryAdapter;
import aabrasha.ua.streettranslator.fragment.adapter.OnRightSwipeCallback;
import aabrasha.ua.streettranslator.fragment.dialog.AddStreetDialog;
import aabrasha.ua.streettranslator.fragment.dialog.EditStreetDialog;
import aabrasha.ua.streettranslator.fragment.dialog.OnDialogDismiss;
import aabrasha.ua.streettranslator.material.DividerItemDecoration;
import aabrasha.ua.streettranslator.material.VerticalSpaceItemDecoration;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class ResultsFragment extends Fragment {

    private static final String TAG = ResultsFragment.class.getSimpleName();

    private StreetsService streetsService;
    private StreetEntryAdapter itemsAdapter;

    private AsyncStreetLoadTask lastAsyncLoadingTask;
    private SearchPatternProvider patternProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        initServices();
    }

    private void initServices() {
        itemsAdapter = new StreetEntryAdapter();
        streetsService = StreetsService.getInstance();
        lastAsyncLoadingTask = new AsyncStreetLoadTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_results, container, false);

        initFAB(result);
        initRecyclerView(result);

        return result;
    }

    private void initFAB(View parent) {
        parent.findViewById(R.id.fab_add_street).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddStreetDialog addStreetDialog = AddStreetDialog.newInstance(new OnDialogDismiss() {
                    @Override
                    public void onDismiss() {
                        refreshStreetsResults();
                    }
                });
                addStreetDialog.show(getActivity().getFragmentManager(), TAG);
            }
        });
    }

    private void initRecyclerView(View parent) {
        RecyclerView resultsView = (RecyclerView) parent.findViewById(R.id.search_result_list);
        resultsView.setAdapter(itemsAdapter);
        resultsView.setHasFixedSize(false);
        resultsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsView.addItemDecoration(new VerticalSpaceItemDecoration(2));
        resultsView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.list_view_divider));

        ItemTouchHelper.SimpleCallback removeOnRightSwipeCallback = new OnRightSwipeCallback() {
            @Override
            public void onRightSwipe(int position) {
                removeStreetEntry(position);
            }
        };
        ItemTouchHelper.SimpleCallback updateOnLeftSwipeCallback = new OnLeftSwipeCallback() {
            @Override
            public void onLeftSwipe(int position) {
                updateStreetEntry(position);
            }
        };
        new ItemTouchHelper(removeOnRightSwipeCallback).attachToRecyclerView(resultsView);
        new ItemTouchHelper(updateOnLeftSwipeCallback).attachToRecyclerView(resultsView);
    }

    public void findStreets(String pattern) {

        if (shouldCancelLastTask()) {
            lastAsyncLoadingTask.cancel(true);
        }

        lastAsyncLoadingTask = new AsyncStreetLoadTask();
        lastAsyncLoadingTask.execute(pattern);
    }

    public void refreshStreetsResults() {
        findStreets(patternProvider.getPattern());
    }

    public void setPatternProvider(SearchPatternProvider patternProvider) {
        this.patternProvider = patternProvider;
    }

    private void removeStreetEntry(int position) {
        StreetEntry itemToRemove = itemsAdapter.getItem(position);
        streetsService.deleteById(itemToRemove.getId());
        itemsAdapter.remove(itemToRemove);
        String report = getResources().getString(R.string.report_streets_removed);
        Toast.makeText(getActivity(), format(report, 1), Toast.LENGTH_SHORT).show();
    }

    private void updateStreetEntry(int position) {
        StreetEntry itemToUpdate = itemsAdapter.getItem(position);
        EditStreetDialog updateDialog = EditStreetDialog.newInstance(itemToUpdate, new OnDialogDismiss() {
            @Override
            public void onDismiss() {
                findStreets(patternProvider.getPattern());
            }
        });

        updateDialog.show(getActivity().getFragmentManager(), TAG);
    }

    private boolean shouldCancelLastTask() {
        return lastAsyncLoadingTask != null && !lastAsyncLoadingTask.isCancelled();
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
            itemsAdapter.setItems(items);
        }
    }


}
