package com.example.ares;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ares.databinding.FragmentNewrepairorderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import java.util.Random;


public class NewRepairOrderFragment extends Fragment {

    private FragmentNewrepairorderBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference sr = storage.getReference();

    private RepairOrder currentRO;
    private Vehicle currentVehicle;
    private String dateString;
    private Random rand;
    private int result;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        result = 0;
        rand = new Random();
        currentRO = new RepairOrder();
        currentVehicle = new Vehicle();
        dateString = "";
        Log.d("msg", "binding created");
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

        binding.buttonImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                launcher.launch(photoPickerIntent);
            }
        });

        //save button:
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Make sure currentRO is filled with values from form:
                populateObjectsFromForm();
                //back-end call to save the data inside the currentRO object to the db.

                //navigate back to parent page

            }
        });
    }

    public void populateObjectsFromForm(){
        Repair:
        currentRO.setId(0);
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
        currentVehicle.setId(createVehicleId());
        currentVehicle.setMake(binding.make.getText().toString());
        currentVehicle.setModel(binding.model.getText().toString());
        currentVehicle.setYear(Integer.parseInt(binding.year.getText().toString()));
        currentVehicle.setLicensePlate(binding.plate.getText().toString());
        currentVehicle.setPastRepairs(null);
        //Vehicle
        getVehicleInfo(this.getView(), new VehicleCallback() {
            @Override
            public void onCallback(List<Vehicle> vehicleList) {
                if(!vehicleList.isEmpty()) {
                    currentRO.setVehicleId(vehicleList.get(0).getId());
                    currentVehicle = vehicleList.get(0);
                    db.collection("repairOrders").document("RepairOrder_" + currentRO.getNumber())
                            .set(currentRO)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            NavHostFragment.findNavController(NewRepairOrderFragment.this)
                                    .navigate(R.id.action_newRepairOrderFragment_to_RecyclerFragment);
                        }
                    });
                }
                else {
                    currentRO.setVehicleId(currentVehicle.getId());
                    db.collection("repairOrders").document("RepairOrder_" + currentRO.getNumber()).set(currentRO).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            NavHostFragment.findNavController(NewRepairOrderFragment.this)
                                    .navigate(R.id.action_newRepairOrderFragment_to_RecyclerFragment);
                        }
                    });
                    db.collection("vehicles").document("Vehicle_" + currentVehicle.getLicensePlate()).set(currentVehicle);
                }
            }
        });
    }

    public void getVehicleInfo(View view, VehicleCallback vehicleCallback){
        db.collection("vehicles").whereEqualTo("licensePlate", binding.plate.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                vehicleCallback.onCallback(task.getResult().toObjects(Vehicle.class));
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("msg", "binding destroyed");
        binding = null;
    }

    public int createVehicleId() {
        result = rand.nextInt(1000);
        db.collection("vehicles").whereEqualTo("id", result)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult() != null) {
                                 result = createVehicleId();
                            }
                        }
                    }
                });
        return result;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK
                && result.getData() != null) {
            Uri photoUri = result.getData().getData();
            UploadTask ut = sr.child(photoUri.getLastPathSegment()).putFile(photoUri);
            ut.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sr.child(photoUri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            currentRO.setCarPicture(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            });

        }
    });

}