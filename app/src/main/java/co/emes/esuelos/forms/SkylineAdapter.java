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
import co.emes.esuelos.model.Skyline;

/**
 * Created by csarmiento on 25/04/16.
 */
public class SkylineAdapter extends ArrayAdapter<Skyline> {
    Context context;
    int layoutResourceId;
    List<Skyline> data = new LinkedList<>();
    FragmentManager fragmentManager;

    public SkylineAdapter(Context context, FragmentManager fragmentManager, int layoutResourceId, List<Skyline> data) {
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
            holder.btnEditSkyline = (Button) row.findViewById(R.id.btn_edit_skyline);
            holder.btnDeleteSkyline = (Button) row.findViewById(R.id.btn_delete_skyline);

            holder.btnEditSkyline.setTransformationMethod(null);
            holder.btnDeleteSkyline.setTransformationMethod(null);

            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }

        Skyline object = data.get(position);
        holder.inputDepth1.setText(object.getDepth().toString());
        holder.inputColor1.setText(object.getHueColor().getDescripcion() + " - " + object.getValueColor() + " - " +
        object.getChromaColor());
        holder.inputTexture1.setText(object.getTexture().getDescripcion());
        holder.btnEditSkyline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTesting fragmentTesting = (FragmentTesting)fragmentManager.findFragmentByTag("FragmentTesting");
                fragmentTesting.editItemSkyline(position);
            }
        });

        holder.btnDeleteSkyline.setOnClickListener(new View.OnClickListener() {
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
        alert.setTitle(context.getResources().getString(R.string.tst_title));
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
                        fragmentTesting.removeItemSkyline();
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
        Button btnEditSkyline;
        Button btnDeleteSkyline;
    }

}

