package br.com.abraao.greekquiz_android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    int MENU_PRINCIPAL = 0;
    int MENU_VERBOS = 1;

    int menu = 0;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = null;

        if(menu == MENU_PRINCIPAL) {
            view = inflater.inflate(R.layout.fragment_main, container, false);

            view.findViewById(R.id.btn_pres_indicativo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMenuVerbos();
                }
            });

            view.findViewById(R.id.btn_vocabulario).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("vocabulario.dat");
                }
            });

            view.findViewById(R.id.btn_substantivos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("substantivos.dat");
                }
            });

            view.findViewById(R.id.btnAdjetivos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("adjetivos.dat");
                }
            });

            view.findViewById(R.id.btnPronomes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("pronomes.dat");
                }
            });

        } else if(menu == MENU_VERBOS) {
            view = inflater.inflate(R.layout.fragment_verbos, container, false);

            view.findViewById(R.id.btn_pres_indicativo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("pres_indicativo.dat");
                }
            });

            view.findViewById(R.id.btn_pres_subjuntivo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("pres_subjuntivo.dat");
                }
            });

            view.findViewById(R.id.btn_fut_indicativo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("fut_indicativo.dat");
                }
            });

            view.findViewById(R.id.btn_imperfeito_indicativo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("imperfeito_indicativo.dat");
                }
            });

            view.findViewById(R.id.btn_mais_perfeito).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("mais_perfeito.dat");
                }
            });

            view.findViewById(R.id.btn_imperativo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("imperativo.dat");
                }
            });

            view.findViewById(R.id.btn_aoristo_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("aoristo.dat");
                }
            });

            view.findViewById(R.id.btn_participio).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("participio.dat");
                }
            });

            view.findViewById(R.id.btn_perfeito).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("perfeito.dat");
                }
            });

            view.findViewById(R.id.btn_optativo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("optativo.dat");
                }
            });

            view.findViewById(R.id.btn_infinitivo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("infinitivo.dat");
                }
            });

            view.findViewById(R.id.btn_conjug_mi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuiz("conjug_mi.dat");
                }
            });
        }

        return view;
    }

    private void openMenuVerbos() {
        menu = MENU_VERBOS;
        FragmentManager fragmentManager = MainActivityFragment.this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(MainActivityFragment.this);
        fragmentTransaction.attach(MainActivityFragment.this);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
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
