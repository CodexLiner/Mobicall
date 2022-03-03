package com.mobicall.call.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobicall.call.R;
import com.mobicall.call.UI.PermisionActivity;
import com.mobicall.call.databinding.FragmentHomeBinding;
import com.mobicall.call.databinding.FragmentPermisionFragmentBinding;
import com.mobicall.call.services.PermisionClass;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link permision_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class permision_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentPermisionFragmentBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public permision_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment permision_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static permision_fragment newInstance(String param1, String param2) {
        permision_fragment fragment = new permision_fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPermisionFragmentBinding.inflate(inflater ,container , false);
        checkPermision();
        return binding.getRoot();
    }

    private void checkPermision() {
        if (PermisionClass.hasPermision(getContext() , PermisionClass.permisions)){

        }else if (!PermisionClass.hasPermision(getContext()  , PermisionClass.permisions)){
            ActivityCompat.requestPermissions(requireActivity(), PermisionClass.permisions, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("TAG", "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}