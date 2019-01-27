package com.demo.savemymoney.monto;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.demo.savemymoney.R;


import java.text.ParseException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MontoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MontoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private  MontoActivity montoActivity;

    public MontoFragment() {
        // Required empty public constructor
    }






    // TODO: Rename and change types and number of parameters
    public static MontoFragment newInstance(String param1, String param2) {
        MontoFragment fragment = new MontoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }
    public void Registrar(View view) throws ParseException   {
        montoActivity.Registrar(view);
    }

    public void  singOut(View view){
        montoActivity.signOut(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monto, container, false);
    }

}
