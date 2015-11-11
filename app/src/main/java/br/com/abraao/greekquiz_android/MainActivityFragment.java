package br.com.abraao.greekquiz_android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.btn_pres_indicativo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuiz("pres_indicativo.dat");
            }
        });

        view.findViewById(R.id.btn_pres_subjuntivo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuiz("pres_indicativo.dat");
            }
        });

        view.findViewById(R.id.btn_vocabulario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuiz("translate_1.dat");
            }
        });

        return view;
    }

    private void openQuiz(String quiz) {
        FragmentManager fragmentManager = MainActivityFragment.this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.detach(MainActivityFragment.this);

        QuizFragment frag = new QuizFragment();
        frag.setAsset(getActivity().getAssets());
        frag.setQuiz(quiz);

        //fragmentTransaction.attach(frag);
        fragmentTransaction.replace(R.id.main_frame_layout, frag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
