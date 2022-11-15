package com.example.ares;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentNewuserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private List employeeIdArray;
    private int result;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        result = 0;
        employeeIdArray = new ArrayList();
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
                    //save to database here!
                    db.collection("employees").document("Employee_" + getEmployeeId()).set(employee_profile);
                    Log.d("Creation","New Employee Added to Database.");
                } else if (userDecision == 2){
                    employer_profile.setUsername(binding.userInputUsername.getText().toString());
                    employer_profile.setPassword(binding.userInputPassword.getText().toString());
                    employer_profile.setStatus("Active");
                    //save to database here!
                    db.collection("employers").document("Employer_" + getEmployerId()).set(employer_profile);
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
        //pull all existing employee Id's into int array
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
