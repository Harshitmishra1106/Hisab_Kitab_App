package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.widget.TextView;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.data.MyDbHandler;
import com.example.myapplication.databinding.FragmentAnalyticsBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Analytics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Analytics extends Fragment {

    private FragmentAnalyticsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    PieChart pieChart;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Analytics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Analytics.
     */
    // TODO: Rename and change types and number of parameters
    public static Analytics newInstance(String param1, String param2) {
        Analytics fragment = new Analytics();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int[] costs = new int[8];
        MyDbHandler db = new MyDbHandler(getContext());
        binding.tvFundTransfer.setText(String.valueOf(db.getCategory("Fund Transfer")));
        costs[0]=Integer.parseInt(binding.tvFundTransfer.getText().toString());
        binding.tvTripFare.setText(String.valueOf(db.getCategory("Trip Fare")));
        costs[1]=Integer.parseInt(binding.tvTripFare.getText().toString());
        binding.tvVegetables.setText(String.valueOf(db.getCategory("Vegetables")));
        costs[2]=Integer.parseInt(binding.tvVegetables.getText().toString());
        binding.tvFruits.setText(String.valueOf(db.getCategory("Fruits")));
        costs[3]=Integer.parseInt(binding.tvFruits.getText().toString());
        binding.tvSnacks.setText(String.valueOf(db.getCategory("Snacks")));
        costs[4]=Integer.parseInt(binding.tvSnacks.getText().toString());
        binding.tvStationary.setText(String.valueOf(db.getCategory("Stationary")));
        costs[5]=Integer.parseInt(binding.tvStationary.getText().toString());
        binding.tvTrain.setText(String.valueOf(db.getCategory("Train Ticket")));
        costs[6]=Integer.parseInt(binding.tvTrain.getText().toString());
        binding.tvFlight.setText(String.valueOf(db.getCategory("Flight Ticket")));
        costs[7]=Integer.parseInt(binding.tvFlight.getText().toString());
        int maxi=0;
        String maxCost="";
        for(int i=0;i<8;i++){
            if(costs[i]>maxi){
                maxi=costs[i];
                if(i==0) maxCost = "Fund Transfer";
                else if(i==1) maxCost = "Trip Fare";
                else if(i==2) maxCost = "Vegetables";
                else if(i==3) maxCost = "Fruits";
                else if(i==4) maxCost = "Snacks";
                else if(i==5) maxCost = "Stationary";
                else if(i==6) maxCost = "Train Ticket";
                else maxCost = "Flight Ticket";
            }
        }
        binding.piechart.addPieSlice(
                new PieModel(
                        "Fund Transfer",
                        Integer.parseInt(binding.tvFundTransfer.getText().toString().trim()),
                        Color.parseColor("#FFA726")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Trip Fare",
                        Integer.parseInt(binding.tvTripFare.getText().toString().trim()),
                        Color.parseColor("#66BB6A")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Vegetables",
                        Integer.parseInt(binding.tvVegetables.getText().toString().trim()),
                        Color.parseColor("#EF5350")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Fruits",
                        Integer.parseInt(binding.tvFruits.getText().toString().trim()),
                        Color.parseColor("#8929F6")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Snacks",
                        Integer.parseInt(binding.tvSnacks.getText().toString().trim()),
                        Color.parseColor("#3029F6")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Stationary",
                        Integer.parseInt(binding.tvStationary.getText().toString().trim()),
                        Color.parseColor("#E229F6")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Train Ticket",
                        Integer.parseInt(binding.tvTrain.getText().toString().trim()),
                        Color.parseColor("#7AED06")));
        binding.piechart.addPieSlice(
                new PieModel(
                        "Flight Ticket",
                        Integer.parseInt(binding.tvFlight.getText().toString().trim()),
                        Color.parseColor("#06ED48")));

        binding.piechart.startAnimation();

        scheduleDailyNotification(maxi, maxCost);

    }

    private void scheduleDailyNotification(int maxi, String maxCost) {
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        intent.putExtra("category", maxCost);
        intent.putExtra("cost", maxi);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }


}