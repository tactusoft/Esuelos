package co.emes.esuelos.forms;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.layout.FragmentFieldNote;
import co.emes.esuelos.layout.FragmentSummaryDetail;
import co.emes.esuelos.layout.FragmentTesting;
import co.emes.esuelos.main.MainActivity;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormNotaCampo;

/**
 * Created by csarmiento on 25/04/16.
 */
public class ArrayAdapterSummary extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
    List<String> data = new LinkedList<>();
    FragmentManager fragmentManager;

    public ArrayAdapterSummary(Context context, FragmentManager fragmentManager,
                               int layoutResourceId, List<String> data) {
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
            holder.inputDate = (TextView) row.findViewById(R.id.input_date);
            holder.btnEditFormComprobacion = (TextView) row.findViewById(R.id.btn_edit_skyline);
            holder.btnEditFormComprobacion.setTransformationMethod(null);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }

        final String object = data.get(position);
        holder.inputDate.setText(object);

        holder.btnEditFormComprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSummaryDetail fragment = FragmentSummaryDetail.newInstance(object);
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        return row;
    }

    static class UserHolder {
        TextView inputDate;
        TextView btnEditFormComprobacion;
    }

}

