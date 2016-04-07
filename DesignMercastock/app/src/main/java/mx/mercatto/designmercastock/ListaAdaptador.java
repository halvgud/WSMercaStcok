package mx.mercatto.designmercastock;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

/**
 * Created by Juan Carlos De León on 02/04/2016.
 */
public class ListaAdaptador extends SimpleAdapter {
    private int[] colors = new int[] { 0x00000000, 0xAAF5F5F5 };

    public ListaAdaptador(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int colorPos = position % colors.length;
        view.setBackgroundColor(colors[colorPos]);
        return view;
    }
}