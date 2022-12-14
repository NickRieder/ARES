package com.example.ares;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentNewuserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewUserFragment extends Fragment {
    private FragmentNewuserBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 1 = employee
    // 2 = employer
    private int userDecision;
    private Employee employee_profile;
    private Employer employer_profile;
    private Random RandoGenerator;
    private int result;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        result = 0;
        RandoGenerator = new Random();
        userDecision = 0;
        employee_profile = new Employee();
        employer_profile = new Employer();
        binding = FragmentNewuserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //back button:
        binding.backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NewUserFragment.this)
                        .navigate(R.id.action_newUserFragment_to_FirstFragment);
            }
        });

        //save button:
        binding.saveNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check userDecision for Employee vs. Employer
                //Save new account into corresponding database:
                if (userDecision == 1){
                    employee_profile.setUsername(binding.userInputUsername.getText().toString());
                    employee_profile.setPassword(binding.userInputPassword.getText().toString());
                    employee_profile.setFirstName(binding.userInputFirstname.getText().toString());
                    employee_profile.setLastName(binding.userInputLastname.getText().toString());
                    employee_profile.setEmployerId(Integer.parseInt(binding.userInputEmployerId.getText().toString()));
                    employee_profile.setId(getEmployeeId());
                    //save to database here!
                    db.collection("employees").document("Employee_" + employee_profile.getId()).set(employee_profile);
                    Log.d("Creation","New Employee Added to Database.");
                } else if (userDecision == 2){
                    employer_profile.setUsername(binding.userInputUsername.getText().toString());
                    employer_profile.setPassword(binding.userInputPassword.getText().toString());
                    employer_profile.setStatus("Active");
                    employer_profile.setCompanyName(binding.userInputCompanyName.getText().toString());
                    employer_profile.setId(getEmployerId());
                    //save to database here!
                    db.collection("employers").document("Employer_" + employer_profile.getId()).set(employer_profile);
                    Log.d("Creation","New Employer Added to Database.");
                } else {
                    Log.d("RadioButtonError","userDecision not set to 1 or 2.");
                }
                NavHostFragment.findNavController(NewUserFragment.this)
                        .navigate(R.id.action_newUserFragment_to_FirstFragment);
            }
        });

        //radio button employee:
        binding.radioEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                switch(view.getId()) {
                    case R.id.radio_employee:
                        if (checked)
                            //save as employee
                            userDecision = 1;
                            //clean slate:
                            binding.nameText.setVisibility(View.GONE);
                            binding.userInputCompanyName.setVisibility(View.GONE);
                            //make proper form fields visible:
                            binding.usernameText.setVisibility(View.VISIBLE);
                            binding.userInputUsername.setVisibility(View.VISIBLE);
                            binding.passwordText.setVisibility(View.VISIBLE);
                            binding.userInputPassword.setVisibility(View.VISIBLE);
                            binding.firstnameText.setVisibility(View.VISIBLE);
                            binding.userInputFirstname.setVisibility(View.VISIBLE);
                            binding.lastnameText.setVisibility(View.VISIBLE);
                            binding.userInputLastname.setVisibility(View.VISIBLE);
                            binding.employerText.setVisibility(View.VISIBLE);
                            binding.userInputEmployerId.setVisibility(View.VISIBLE);
                            Log.d("RadioButton","Employee Checked.");
                        break;
                }
            }
        });

        //radio button employer:
        binding.radioEmployer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                switch(view.getId()) {
                    case R.id.radio_employer:
                        if (checked)
                            //save as employer
                            userDecision = 2;
                            //clean slate:
                            binding.firstnameText.setVisibility(View.GONE);
                            binding.userInputFirstname.setVisibility(View.GONE);
                            binding.lastnameText.setVisibility(View.GONE);
                            binding.userInputLastname.setVisibility(View.GONE);
                            binding.employerText.setVisibility(View.GONE);
                            binding.userInputEmployerId.setVisibility(View.GONE);
                            //make proper form fields visible:
                            binding.usernameText.setVisibility(View.VISIBLE);
                            binding.userInputUsername.setVisibility(View.VISIBLE);
                            binding.passwordText.setVisibility(View.VISIBLE);
                            binding.userInputPassword.setVisibility(View.VISIBLE);
                            binding.nameText.setVisibility(View.VISIBLE);
                            binding.userInputCompanyName.setVisibility(View.VISIBLE);
                            Log.d("RadioButton","Employer Checked.");
                        break;
                }
            }
        });

    }

    public int getEmployeeId(){
        result = 0;
        result = RandoGenerator.nextInt(1000);
        //pull all existing employee Id's into int array
        //check result with each value of array:

        db.collection("employees").whereEqualTo("id",result).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        //existing employee already holds current id:
                        result = getEmployeeId();
                    } else {
                        //current id is unique:
                        employee_profile.setId(result);
                    }
                }
            }
        });
        return result;
    }

    public int getEmployerId(){
        result = 0;
        result = RandoGenerator.nextInt(1000);
        //pull all existing employer Id's into int array
        //check result with each value of array:

        db.collection("employers").whereEqualTo("id",result).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        //existing employer already holds current id:
                        result = getEmployerId();
                    } else {
                        //current id is unique:
                        employer_profile.setId(result);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
