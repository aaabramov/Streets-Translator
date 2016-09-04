package aabrasha.ua.streettranslator.fragment.adapter;

import aabrasha.ua.streettranslator.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class StreetEntryViewHolder extends RecyclerView.ViewHolder {

    private TextView tvOldName;
    private TextView tvNewName;
    private TextView tvDescription;

    public StreetEntryViewHolder(View container) {
        super(container);
        findViews(container);
    }

    public void setOldName(String oldName) {
        tvOldName.setText(oldName);
    }

    public void setNewName(String newName) {
        tvNewName.setText(newName);
    }

    public void setDescription(String description) {
        if (hasDescription(description)) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(description);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
    }

    private boolean hasDescription(String description){
        return description != null && !description.isEmpty();
    }

    private void findViews(View container) {
        tvNewName = (TextView) container.findViewById(R.id.item_text_new_street_name);
        tvOldName = (TextView) container.findViewById(R.id.item_text_old_street_name);
        tvDescription = (TextView) container.findViewById(R.id.item_text_street_description);
    }
}