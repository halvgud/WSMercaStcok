package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Juan Carlos De León on 10/04/2016.
 */
public class FragmentPassword extends Fragment {

    public EditText txtPinActual;
    public EditText txtPinNuevo;
    public EditText txtPinNuevoR;

    public FragmentPassword() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_password, container, false);
        getActivity().setTitle("Cambiar PIN");

        txtPinActual= (EditText)rootView.findViewById(R.id.editText10);
        txtPinNuevo= (EditText)rootView.findViewById(R.id.editText11);
        txtPinNuevoR= (EditText)rootView.findViewById(R.id.editText12);

        txtPinActual.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPinActual.getText().toString();
                value2=txtPinNuevo.getText().toString();
                value3=txtPinNuevoR.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg) && !value3.equals(gg)) && (value2.equals(value3))&&(value1.length()==4 && value2.length()==4 && value2.length()==4)) {
                    getView().findViewById(R.id.button8).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button8).setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        txtPinNuevo.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPinActual.getText().toString();
                value2=txtPinNuevo.getText().toString();
                value3=txtPinNuevoR.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg) && !value3.equals(gg)) && (value2.equals(value3)) && (value2.equals(value3))&&(value1.length()==4 && value2.length()==4 && value2.length()==4)) {
                    getView().findViewById(R.id.button8).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button8).setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        txtPinNuevoR.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPinActual.getText().toString();
                value2=txtPinNuevo.getText().toString();
                value3=txtPinNuevoR.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg) && !value3.equals(gg)) && (value2.equals(value3)) && (value2.equals(value3))&&(value1.length()==4 && value2.length()==4 && value2.length()==4)) {
                    getView().findViewById(R.id.button8).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button8).setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return rootView;
    }

    protected FragmentActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity)activity;
    }

}

