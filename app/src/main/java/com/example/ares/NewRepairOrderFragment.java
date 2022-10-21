package com.example.ares;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentNewrepairorderBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class NewRepairOrderFragment extends Fragment {

    private FragmentNewrepairorderBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RepairOrder currentRO;
    private Vehicle currentVehicle;
    private String dateString;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        currentRO = null;
        currentVehicle = null;
        dateString = "";
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
                //Make sure currentRO is filled with values from form:
                populateObjectsFromForm();
                //back-end call to save the data inside the currentRO object to the db.
                //db.collection("repairOrders").add(currentRO);
                db.collection("vehicles").add(currentVehicle);
                //navigate back to parent page
                NavHostFragment.findNavController(NewRepairOrderFragment.this)
                        .navigate(R.id.action_newRepairOrderFragment_to_SecondFragment);
            }
        });
    }

    public void populateObjectsFromForm(){
        Repair:
        currentRO.id = 100;
        currentRO.paperOrder = "";
        currentRO.carPicture = "";
        currentRO.repairs = null;
        dateString = binding.date.toString();
        currentRO.number = Integer.parseInt(binding.roNumber.toString());
        currentRO.hours = Integer.parseInt(binding.hours.toString());
        currentRO.status = binding.status.toString();
        try {
            currentRO.date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Error", "Error with format of date input.");
        }
        //Vehicle
        currentVehicle.setId(100);
        currentVehicle.setMake(binding.make.toString());
        currentVehicle.setModel(binding.model.toString());
        currentVehicle.setYear(Integer.parseInt(binding.year.toString()));
        currentVehicle.setLP(binding.plate.toString());
        currentVehicle.setPastRepairs(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}