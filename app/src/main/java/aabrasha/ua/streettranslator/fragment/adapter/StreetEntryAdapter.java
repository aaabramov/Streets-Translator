package aabrasha.ua.streettranslator.fragment.adapter;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrii Abramov on 8/28/16.
 */
public class StreetEntryAdapter extends RecyclerView.Adapter<StreetEntryViewHolder> {

    private List<StreetEntry> items = new ArrayList<>();

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
        StreetEntry item = items.get(position);
        holder.setOldName(item.getOldName());
        holder.setNewName(item.getNewName());
        holder.setDescription(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<StreetEntry> items) {
        this.items = items;
        notifyDataSetChanged();
    }


}
