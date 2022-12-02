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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ares.databinding.FragmentEmployerLandingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployerLandingFragment extends Fragment implements EmployeeRecViewAdapter.ItemClickListener{
    private FragmentEmployerLandingBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<Employee> employeeList = new ArrayList<>();
    EmployeeRecViewAdapter adapter;

    //this variable should hold the current employer's unique id
    //so we can use it to make a back-end call for all employees tied to it:
    private int employerId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        employerId = getArguments().getInt("currentEmprId");
        populateEmployeeList(new EmployeeCallback() {
            @Override
            public void onCallback(List<Employee> empList) {
                RecyclerView recyclerView = getView().findViewById(R.id.rvEmpList);
                recyclerView.setLayoutManager(new LinearLayoutManager(EmployerLandingFragment.this.getContext()));
                adapter = new EmployeeRecViewAdapter(EmployerLandingFragment.this.getContext(), empList);
                adapter.setClickListener(EmployerLandingFragment.this);
                recyclerView.setAdapter(adapter);
            }
        });
        binding = FragmentEmployerLandingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.emprNum.setText("Employer Number: " + employerId);
        //back button:
        binding.backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EmployerLandingFragment.this)
                        .navigate(R.id.action_employerLandingFragment_to_FirstFragment);
            }
        });

    }

    private void populateEmployeeList(EmployeeCallback employeeCallback) {
        //back-end call to populate employeeList
        db.collection("employees").whereEqualTo("employerId", employerId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        employeeList = task.getResult().toObjects(Employee.class);
                        employeeCallback.onCallback(employeeList);
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
        binding = null;
    }

    @Override
    public void onItemClick(View view, int position) {
        //Nothing to do
    }
}