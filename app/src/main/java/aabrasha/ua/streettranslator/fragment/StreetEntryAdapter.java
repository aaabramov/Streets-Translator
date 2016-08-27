package aabrasha.ua.streettranslator.fragment;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Andrii Abramov on 8/28/16.
 */
public class StreetEntryAdapter extends ArrayAdapter<StreetEntry> {

    public StreetEntryAdapter(Context context) {
        super(context, R.layout.list_item_street_entry);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_street_entry, null);
        }

        fillItemData(position, convertView);
        return convertView;
    }

    private void fillItemData(int position, View convertView) {
        StreetEntry item = getItem(position);

        TextView tvNewName = (TextView) convertView.findViewById(R.id.item_text_new_street_name);
        tvNewName.setText(item.getNewName());
        TextView tvOldName = (TextView) convertView.findViewById(R.id.item_text_old_street_name);
        tvOldName.setText(item.getOldName());
        TextView tvDescription = (TextView) convertView.findViewById(R.id.item_text_street_description);
        tvDescription.setText(item.getDescription());
    }
}
