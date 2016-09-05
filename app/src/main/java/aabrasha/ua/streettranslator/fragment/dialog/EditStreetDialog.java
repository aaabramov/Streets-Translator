package aabrasha.ua.streettranslator.fragment.dialog;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Andrii Abramov on 9/3/16.
 */
public class EditStreetDialog extends DialogFragment {

    private static final String TAG = EditStreetDialog.class.getSimpleName();
    private static final String KEY_ITEM_TO_UPDATE = EditStreetDialog.class.getSimpleName();

    private Context context;

    private EditText etOldName;
    private EditText etNewName;
    private EditText etDescription;

    private OnDialogDismiss onDialogDismiss;
    private StreetEntry itemToUpdate;

    public static EditStreetDialog newInstance(StreetEntry itemToUpdate) {
        return newInstance(itemToUpdate, null);
    }

    public static EditStreetDialog newInstance(StreetEntry itemToUpdate, OnDialogDismiss onDialogDismiss) {
        EditStreetDialog dialog = new EditStreetDialog();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ITEM_TO_UPDATE, itemToUpdate);
        dialog.setArguments(args);
        dialog.onDialogDismiss = onDialogDismiss;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        parseArgs();
    }

    private void parseArgs() {

        Bundle args = getArguments();
        if (args != null) {
            itemToUpdate = (StreetEntry) args.getSerializable(KEY_ITEM_TO_UPDATE);
        } else {
            throw new RuntimeException("You have to provide item to update.");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogContent = initView();
        return new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(R.string.title_edit_street)
                .setView(dialogContent)
                .setPositiveButton(R.string.btn_update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateStreet(dialogInterface);
                    }
                }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDialogDismiss != null) {
            onDialogDismiss.onDismiss();
        }
    }

    private View initView() {
        View result = getDialogView();

        etOldName = (EditText) result.findViewById(R.id.et_old_name);
        etNewName = (EditText) result.findViewById(R.id.et_new_name);
        etDescription = (EditText) result.findViewById(R.id.et_description);

        fillData();

        return result;
    }

    private void fillData() {
        etOldName.setText(itemToUpdate.getOldName());
        etNewName.setText(itemToUpdate.getNewName());
        etDescription.setText(itemToUpdate.getDescription());
    }

    private View getDialogView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_street_details, null);
    }

    private void updateStreet(DialogInterface dialogInterface) {
        new UpdateStreetTask().execute(dialogInterface);
    }

    private class UpdateStreetTask extends AsyncTask<DialogInterface, Void, Boolean> {

        @Override
        protected Boolean doInBackground(DialogInterface... dialogs) {
            DialogInterface dialog = dialogs[0];

            if (requiredDataPresents()) {
                StreetEntry updated = parseStreetEntryFromFields();
                StreetsService.getInstance().updateStreetEntry(updated);
                dialog.dismiss();
                return true;
            } else {
                Log.d(TAG, "update: Not all required fields were filled in");
                return false;
            }
        }

        private boolean requiredDataPresents() {
            return !isEmpty(etOldName.getText()) || !isEmpty(etNewName.getText());
        }

        private StreetEntry parseStreetEntryFromFields() {
            String oldName = etOldName.getText().toString();
            String newName = etNewName.getText().toString();
            String description = etDescription.getText().toString();
            return StreetEntry.from(oldName, newName, description);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(context, R.string.title_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.hint_invalid_street_fields, Toast.LENGTH_LONG).show();
            }
        }
    }

}
