package com.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.androidatelier.lunchin.R;
import com.androidatelier.lunchin.settings.SettingsAccess;
import com.androidatelier.lunchin.util.Constants;
import com.androidatelier.lunchin.util.Formatter;

public class EditTextDialogFragment extends DialogFragment {
    public static EditTextDialogFragment newInstance(CharSequence title, String text) {
        EditTextDialogFragment fragment = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putCharSequence(Constants.KEY_TITLE, title);
        args.putString(Constants.KEY_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(
                getActivity()).inflate(R.layout.dialog_edit_text, null);
        builder.setView(textInputLayout);

        CharSequence title = getArguments().getCharSequence(Constants.KEY_TITLE);
        textInputLayout.setHint(title);

        final EditText editText = (EditText) textInputLayout.findViewById(R.id.edit_text);
        String text = getArguments().getString(Constants.KEY_TEXT);


        if (getTargetRequestCode() == Constants.REQUEST_CODE_GROSS_SALARY_DIALOG) {
            // only allow integers
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setText(text);
        } else if (getTargetRequestCode() == Constants.REQUEST_CODE_LUNCH_COST_DIALOG) {
            double averageLunchCost = Double.parseDouble(text);
            editText.setText(Formatter.formatToDecimalHundreths(averageLunchCost));
        }

        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int resultCode = Activity.RESULT_CANCELED;

                Intent data = new Intent();
                data.replaceExtras(getArguments());

                String inputtedText = editText.getText().toString();
                if (!TextUtils.isEmpty(inputtedText)) {
                    data.putExtra(Constants.KEY_TEXT, inputtedText);
                    resultCode = Activity.RESULT_OK;

                    if (getTargetRequestCode() == Constants.REQUEST_CODE_LUNCH_COST_DIALOG) {
                        new SettingsAccess(getActivity()).setAverageLunchCost(inputtedText);
                    } else if (getTargetRequestCode() == Constants.REQUEST_CODE_GROSS_SALARY_DIALOG) {
                        new SettingsAccess(getActivity()).setGrossAnnualSalary(Integer.parseInt(inputtedText));
                    }

                }

                getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, null);

        return builder.create();
    }
}