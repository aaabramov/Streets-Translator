package aabrasha.ua.streettranslator.view.fragment;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.material.DividerItemDecoration;
import aabrasha.ua.streettranslator.material.VerticalSpaceItemDecoration;
import aabrasha.ua.streettranslator.model.SortMethod;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
import aabrasha.ua.streettranslator.view.adapter.OnLeftSwipeCallback;
import aabrasha.ua.streettranslator.view.adapter.OnRightSwipeCallback;
import aabrasha.ua.streettranslator.view.adapter.SearchPatternProvider;
import aabrasha.ua.streettranslator.view.adapter.StreetEntryAdapter;
import aabrasha.ua.streettranslator.view.fragment.dialog.AddStreetDialog;
import aabrasha.ua.streettranslator.view.fragment.dialog.EditStreetDialog;
import aabrasha.ua.streettranslator.view.fragment.dialog.OnDialogDismiss;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class ResultsFragment extends Fragment {

    private static final String TAG = ResultsFragment.class.getSimpleName();

    private StreetsService streetsService;
    private StreetEntryAdapter itemsAdapter;

    private AsyncStreetLoadTask currentAsyncLoadingTask;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_results, container, false);

        initFab(result);
        initRecyclerView(result);

        return result;
    }

    private void initFab(View parent) {
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

    public void setSortMethod(SortMethod sortMethod) {
        itemsAdapter.setSortingMethod(sortMethod);

        String message = "Changed sorting method to: " + sortMethod.getLocalizedString(getContext());
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                openConfirmToRemoveDialog(position);
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
            currentAsyncLoadingTask.cancel(true);
        }

        currentAsyncLoadingTask = new AsyncStreetLoadTask();
        currentAsyncLoadingTask.execute(pattern);
    }

    public void refreshStreetsResults() {
        findStreets(patternProvider.getPattern());
    }

    public void setPatternProvider(SearchPatternProvider patternProvider) {
        this.patternProvider = patternProvider;
    }

    private void openConfirmToRemoveDialog(final int position) {
        StreetEntry itemToRemove = itemsAdapter.getItem(position);
        String message = getString(R.string.title_are_you_sure_to_delete, itemToRemove.getOldName());
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeStreetEntry(position);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemsAdapter.notifyItemChanged(position);
                    }
                })
                .create()
                .show();
    }

    private void removeStreetEntry(int position) {
        StreetEntry itemToRemove = itemsAdapter.getItem(position);
        new AsyncStreetRemoveTask().execute(itemToRemove);

        itemsAdapter.remove(position);
        itemsAdapter.notifyItemRemoved(position);
    }

    private void updateStreetEntry(final int position) {
        StreetEntry itemToUpdate = itemsAdapter.getItem(position);
        EditStreetDialog updateDialog = EditStreetDialog.newInstance(itemToUpdate, new OnDialogDismiss() {
            @Override
            public void onDismiss() {
                itemsAdapter.notifyItemChanged(position);
            }
        });

        updateDialog.show(getActivity().getFragmentManager(), TAG);
    }

    private boolean shouldCancelLastTask() {
        return currentAsyncLoadingTask != null && !currentAsyncLoadingTask.isCancelled();
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
            itemsAdapter.setPattern(patternProvider.getPattern());
            itemsAdapter.setItems(items);
        }
    }

    private class AsyncStreetRemoveTask extends AsyncTask<StreetEntry, Void, String> {

        @Override
        protected String doInBackground(StreetEntry... items) {
            StreetEntry itemToRemove = items[0];
            streetsService.deleteById(itemToRemove.getId());
            return itemToRemove.getOldName();
        }

        @Override
        protected void onPostExecute(String oldName) {
            String report = getString(R.string.report_street_removed, oldName);
            Toast.makeText(getActivity(), report, Toast.LENGTH_SHORT).show();
        }
    }


}
