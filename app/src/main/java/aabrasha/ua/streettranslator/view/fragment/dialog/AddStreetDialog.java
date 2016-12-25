package aabrasha.ua.streettranslator.view.fragment.dialog;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.model.StreetEntry;
import aabrasha.ua.streettranslator.service.StreetsService;
import aabrasha.ua.streettranslator.util.ViewUtils;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.Date;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Andrii Abramov on 9/3/16.
 */
public class AddStreetDialog extends DialogFragment {

    private static final String TAG = AddStreetDialog.class.getSimpleName();

    private Context context;

    private LinearLayout llNewNameContainer;

    private EditText etOldName;
    private EditText etNewName;
    private EditText etDescription;
    private CheckBox cbHasNewName;

    private OnDialogDismiss onDialogDismiss;


    public static AddStreetDialog newInstance(OnDialogDismiss onDialogDismiss) {
        AddStreetDialog dialog = new AddStreetDialog();
        dialog.onDialogDismiss = onDialogDismiss;
        return dialog;
    }

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
                .setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveStreet();
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
        llNewNameContainer = (LinearLayout) result.findViewById(R.id.container_street_new_name);

        etOldName = (EditText) result.findViewById(R.id.et_old_name);
        etNewName = (EditText) result.findViewById(R.id.et_new_name);
        etDescription = (EditText) result.findViewById(R.id.et_description);

        cbHasNewName = (CheckBox) result.findViewById(R.id.cb_has_new_name);
        cbHasNewName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasNewNameChecked(isChecked);
            }
        });
        return result;
    }

    private void hasNewNameChecked(boolean isChecked) {
        if (isChecked) {
            llNewNameContainer.setVisibility(View.VISIBLE);
            ViewUtils.focusWithKeyboard(etNewName);
        } else {
            llNewNameContainer.setVisibility(View.GONE);
            ViewUtils.focusWithKeyboard(etOldName);
        }
    }

    private View getDialogView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_street, null);
    }

    private void saveStreet() {
        new SaveStreetTask().execute();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDialogDismiss != null) {
            onDialogDismiss.onDismiss();
        }
    }

    private class SaveStreetTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            if (requiredDataPresents()) {
                StreetEntry added = parseStreetEntryFromFields();
                StreetsService.getInstance().addNewStreetEntry(added);
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
            String oldName = ViewUtils.getText(etOldName);
            String newName = isNewNamePresent() ? parseNewName() : null;
            String description = ViewUtils.getText(etDescription);
            Date insertionDate = new Date();
            return StreetEntry.from(oldName, newName, description, insertionDate);
        }

        private boolean isNewNamePresent() {
            return cbHasNewName.isChecked() && !TextUtils.isEmpty(ViewUtils.getText(etNewName));
        }

        private String parseNewName() {
            return ViewUtils.getText(etNewName);
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
