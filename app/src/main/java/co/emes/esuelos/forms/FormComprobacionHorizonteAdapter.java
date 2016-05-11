package co.emes.esuelos.forms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.layout.FragmentTesting;
import co.emes.esuelos.model.FormComprobacionHorizonte;

/**
 * Created by csarmiento on 25/04/16.
 */
public class FormComprobacionHorizonteAdapter extends ArrayAdapter<FormComprobacionHorizonte> {
    Context context;
    int layoutResourceId;
    List<FormComprobacionHorizonte> data = new LinkedList<>();
    FragmentManager fragmentManager;

    public FormComprobacionHorizonteAdapter(Context context, FragmentManager fragmentManager, int layoutResourceId, List<FormComprobacionHorizonte> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.inputDepth1 = (TextView) row.findViewById(R.id.input_depth_1);
            holder.inputColor1 = (TextView) row.findViewById(R.id.input_color_1);
            holder.inputTexture1 = (TextView) row.findViewById(R.id.input_texture_1);
            holder.btnEditFormComprobacion = (Button) row.findViewById(R.id.btn_edit_skyline);
            holder.btnDeleteFormComprobacion = (Button) row.findViewById(R.id.btn_delete_skyline);

            holder.btnEditFormComprobacion.setTransformationMethod(null);
            holder.btnDeleteFormComprobacion.setTransformationMethod(null);

            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }

        FormComprobacionHorizonte object = data.get(position);
        holder.inputDepth1.setText(String.valueOf(object.getProfundidad()));
        holder.inputColor1.setText(object.getDescColorHue() + " - " + object.getDescColorValue() + " - " +
                object.getDescColorChroma());
        holder.inputTexture1.setText(object.getDescTipoMaterial());
        holder.btnEditFormComprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTesting fragmentTesting = (FragmentTesting)fragmentManager.findFragmentByTag("FragmentTesting");
                fragmentTesting.editFormComprobacionHorizonte(position);
            }
        });

        holder.btnDeleteFormComprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete(position);
            }
        });
        return row;
    }

    private void confirmDelete(final int position){
        final AlertDialog alert = new AlertDialog.Builder(
                new ContextThemeWrapper(context,android.R.style.Theme_Dialog))
                .create();
        alert.setTitle(context.getResources().getString(R.string.ndi_summary));
        alert.setMessage(context.getResources().getString(R.string.msg_confirm));
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(position);
                        notifyDataSetChanged();
                        FragmentTesting fragmentTesting = (FragmentTesting)fragmentManager.findFragmentByTag("FragmentTesting");
                        fragmentTesting.removeFormComprobacionHorizonte();
                    }
                });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
        alert.show();
    }

    static class UserHolder {
        TextView inputDepth1;
        TextView inputColor1;
        TextView inputTexture1;
        Button btnEditFormComprobacion;
        Button btnDeleteFormComprobacion;
    }

}

