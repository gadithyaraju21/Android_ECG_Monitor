package com.adithya.healthcare.ecgmonitor;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;



public class GraphFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View myFragmentView;
    TextView hrt;
    TextView diag;

    private GraphView graph;
    static GraphFragment fragment;
    SharedPreferences sharedPref;





    public static GraphFragment newInstance(String param1, String param2) {
        fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean isGraphOpen(){
        return myFragmentView != null && this.isVisible();
    }

    public GraphFragment() {
        // Required empty public constructor
    }
    public void setvalue(Float val){
        hrt= (TextView) myFragmentView.findViewById(R.id.hrt);
        String s=Float.toString(val);
        hrt.setText(s);

    }
    public void settext(Float val){
        diag=(TextView)myFragmentView.findViewById(R.id.diag);
        if(val>60&&val<100){
            diag.setText("      Normal Heart Rhythm");

        }
        else if (val>100 && val<120){
            diag.setText("      Higher heart rate detected");
        }
        else if(val<60)
        {
            diag.setText("      Bradycardia Detected");
        }
        else
            diag.setText("      Tachycardia detected");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        boolean isNull = graph==null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    public void initialize(LineGraphSeries<DataPoint>[] series){
        graph = (GraphView) myFragmentView.findViewById(R.id.graphView);
        float tmp;
        if(!sharedPref.getBoolean("pref_yAutoScale", true)){
            graph.getViewport().setYAxisBoundsManual(true);
            tmp = Float.parseFloat(sharedPref.getString("pref_y_min", "-200"));
            graph.getViewport().setMinY(tmp);
            tmp = Float.parseFloat(sharedPref.getString("pref_y_max", "200"));
            graph.getViewport().setMaxY(tmp);
        }
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        tmp = Integer.parseInt(sharedPref.getString("pref_windowSize", "200"));
        graph.getViewport().setMaxX(tmp);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(sharedPref.getBoolean("pref_xgridlabels", true));//pref_ygridlabels
        graph.getGridLabelRenderer().setVerticalLabelsVisible(sharedPref.getBoolean("pref_ygridlabels", true));
        graph.setTitle("ECG PLOT");
        int i =0;
        for (LineGraphSeries<DataPoint> sery : series) {
            if (sery != null) {
                sery.setColor(getColor(i));
                graph.addSeries(sery);
            }
            i++;
        }
    }

    public  void setcolor(Float val)
    {
        if(val<60 && val!=0)
        graph.setBackgroundColor(getColor(1));
        else if(val>80 && val!=0)
            graph.setBackgroundColor(getColor(1));
        else
            graph.setBackgroundColor(getColor(4));

    }
    private int getColor(int ctr){
        switch(ctr){
            case 0:
                return Color.GREEN;
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.WHITE;
            case 5:
                return 0xffbb33;
            default:
                return Color.MAGENTA;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_graph, container, false);

        return myFragmentView;
    }


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
