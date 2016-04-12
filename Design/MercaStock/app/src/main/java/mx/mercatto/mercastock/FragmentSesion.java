package mx.mercatto.mercastock;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by Juan Carlos De León on 10/04/2016.
 */
public class FragmentSesion extends Fragment {

    public FragmentSesion() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sesion, container, false);
        getActivity().setTitle("Sesión");
        return rootView;
    }

    protected FragmentActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity)activity;
    }

}
