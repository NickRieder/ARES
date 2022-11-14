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

        currentRO = new RepairOrder();
        currentVehicle = new Vehicle();
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
                        .navigate(R.id.action_newRepairOrderFragment_to_RecyclerFragment);
            }
        });

        //save button:
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Make sure currentRO is filled with values from form:
                populateObjectsFromForm();
                //back-end call to save the data inside the currentRO object to the db.
                db.collection("repairOrders").document("RepairOrder_" + currentRO.getNumber()).set(currentRO);
                db.collection("vehicles").document("Vehicle_" + currentVehicle.getLicensePlate()).set(currentVehicle);
                //navigate back to parent page
                NavHostFragment.findNavController(NewRepairOrderFragment.this)
                        .navigate(R.id.action_newRepairOrderFragment_to_RecyclerFragment);
            }
        });
    }

    public void populateObjectsFromForm(){
        Repair:
        currentRO.setId(0);
        currentRO.setCarPicture("");
        currentRO.setPaperOrder("");
        currentRO.setHours(Integer.parseInt(binding.hours.getText().toString()));
        currentRO.setStatus(binding.status.getText().toString());
        currentRO.setNumber(Integer.parseInt(binding.roNumber.getText().toString()));
        currentRO.setRepairs(null);
        dateString = binding.date.getText().toString();
        try {
            currentRO.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Error", "Error with format of date input.");
        }

        //Vehicle
        
        currentVehicle.setId(0);
        currentVehicle.setMake(binding.make.getText().toString());
        currentVehicle.setModel(binding.model.getText().toString());
        currentVehicle.setYear(Integer.parseInt(binding.year.getText().toString()));
        currentVehicle.setLicensePlate(binding.plate.getText().toString());
        currentVehicle.setPastRepairs(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}