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
public class AddStreetDialog extends DialogFragment {

    private static final String TAG = AddStreetDialog.class.getSimpleName();

    private Context context;

    private EditText etOldName;
    private EditText etNewName;
    private EditText etDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogContent = initView();
        return new AlertDialog.Builder(context)
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

    private class SaveStreetTask extends AsyncTask<DialogInterface, Void, Boolean> {

        @Override
        protected Boolean doInBackground(DialogInterface... dialogs) {
            DialogInterface dialog = dialogs[0];

            if (requiredDataPresents()) {
                StreetEntry added = parseStreetEntryFromFields();
                StreetsService.getInstance().addNewStreetEntry(added);
                dialog.dismiss();
                return true;
            } else {
                Log.d(TAG, "saveStreet: Not all required fields were filled in");
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
