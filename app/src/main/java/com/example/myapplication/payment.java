package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.data.MyDbHandler;
import com.example.myapplication.databinding.FragmentPaymentBinding;
import com.example.myapplication.model.Transaction;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class payment extends Fragment {

    private FragmentPaymentBinding binding;
    private ActivityResultLauncher<Intent> upiPaymentLauncher;
    String payeeUpiId ;
    String payeeName ;
    String description ;
    String amount ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public payment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loan.
     */
    // TODO: Rename and change types and number of parameters
    public static payment newInstance(String param1, String param2) {
        payment fragment = new payment();
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
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // startActivityForResult() and onActivityResult() are deprecated so Initialize Activity Result Launcher to use
        // The modern approach in a Fragment is to use the Activity Result API with registerForActivityResult()
        upiPaymentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();

                    if (data != null) {
                        String response = data.getStringExtra("response");

                        // Fallback for apps like Google Pay
                        if (response == null || response.isEmpty()) {
                            response = data.getDataString();
                        }

                        if (response != null && !response.isEmpty()) {
                            String status = getStatusFromResponse(response);
                            if (status.equals("Successful")) {
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);
                                String timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                String dateString = String.format(Locale.getDefault(), "%02d-%02d-%02d", day, month, year);

                                try (MyDbHandler db = new MyDbHandler(getContext())) {
                                    Transaction cost = new Transaction();
                                    cost.setReason("Fund Transfer " + payeeName);
                                    cost.setAmount(Integer.parseInt(amount));
                                    cost.setDate(dateString);
                                    cost.setTime(timeString);
                                    cost.setType("e");
                                    db.addTransaction(cost);
                                    new SweetAlertDialog(binding.getRoot().getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Confirmation!")
                                            .setContentText("Expense details saved successfully")
                                            .show();
                                }
                            } else {
                                Toast.makeText(binding.getRoot().getContext(), "Transaction " + status, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(binding.getRoot().getContext(), "No response from UPI app", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(binding.getRoot().getContext(), "Transaction cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payeeUpiId = Objects.requireNonNull(binding.fieldVpa.getText()).toString().trim();
                payeeName = Objects.requireNonNull(binding.fieldName.getText()).toString().trim();
                description = Objects.requireNonNull(binding.fieldDescription.getText()).toString().trim();
                amount = Objects.requireNonNull(binding.fieldAmount.getText()).toString().trim();
                if(!payeeUpiId.isEmpty()&& !payeeName.isEmpty()&& !description.isEmpty()&& !amount.isEmpty()) payUsingUpi();
                else{
                    Toast.makeText(binding.getRoot().getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void payUsingUpi() {
        double amt = Double.parseDouble(amount);
        String formattedAmount = String.format(Locale.US, "%.2f", amt);

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", payeeUpiId)
                .appendQueryParameter("pn", payeeName)
                .appendQueryParameter("tn", description)
                .appendQueryParameter("am", formattedAmount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (chooser.resolveActivity(requireContext().getPackageManager()) != null) {
            upiPaymentLauncher.launch(chooser);
        } else {
            Toast.makeText(requireContext(), "No UPI app found. Please install one to continue.", Toast.LENGTH_SHORT).show();
        }

    }

    private String getStatusFromResponse(String response) {
        if (response == null) return "FAILED";

        String[] pairs = response.split("&");
        HashMap<String, String> map = new HashMap<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length >= 2) {
                map.put(keyValue[0].toLowerCase(), keyValue[1]);
            }
        }

        String status = map.get("status");
        if (status == null) return "FAILED";

        switch (status.toUpperCase()) {
            case "SUCCESS":
                return "Successful";
            case "FAILURE":
                return "Failed";
            case "SUBMITTED":
                return "Pending";
            default:
                return "Cancelled";
        }
    }
}