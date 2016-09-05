package aabrasha.ua.streettranslator.fragment;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.adapter.StreetEntryAdapter;
import aabrasha.ua.streettranslator.fragment.adapter.OnRightSwipeCallback;
import aabrasha.ua.streettranslator.fragment.dialog.AddStreetDialog;
import aabrasha.ua.streettranslator.material.DividerItemDecoration;
import aabrasha.ua.streettranslator.material.VerticalSpaceItemDecoration;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
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

        initFAB(result);
        initRecyclerView(result);

        return result;
    }

    private void initFAB(View parent) {
        parent.findViewById(R.id.fab_add_street).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddStreetDialog().show(getActivity().getFragmentManager(), TAG);
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

        ItemTouchHelper.SimpleCallback simpleCallback = new OnRightSwipeCallback() {
            @Override
            public void onRightSwipe(int position) {
                removeStreetEntry(position);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(resultsView);
    }

    private void removeStreetEntry(int position) {
        StreetEntry itemToRemove = itemsAdapter.getItem(position);
        streetsService.deleteById(itemToRemove.getId());
        itemsAdapter.remove(itemToRemove);
        String report = getResources().getString(R.string.report_streets_removed);
        Toast.makeText(getActivity(), format(report, 1), Toast.LENGTH_SHORT).show();
    }

    public void setItems(List<StreetEntry> items) {
        itemsAdapter.setItems(items);
    }
}
