package com.example.ares;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentEmployerLandingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployerLandingFragment extends Fragment {
    private FragmentEmployerLandingBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<Employee> employeeList = new ArrayList<>();

    //this variable should hold the current employer's unique id
    //so we can use it to make a back-end call for all employees tied to it:
    private int employerId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEmployerLandingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //populate employee list with back-end call:
        populateEmployeeList();

        //back button:
        binding.backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EmployerLandingFragment.this)
                        .navigate(R.id.action_employerLandingFragment_to_FirstFragment);
            }
        });

    }

    private void populateEmployeeList() {
        //back-end call to populate employeeList
        db.collection("employees").whereEqualTo("employerId", employerId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        employeeList = task.getResult().toObjects(Employee.class);
                    } else {
                        //something happened, error.
                    }
                }
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
        binding = null;
    }
}