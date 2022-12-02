package com.example.ares;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentFirstBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private int currentUserId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //this is used for login purposes:
    //0 = username found, password matched, account exists, login successful
    //1 = username not found, login failed
    //2 = username found, password did not match, login failed
    private int token;
    private int authentication_flag;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        currentUserId = 0;

        token = 0;

        authentication_flag = 0;

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUserLogin();
            }
        });

        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_newUserFragment);
            }
        });

        binding.passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    authenticateUserLogin();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public void getEmployees(LoginCallback loginCallback) {
        db.collection("employees").whereEqualTo("username",binding.usernameText.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        List<Employee> empList = task.getResult().toObjects(Employee.class);
                        loginCallback.onCallback(empList);
                    }
                    else {
                        //username not found:
                        authentication_flag = 1;
                    }
                }

            }
        });
    }

    public void getEmployers(EmployerLoginCallback employerLoginCallback) {
        db.collection("employers").whereEqualTo("username",binding.usernameText.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        List<Employer> emprList = task.getResult().toObjects(Employer.class);
                        employerLoginCallback.onCallback(emprList);
                    }
                    else {
                        //username not found:
                        authentication_flag = 1;
                    }
                }

            }
        });
    }

    public void authenticateUserLogin(){
        authentication_flag = 0;

        //check for username:
        getEmployees(new LoginCallback() {
            @Override
            public void onCallback(List<Employee> empList) {
                if(empList.isEmpty()) {
                    authenticateEmployerLogin();
                    return;
                }
                else {
                    if(!empList.get(0).getPassword().equals(binding.passwordText.getText().toString())) {
                        authentication_flag = 2;
                    }
                }
                if (authentication_flag == 0){
                    Log.d("Login","Login Successful.");
                    currentUserId = empList.get(0).getId();
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentEmpId", currentUserId);
                    bundle.putInt("isEmployer", 0);
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_RecyclerActivity, bundle);
                } else if (authentication_flag == 1){
                    Log.d("Login","No existing account found with this username, login failed.");
                } else if (authentication_flag == 2){
                    Log.d("Login","Existing account found, password did not match, login failed.");
                } else {
                    Log.d("Error","Login Error Unresolved.");
                }
            }
        });
    }

    public void authenticateEmployerLogin() {
        authentication_flag = 0;

        //check for username:
        getEmployers(new EmployerLoginCallback() {
            @Override
            public void onCallback(List<Employer> emprList) {
                if (emprList.isEmpty()) {
                    authentication_flag = 1;
                } else {
                    if (!emprList.get(0).getPassword().equals(binding.passwordText.getText().toString())) {
                        authentication_flag = 2;
                    }
                }
                if (authentication_flag == 0) {
                    Log.d("Login", "Login Successful.");
                    currentUserId = emprList.get(0).getId();
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentEmpId", currentUserId);
                    bundle.putInt("isEmployer", 1);
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_RecyclerActivity, bundle);
                } else if (authentication_flag == 1) {
                    Log.d("Login", "No existing account found with this username, login failed.");
                } else if (authentication_flag == 2) {
                    Log.d("Login", "Existing account found, password did not match, login failed.");
                } else {
                    Log.d("Error", "Login Error Unresolved.");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}