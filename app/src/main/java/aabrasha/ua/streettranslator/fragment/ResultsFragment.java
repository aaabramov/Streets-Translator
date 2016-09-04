package aabrasha.ua.streettranslator.fragment;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.adapter.StreetEntryAdapter;
import aabrasha.ua.streettranslator.fragment.dialog.AddStreetDialog;
import aabrasha.ua.streettranslator.material.DividerItemDecoration;
import aabrasha.ua.streettranslator.material.VerticalSpaceItemDecoration;
import aabrasha.ua.streettranslator.model.StreetEntry;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
 */
public class ResultsFragment extends Fragment {

    private static final String TAG = ResultsFragment.class.getSimpleName();

    private RecyclerView resultsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_results, container, false);

        initFAB(result);
        initRecyclerView(result);

        return result;
    }

    private void initRecyclerView(View parent) {
        resultsView = (RecyclerView) parent.findViewById(R.id.search_result_list);
        resultsView.setHasFixedSize(true);
        resultsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultsView.setAdapter(new StreetEntryAdapter());
        resultsView.addItemDecoration(new VerticalSpaceItemDecoration(2));
        resultsView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.list_view_divider));
    }

    private void initFAB(View parent) {
        parent.findViewById(R.id.fab_add_street).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddStreetDialog().show(getActivity().getFragmentManager(), TAG);
            }
        });
    }

    public void setItems(List<StreetEntry> items) {
        StreetEntryAdapter adapter = (StreetEntryAdapter) resultsView.getAdapter();
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}
