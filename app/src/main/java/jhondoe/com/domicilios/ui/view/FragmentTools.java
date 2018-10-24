package jhondoe.com.domicilios.ui.view;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jhondoe.com.domicilios.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTools extends Fragment {


    public FragmentTools() {
        // Required empty public constructor
    }

    // Referencias UI
    private View view;
    Button btnStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tools, container, false);

        // Preparar elementos UI
        prepararUI();

        return view;
    }

    private void prepararUI() {
        btnStore = (Button)view.findViewById(R.id.btn_section);
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStore = new Intent(getActivity(), ActivityStore.class);
                startActivity(intentStore);
            }
        });

    }
}
