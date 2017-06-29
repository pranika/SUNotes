package com.example.kupal.sunotes.OtherUtilties;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kupal.sunotes.BusTimingPackage.BusData;
import com.example.kupal.sunotes.R;

import java.util.HashMap;

public class Fragment_DetailView extends Fragment {
    BusData busObj= new BusData();

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;

    public static Fragment_DetailView newInstance(HashMap<String,?> bus) {
        Fragment_DetailView fragment = new Fragment_DetailView();
        Bundle args = new Bundle();
        args.putSerializable("Bus", bus);
        // args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_DetailView() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootView=null;
        HashMap<String,?> bus= (HashMap<String,?>) getArguments().getSerializable("Bus");

        View view = inflater.inflate(R.layout.fragment__detail_view, container, false);
        TextView busName = (TextView) view.findViewById(R.id.detailBusName);
        ImageView busImage = (ImageView) view.findViewById(R.id.detailImage);
        int imgId= (Integer)bus.get("image");
        busImage.setImageResource(imgId);

        String busDetailName= (String)bus.get("name");
        busName.setText(busDetailName);

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
