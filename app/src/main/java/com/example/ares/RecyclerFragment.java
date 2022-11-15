package com.example.ares;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ares.databinding.FragmentRecyclerviewBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RecyclerFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener{

    private FragmentRecyclerviewBinding binding;
    RecyclerViewAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int currentEmpId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        currentEmpId = (int) this.getArguments().get("currentEmpId");
        getRoList(new RoCallback() {
            @Override
            public void onCallback(List<RepairOrder> roList) {
                RecyclerView recyclerView = getView().findViewById(R.id.rvRepairOrder);
                recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerFragment.this.getContext()));
                adapter = new RecyclerViewAdapter(RecyclerFragment.this.getContext(), roList);
                adapter.setClickListener(RecyclerFragment.this);
                recyclerView.setAdapter(adapter);
            }
        });
        binding = FragmentRecyclerviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void getRoList(RoCallback roCallback) {
        db.collection("repairOrders").whereEqualTo("id", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<RepairOrder> roList = task.getResult().toObjects(RepairOrder.class);
                                roCallback.onCallback(roList);
                            }
                        } else {
                            Log.d("Retrieval", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("IDK", "Back pressed");
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(RecyclerFragment.this)
                        .navigate(R.id.action_RecyclerFragment_to_MainActivity);
            }
        });

        /*binding.buttonRo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_repairOrderFragment2);
            }
        });*/

        //create new RO:
        binding.buttonNewRo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(RecyclerFragment.this)
                        .navigate(R.id.action_RecyclerFragment_to_newRepairOrderFragment);
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
        Bundle bundle = new Bundle();
        bundle.putInt("num", Integer.parseInt(adapter.getItem(position)));
        NavHostFragment.findNavController(RecyclerFragment.this).navigate(R.id.action_RecyclerFragment_to_repairOrderFragment2, bundle);
    }

}