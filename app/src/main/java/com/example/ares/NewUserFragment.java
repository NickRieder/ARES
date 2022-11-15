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

    private Employee employee_profile;
    private Random RandoGenerator;
    private int result;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        result = 0;
        RandoGenerator = new Random();
        employee_profile = new Employee();
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
                employee_profile.setUsername(binding.userInputUsername.getText().toString());
                employee_profile.setPassword(binding.userInputPassword.getText().toString());
                employee_profile.setFirstName(binding.userInputFirstname.getText().toString());
                employee_profile.setLastName(binding.userInputLastname.getText().toString());
                employee_profile.setId(getEmployeeId());
                //save to database here!
                db.collection("employees").document("Employee_" + employee_profile.getId()).set(employee_profile);
                Log.d("Creation","New Employee Added to Database.");
                NavHostFragment.findNavController(NewUserFragment.this).navigate(R.id.action_newUserFragment_to_FirstFragment);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
