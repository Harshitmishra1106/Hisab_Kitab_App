package com.example.myapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.data.MyDbHandler;
import com.example.myapplication.model.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton download = view.findViewById(R.id.download);
        ArrayList<String> purpose,amount,date,time;
        purpose=new ArrayList<>();
        amount=new ArrayList<>();
        date=new ArrayList<>();
        time=new ArrayList<>();
        MyDbHandler db;
        MyListAdapter adapter;
        db = new MyDbHandler(this.getContext());
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) db.getAllTransactions();
        for(Transaction transaction : transactions){
            purpose.add(transaction.getReason());
            amount.add(String.valueOf(transaction.getAmount())); // Convert int or double to String
            date.add(transaction.getDate());
            time.add(transaction.getTime());
        }
        adapter = new MyListAdapter(this.getActivity(),purpose,amount,date,time);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = inflater.inflate(R.layout.fragment_history, container, false);
                DisplayMetrics displayMetrics = new DisplayMetrics();

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                    getActivity().getDisplay().getRealMetrics(displayMetrics);
                } else getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels,View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels,View.MeasureSpec.EXACTLY));


                PdfDocument document = new PdfDocument();

                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMinimumHeight();
                view.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels);

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight,1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                view.draw(canvas);

                document.finishPage(page);
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String fileName = "exampleXML.pdf";
                File file = new File(downloadsDir, fileName);
                try{
                    FileOutputStream fos = new FileOutputStream(file);
                    document.writeTo(fos);
                    document.close();
                    fos.close();
                    Toast.makeText(getContext(), "Written Successfully!!!", Toast.LENGTH_SHORT).show();

                }
                catch (FileNotFoundException e){
                    Log.d("myLog","Error while writing "+e.toString());
                    throw  new RuntimeException(e);
                }
                catch (IOException e){
                    throw  new RuntimeException(e);
                }
            }
        });
        return view;
    }
}