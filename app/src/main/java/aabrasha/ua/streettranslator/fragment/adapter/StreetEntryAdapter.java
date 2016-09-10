package aabrasha.ua.streettranslator.fragment.adapter;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Andrii Abramov on 8/28/16.
 */
public class StreetEntryAdapter extends AccessibleRecyclerViewAdapter<StreetEntry, StreetEntryViewHolder> {

    private List<StreetEntry> items = new ArrayList<>();
    private String pattern;

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

    public void setItems(Collection<StreetEntry> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
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
}
