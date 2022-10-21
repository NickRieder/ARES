package com.example.ares;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentNewrepairorderBinding;
import com.google.firebase.firestore.FirebaseFirestore;


public class NewRepairOrderFragment extends Fragment {

    private FragmentNewrepairorderBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RepairOrder currentRO;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        currentRO = null;
        binding = FragmentNewrepairorderBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //back button:
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NewRepairOrderFragment.this)
                        .navigate(R.id.action_newRepairOrderFragment_to_SecondFragment);
            }
        });

        //save button:
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To-do
                //Make sure currentRO is filled with values from form:


                //back-end call to save the data inside the currentRO object to the db.
                db.collection("repairOrders").add(currentRO);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}