package aabrasha.ua.streettranslator.view.adapter;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.StreetsApplication;
import aabrasha.ua.streettranslator.model.SortMethod;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.util.StreetsSorters;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

/**
 * @author Andrii Abramov on 8/28/16.
 */
public class StreetEntryAdapter extends AccessibleRecyclerViewAdapter<StreetEntry, StreetEntryViewHolder> {

    private List<StreetEntry> items = new ArrayList<>();
    private String pattern;
    private Comparator<StreetEntry> streetComparator;

    public StreetEntryAdapter() {
        SortMethod currentSortingMethod = StreetsApplication.getApplication().getSortingManager().getCurrentSortingMethod();
        streetComparator = StreetsSorters.getStreetEntryComparator(currentSortingMethod);
    }

    @Override
    public StreetEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemHolder = getItemHolder(parent);
        return new StreetEntryViewHolder(itemHolder);
    }

    private View getItemHolder(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_street_entry, parent, false);
    }

    @Override
    public void onBindViewHolder(StreetEntryViewHolder holder, int position) {
        holder.setPattern(pattern);
        holder.setStreetItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setSortingMethod(SortMethod streetComparator) {
        this.streetComparator = StreetsSorters.getStreetEntryComparator(streetComparator);
        sortItems();
    }

    public void setItems(Collection<StreetEntry> newEntries) {
        this.items = new ArrayList<>(newEntries);
        sortItems();
    }

    @SuppressWarnings("unchecked")
    private void sortItems() {
        new StreetsSorterTask().execute(streetComparator);
    }

    @Override
    public StreetEntry getItem(int position) {
        return items.get(position);
    }

    @Override
    public void remove(int position) {
        items.remove(position);
        notifyItemChanged(position);
    }

    @Override
    public void remove(StreetEntry item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemChanged(position);
    }

    private class StreetsSorterTask extends AsyncTask<Comparator<StreetEntry>, Void, Void> {

        @Override
        @SafeVarargs
        protected final Void doInBackground(Comparator<StreetEntry>... params) {
            Collections.sort(items, params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            notifyDataSetChanged();
        }
    }

}