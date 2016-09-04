package aabrasha.ua.streettranslator.fragment;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.fragment.adapter.StreetEntryAdapter;
import aabrasha.ua.streettranslator.fragment.dialog.AddStreetDialog;
import aabrasha.ua.streettranslator.model.StreetEntry;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Andrii Abramov on 8/27/16.
*/
public class ResultsFragment extends ListFragment {

    private static final String TAG = ResultsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        StreetEntryAdapter adapter = new StreetEntryAdapter(getContext());
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_results, container, false);
        result.findViewById(R.id.fab_add_street).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddStreetDialog().show(getActivity().getFragmentManager(), TAG);
            }
        });
        return result;
    }

    public void setItems(List<StreetEntry> items) {
        final StreetEntryAdapter adapter = (StreetEntryAdapter) getListAdapter();
        adapter.clear();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }
}
