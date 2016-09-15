package aabrasha.ua.streettranslator.fragment.dialog;

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

import static aabrasha.ua.streettranslator.util.StringUtils.EMPTY_STRING;
import static android.text.TextUtils.isEmpty;

/**
 * Created by Andrii Abramov on 9/3/16.
 */
public class EditStreetDialog extends DialogFragment {

    private static final String TAG = EditStreetDialog.class.getSimpleName();

    private Context context;

    private LinearLayout llNewNameContainer;

    private EditText etOldName;
    private EditText etNewName;
    private EditText etDescription;
    private CheckBox cbHasNewName;

    private OnDialogDismiss onDialogDismiss;
    private StreetEntry itemToUpdate;

    public static EditStreetDialog newInstance(StreetEntry itemToUpdate, OnDialogDismiss onDialogDismiss) {
        EditStreetDialog dialog = new EditStreetDialog();
        dialog.setItemToUpdate(itemToUpdate);
        dialog.onDialogDismiss = onDialogDismiss;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
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

        fillData();

        return result;
    }

    private void hasNewNameChecked(boolean isChecked) {
        if (isChecked) {
            llNewNameContainer.setVisibility(View.VISIBLE);
        } else {
            llNewNameContainer.setVisibility(View.GONE);
        }
    }

    private void fillData() {
        etOldName.setText(itemToUpdate.getOldName());

        String newName = itemToUpdate.getNewName();
        fillInNewName(newName);

        etDescription.setText(itemToUpdate.getDescription());
    }

    private void fillInNewName(String newName) {
        if (newName != null) {
            etNewName.setText(newName);
            cbHasNewName.setChecked(true);
            llNewNameContainer.setVisibility(View.VISIBLE);
        } else {
            etNewName.setText(EMPTY_STRING);
            cbHasNewName.setChecked(false);
            llNewNameContainer.setVisibility(View.GONE);
        }
    }

    public void setItemToUpdate(StreetEntry itemToUpdate) {
        this.itemToUpdate = itemToUpdate;
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
                        updateStreet();
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


    private View getDialogView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_street, null);
    }

    private void updateStreet() {
        new UpdateStreetTask().execute();
    }

    private class UpdateStreetTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            if (requiredDataPresents()) {
                updateItem();
                return true;
            } else {
                Log.d(TAG, "update: Not all required fields were filled in");
                return false;
            }
        }

        private boolean requiredDataPresents() {
            return !isEmpty(etOldName.getText()) || !isEmpty(etNewName.getText());
        }

        private void updateItem() {
            String oldName = ViewUtils.getText(etOldName);
            itemToUpdate.setOldName(oldName);

            String newName = isNewNamePresent() ? ViewUtils.getText(etNewName) : null;
            itemToUpdate.setNewName(newName);

            String description = ViewUtils.getText(etDescription);
            itemToUpdate.setDescription(description);

            StreetsService.getInstance().updateStreetEntry(itemToUpdate);
        }

        private boolean isNewNamePresent() {
            return cbHasNewName.isChecked() && !TextUtils.isEmpty(ViewUtils.getText(etNewName));
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
