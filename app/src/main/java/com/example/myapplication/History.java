package com.example.myapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.data.MyDbHandler;
import com.example.myapplication.databinding.FragmentHistoryBinding;
import com.example.myapplication.model.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
    private FragmentHistoryBinding binding;

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
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerView.measure(
                        View.MeasureSpec.makeMeasureSpec(binding.recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                int width = binding.recyclerView.getMeasuredWidth();
                int height = binding.recyclerView.getMeasuredHeight();

                // Create PDF
                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                binding.recyclerView.draw(canvas);
                document.finishPage(page);

                String fileName = "Expense_History.pdf";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Use MediaStore for Android 10 and above
                    savePdfUsingMediaStore(document, fileName);
                } else {
                    // Use File API for Android 9 and below
                    if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(),
                            "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                        return;
                    }
                    savePdfUsingFileAPI(document, fileName);
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.Q)
            private void savePdfUsingMediaStore(PdfDocument document, String fileName) {
                ContentResolver resolver = binding.getRoot().getContext().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.Downloads.IS_PENDING, 1);

                Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                Uri fileUri = resolver.insert(collection, contentValues);

                try {
                    OutputStream out = resolver.openOutputStream(fileUri);
                    document.writeTo(out);
                    document.close();
                    out.close();

                    contentValues.clear();
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0);
                    resolver.update(fileUri, contentValues, null, null);

                    Toast.makeText(getContext(), "PDF saved to Downloads!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error saving PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            private void savePdfUsingFileAPI(PdfDocument document, String fileName) {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(downloadsDir, fileName);

                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    document.writeTo(fos);
                    document.close();
                    fos.close();

                    Toast.makeText(getContext(), "PDF saved to Downloads!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error saving PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}