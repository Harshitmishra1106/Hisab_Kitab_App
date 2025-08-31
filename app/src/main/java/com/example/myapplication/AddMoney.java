package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.data.MyDbHandler;
import com.example.myapplication.databinding.FragmentAddMoneyBinding;
import com.example.myapplication.model.Transaction;

import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMoney#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMoney extends Fragment {
    private FragmentAddMoneyBinding binding;
    public AddMoney() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMoney.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMoney newInstance(String param1, String param2) {
        AddMoney fragment = new AddMoney();
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
        binding = FragmentAddMoneyBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String timeString = String.format(Locale.getDefault(),"%02d:%02d", hour, minute);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = String.format(Locale.getDefault(),"%02d/%02d/%02d", year, month, day);
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = binding.saved.getText().toString();
                int val = Integer.parseInt(str.trim());
                MyDbHandler db = new MyDbHandler(getContext());
                Transaction save = new Transaction();
                save.setReason("Money Credited");
                save.setAmount(val);
                save.setDate(dateString);
                save.setTime(timeString);
                save.setType("s");
                db.addTransaction(save);
                Options fragment = new Options();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout1, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Confirmation!")
                        .setContentText("Savings details saved successfully").show();
            }
        });
    }
}