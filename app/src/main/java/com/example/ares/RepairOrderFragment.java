package com.example.ares;

import static java.util.Objects.isNull;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentRepairorderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RepairOrderFragment extends Fragment {

    private FragmentRepairorderBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RepairOrder currentRo;
    private Vehicle currentVehicle;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        Log.d("Creation","onCreateView() triggered!");
        binding = FragmentRepairorderBinding.inflate(inflater, container, false);
        currentRo = new RepairOrder();
        db.collection("repairOrders").whereEqualTo("number", getArguments().getInt("num"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<RepairOrder> RepairOrderList = task.getResult().toObjects(RepairOrder.class);
                                currentRo = RepairOrderList.get(0);
                                setRoText(RepairOrderList.get(0));
                                getVehicleInfo(RepairOrderList.get(0).getVehicleId());
                                initTable(RepairOrderList.get(0).getRepairs());
                            }
                        } else {
                            Log.d("Retrieval", "Error getting documents.", task.getException());
                        }
                    }
                });
        disableTextEditing(binding.make);
        disableTextEditing(binding.model);
        disableTextEditing(binding.year);
        return binding.getRoot();

    }

    public void getVehicleInfo(int vehicleId) {
        db.collection("vehicles").whereEqualTo("id", vehicleId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Vehicle> VehicleList = task.getResult().toObjects(Vehicle.class);
                                currentVehicle = VehicleList.get(0);
                                setVehicleTexts(VehicleList.get(0));
                            }
                        } else {
                            Log.d("Retrieval", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void setVehicleTexts(Vehicle vehicle) {
        binding.year.setText(String.valueOf(vehicle.getYear()));
        binding.make.setText(vehicle.getMake());
        binding.model.setText(vehicle.getModel());
        binding.plate.setText(vehicle.getLicensePlate());
    }

    public void setRoText(RepairOrder repairOrder) {
        binding.roNumber.setText(String.valueOf(repairOrder.getNumber()));
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Clicked", binding.buttonEdit.getText().toString());
                if (binding.buttonEdit.getText().toString().equals("Edit")) {
                    enableTextEditing(binding.year);
                    enableTextEditing(binding.make);
                    enableTextEditing(binding.model);
                    binding.buttonEdit.setText("Save");
                }
                else {
                    disableTextEditing(binding.roNumber);
                    disableTextEditing(binding.year);
                    disableTextEditing(binding.make);
                    disableTextEditing(binding.model);
                    currentVehicle.setYear(Integer.parseInt(binding.year.getText().toString()));
                    currentVehicle.setMake(binding.make.getText().toString());
                    currentVehicle.setModel(binding.model.getText().toString());
                    db.collection("vehicles").document("Vehicle_" + currentVehicle.getLicensePlate()).set(currentVehicle);
                    binding.buttonEdit.setText("Edit");
                }
            }
        });

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("repairOrders").document("RepairOrder_" + currentRo.getNumber()).delete();
                NavHostFragment.findNavController(RepairOrderFragment.this)
                        .navigate(R.id.action_repairOrderFragment2_to_RecyclerFragment);
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RepairOrderFragment.this)
                        .navigate(R.id.action_repairOrderFragment2_to_RecyclerFragment);
            }
        });
    }

    private void disableTextEditing(EditText editText) {
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setCursorVisible(false);
    }

    private void enableTextEditing(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.setCursorVisible(true);
    }

    public void initTable(Map<String, Integer> repairs) {
/*
        if (Objects.isNull(repairs)) {
            TableLayout repairTable = (TableLayout) binding.repairTable;
            TableRow row0 = new TableRow(this.getContext());

            TextView tv0 = new TextView(this.getContext());
            tv0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0F);
            tv0.setBackgroundColor(Color.WHITE);
            tv0.setTextColor(Color.BLACK);
            tv0.setText("Repair");
            tv0.setGravity(Gravity.CENTER_HORIZONTAL);
            tv0.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            row0.addView(tv0);

            TextView tv1 = new TextView(this.getContext());
            tv1.setBackgroundColor(Color.WHITE);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0F);
            tv1.setTextColor(Color.BLACK);
            tv1.setText("Hours");
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);
            tv1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            row0.addView(tv1);

            repairTable.addView(row0);

            List<String> keys = new ArrayList<String>(repairs.keySet());

            for (int i = 0; i < repairs.size(); i++) {
                TableRow row = new TableRow(this.getContext());

                TextView tv2 = new TextView(this.getContext());
                tv2.setBackgroundColor(Color.WHITE);
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0F);
                tv2.setTextColor(Color.BLACK);
                tv2.setText(keys.get(i));
                tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                row.addView(tv2);

                TextView tv3 = new TextView(this.getContext());
                tv3.setBackgroundColor(Color.WHITE);
                tv3.setTextColor(Color.BLACK);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0F);
                tv3.setText(String.valueOf(repairs.get(keys.get(i))));
                tv3.setGravity(Gravity.CENTER_HORIZONTAL);
                row.addView(tv3);

                repairTable.addView(row);
            }
        }
        */
    }
}
