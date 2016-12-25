package aabrasha.ua.streettranslator.view.fragment.dialog;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.StreetsApplication;
import aabrasha.ua.streettranslator.model.SortMethod;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * @author Andrii Abramov on 12/25/16.
 */

public class SelectSortMethodDialog extends DialogFragment {

    private OnSortMethodChangedHandler onSortMethodChangedHandler;
    private SortMethod newSortMethod;

    public interface OnSortMethodChangedHandler {
        void onSortMethodChanged(SortMethod newSortMethod);
    }

    public static SelectSortMethodDialog newInstance(OnSortMethodChangedHandler handler) {

        SelectSortMethodDialog result = new SelectSortMethodDialog();
        result.onSortMethodChangedHandler = handler;

        return result;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        CharSequence[] items = getListItems();

        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.title_select_sort_method)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemIndex) {
                        changeSortMethodOrder(itemIndex);
                    }
                }).create();
    }

    private CharSequence[] getListItems() {
        SortMethod[] values = SortMethod.values();
        CharSequence[] items = new CharSequence[3];
        for (int i = 0; i < values.length; i++) {
            items[i] = values[i].getLocalizedString(getContext());
        }
        return items;
    }

    private void changeSortMethodOrder(int position) {
        SortMethod selectedItem = SortMethod.values()[position];
        applySortChanges(selectedItem);
    }

    private void applySortChanges(SortMethod selectedItem) {
        this.newSortMethod = selectedItem;
        StreetsApplication application = StreetsApplication.getApplication();
        application.getSortingManager()
                .setCurrentSortingMethod(selectedItem);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (onSortMethodChangedHandler != null) {
            onSortMethodChangedHandler.onSortMethodChanged(newSortMethod);
        }

    }
}
