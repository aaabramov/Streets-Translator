package aabrasha.ua.streettranslator.fragment.dialog;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
import android.app.Dialog;
import android.app.DialogFragment;
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
public class AddStreetDialog extends DialogFragment {

    private static final String TAG = AddStreetDialog.class.getSimpleName();

    private EditText etOldName;
    private EditText etNewName;
    private EditText etDescription;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogContent = initView();
        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.title_add_new_street)
                .setView(dialogContent)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveStreet(dialogInterface);
                    }
                }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    private View initView() {
        View result = getDialogView();
        etOldName = (EditText) result.findViewById(R.id.et_old_name);
        etNewName = (EditText) result.findViewById(R.id.et_new_name);
        etDescription = (EditText) result.findViewById(R.id.et_description);
        return result;
    }

    private View getDialogView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_street, null);
    }

    private void saveStreet(DialogInterface dialogInterface) {
        new SaveStreetTask().execute(dialogInterface);
    }

    private class SaveStreetTask extends AsyncTask<DialogInterface, Void, Void> {

        @Override
        protected Void doInBackground(DialogInterface... dialogs) {
            DialogInterface dialog = dialogs[0];

            if (requiredDataPresents()) {
                StreetEntry added = parseStreetEntryFromFields();
                StreetsService.getInstance().addNewStreetEntry(added);
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Specify at least `Old name` or `New name`", Toast.LENGTH_LONG).show();
                Log.d(TAG, "saveStreet: Not all required fields were filled in");
            }
            return null;
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
    }

}
