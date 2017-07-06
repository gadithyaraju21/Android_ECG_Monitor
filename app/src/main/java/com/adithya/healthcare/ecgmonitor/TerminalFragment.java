package com.adithya.healthcare.ecgmonitor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TerminalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;


    SharedPreferences sharedPref;
    private boolean isLogging = false;

    EditText name;
    EditText age;
    EditText contact;
    public String pname;
    public String page;
    public String pcontact;

    static TerminalFragment fragment;

    public static TerminalFragment newInstance(String param1, String param2) {
        fragment = new TerminalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TerminalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mFragmentView = inflater.inflate(R.layout.fragment_terminal, container, false);
        name=(EditText) mFragmentView.findViewById(R.id.name);
        age=(EditText) mFragmentView.findViewById(R.id.age);
        contact=(EditText) mFragmentView.findViewById(R.id.contact);
        final Button button = (Button) mFragmentView.findViewById(R.id.logButton);

        if(isLogging){
            button.setText("Stop Logging");
        }else{
            button.setText("Start Logging");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pname=name.getText().toString();
                page=age.getText().toString();
                pcontact=contact.getText().toString();

                if(!isLogging) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Enter Filename");
                        // Set up the input
                        final EditText input = new EditText(getActivity());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //start file logging
                                String fileName = input.getText().toString();
                                ((MainActivity) getActivity()).initFileWrite(fileName + ".txt");
                                ((MainActivity)getActivity()).writeToFile(pname);
                                ((MainActivity)getActivity()).writeToFile(page);
                                ((MainActivity)getActivity()).writeToFile(pcontact);
                                isLogging = !isLogging;
                                button.setText("Stop Logging");
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                }else{
                    ((MainActivity)getActivity()).stopFileWrite();
                    button.setText("Start Logging");
                    isLogging = !isLogging;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "The logfile is located in your external root folder.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return mFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
