package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.widget.TextView;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.data.MyDbHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Analytics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Analytics extends Fragment {

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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
        TextView tvFundTransfer = view.findViewById(R.id.tvFundTransfer);
        TextView tvTripFare = view.findViewById(R.id.tvTripFare);
        TextView tvVegetables = view.findViewById(R.id.tvVegetables);
        TextView tvFruits = view.findViewById(R.id.tvFruits);
        TextView tvSnacks = view.findViewById(R.id.tvSnacks);
        TextView tvStationary = view.findViewById(R.id.tvStationary);
        TextView tvTrain = view.findViewById(R.id.tvTrain);
        TextView tvFlight = view.findViewById(R.id.tvFlight);
        pieChart = view.findViewById(R.id.piechart);
        MyDbHandler db = new MyDbHandler(getContext());
        tvFundTransfer.setText(String.valueOf(db.getCategory("Fund Transfer")));
        tvTripFare.setText(String.valueOf(db.getCategory("Trip Fare")));
        tvVegetables.setText(String.valueOf(db.getCategory("Vegetables")));
        tvFruits.setText(String.valueOf(db.getCategory("Fruits")));
        tvSnacks.setText(String.valueOf(db.getCategory("Snacks")));
        tvStationary.setText(String.valueOf(db.getCategory("Stationary")));
        tvTrain.setText(String.valueOf(db.getCategory("Train Ticket")));
        tvFlight.setText(String.valueOf(db.getCategory("Flight Ticket")));
        pieChart.addPieSlice(
                new PieModel(
                        "Fund Transfer",
                        Integer.parseInt(tvFundTransfer.getText().toString().trim()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Trip Fare",
                        Integer.parseInt(tvTripFare.getText().toString().trim()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Vegetables",
                        Integer.parseInt(tvVegetables.getText().toString().trim()),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Fruits",
                        Integer.parseInt(tvFruits.getText().toString().trim()),
                        Color.parseColor("#8929F6")));
        pieChart.addPieSlice(
                new PieModel(
                        "Snacks",
                        Integer.parseInt(tvSnacks.getText().toString().trim()),
                        Color.parseColor("#3029F6")));
        pieChart.addPieSlice(
                new PieModel(
                        "Stationary",
                        Integer.parseInt(tvStationary.getText().toString().trim()),
                        Color.parseColor("#E229F6")));
        pieChart.addPieSlice(
                new PieModel(
                        "Train Ticket",
                        Integer.parseInt(tvTrain.getText().toString().trim()),
                        Color.parseColor("#7AED06")));
        pieChart.addPieSlice(
                new PieModel(
                        "Flight Ticket",
                        Integer.parseInt(tvFlight.getText().toString().trim()),
                        Color.parseColor("#06ED48")));

        // To animate the pie chart
        pieChart.startAnimation();
        return view;
    }


}