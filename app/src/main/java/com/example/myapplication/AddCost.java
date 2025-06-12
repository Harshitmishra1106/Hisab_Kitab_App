package com.example.myapplication;

import static java.lang.Integer.parseInt;
import static java.security.AccessController.getContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.data.MyDbHandler;
import com.example.myapplication.model.Transaction;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCost extends Fragment implements AdapterView.OnItemSelectedListener {
    String [] reason = {"Vegetables","Fruits","Snacks","Stationary","Trip Fare","Train Ticket","Flight Ticket","Fund Transfer"};
    public AddCost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCost.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCost newInstance(String param1, String param2) {
        AddCost fragment = new AddCost();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        setExitTransition(inflater.inflateTransition(R.transition.slide_left));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_cost, container, false);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String timeString = String.format(Locale.getDefault(),"%02d:%02d", hour, minute);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = String.format(Locale.getDefault(),"%02d-%02d-%02d",day,month,year);
        TextView save = view.findViewById(R.id.save_button0);
        EditText spend = view.findViewById(R.id.spend);
        MaterialSpinner spinner =(MaterialSpinner) view.findViewById(R.id.purpose);
        spinner.setItems("Select Reason","Vegetables","Fruits","Snacks","Stationary","Trip Fare","Train Ticket","Flight Ticket","Fund Transfer");
        final String[] spin = new String[1];
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                spin[0]=item;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = spend.getText().toString();
                int val = Integer.parseInt(str.trim());
                MyDbHandler db = new MyDbHandler(getContext());
                Transaction cost = new Transaction();
                cost.setReason(spin[0]);
                cost.setAmount(val);
                cost.setDate(dateString);
                cost.setTime(timeString);
                cost.setType("e");
                db.addTransaction(cost);
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Confirmation!")
                        .setContentText("Expense details saved successfully").show();
                Options fragment = new Options();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout1,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}