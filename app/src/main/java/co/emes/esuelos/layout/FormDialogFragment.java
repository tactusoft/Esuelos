package co.emes.esuelos.layout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by csarmiento on 18/04/16.
 */
public class FormDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    ArrayAdapter<String> arrayAdapter;
    List<String> places = Arrays.asList("Buenos Aires", "Córdoba", "La Plata");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayAdapter.addAll(places);
            }
        }, 5000);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Test List").setAdapter(arrayAdapter, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d(getClass().getSimpleName(), String.format("User clicked item at index %d", which));
    }
}
