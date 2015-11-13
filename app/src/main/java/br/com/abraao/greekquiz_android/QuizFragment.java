package br.com.abraao.greekquiz_android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizFragment extends Fragment {

    String quiz;
    List<Question> questions;
    JSONArray answers;
    boolean result = false;
    boolean resultFinal = false;
    boolean success = false;

    Question question;
    int questionId = 0;
    int sequence = 0;
    int answered = 0;

    int rights = 0;

    int questionsSequence[][];
    int questionSequenceSelected = 0;

    private RadioButton question1;
    private RadioButton question2;
    private RadioButton question3;
    private RadioButton question4;
    private RadioButton question5;

    AssetManager assetMng;
    private TextView textQuestion;

    public QuizFragment() {
    }

    public void setAsset(AssetManager assetMng) {
        this.assetMng = assetMng;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;

        try {
            //JsonReader jsonr = new JsonReader(new InputStreamReader(getActivity().getAssets().open(quiz)));
            parseQuiz(null);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void parseQuiz(JsonReader jsonr) throws IOException, JSONException {
        BufferedReader r = new BufferedReader(new InputStreamReader(assetMng.open(quiz)));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }

        questions = new ArrayList<Question>();

        JSONObject jsonob = new JSONObject(total.toString());
        JSONArray qts = jsonob.getJSONArray("questions");
        answers = jsonob.getJSONArray("answers");

        for (int i = 0; i < qts.length(); i++) {
            JSONObject qt = qts.getJSONObject(i);

            Question q = new Question();
            q.title = qt.getString("title");
            q.correct = qt.getInt("correct");
            q.explanation = qt.getString("explanation");
            q.patterns = new int[3][5];

            JSONArray arr = qt.getJSONArray("patterns");

            for(int ia = 0; ia < arr.length(); ia++) {
                JSONArray arrb = arr.getJSONArray(ia);
                for(int ib = 0; ib < arrb.length(); ib++) {
                    q.patterns[ia][ib] = arrb.getInt(ib);
                }
            }

            questions.add(q);
        }

        questionsSequence = new int[3][questions.size()];

        Random rand = new Random();

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < questions.size(); j++) {
                questionsSequence[i][j] = rand.nextInt(questions.size());
            }
        }

        questionSequenceSelected = rand.nextInt(3);
    }

    int getQuestionID() {
        return questionsSequence[questionSequenceSelected][questionId];
    }

    public void next() {
        questionId++;
        answered++;
        if(questionId >= questions.size()) {
            resultFinal = true;
            result = false;
            questionId = 0;
            success = false;
        } else {
            result = false;
            success = false;
        }
        refreshView();
    }

    private void verify() {

        result = true;

        int [] pat = question.patterns[sequence];

        if(question1.isChecked()) {
            success = pat[0] == question.correct;
        } else if(question2.isChecked()) {
            success = pat[1] == question.correct;
        } else if(question3.isChecked()) {
            success = pat[2] == question.correct;
        } else if(question4.isChecked()) {
            success = pat[3] == question.correct;
        } else if(question5.isChecked()) {
            success = pat[4] == question.correct;
        }

        if(success) {
            rights++;
        }

        refreshView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = null;

        if(resultFinal) {
            view = inflater.inflate(R.layout.fragment_result_total, container, false);

            TextView txtResult = (TextView) view.findViewById(R.id.txt_final_result);
            TextView txtRgt = (TextView) view.findViewById(R.id.txt_total_right);
            TextView txtWrg = (TextView) view.findViewById(R.id.txt_total_wrong);
            ImageView imgType = (ImageView) view.findViewById(R.id.img_result_total);

            float perc = ((float) rights / (float) answered) * 100;
            String percStr = new DecimalFormat("###.##").format(perc);

            if(perc>=70) {
                txtResult.setText("Parabéns! Você acertou:" + percStr + "%");
                imgType.setImageResource(R.mipmap.right);
            } else if(perc>=50) {
                txtResult.setText("Parabéns! Você acertou:" + percStr + "%");
                imgType.setImageResource(R.mipmap.warning);
            } else {
                txtResult.setText("Que pena! Você acertou:" + percStr + "%");
                imgType.setImageResource(R.mipmap.wrong);
            }

            txtRgt.setText("Total de acertos:" + rights);
            txtWrg.setText("Total de erros:" + (answered-rights));

            Button btnOK = (Button) view.findViewById(R.id.btn_ok_final);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu();
                }
            });

        } else if(!result) {
            view = inflater.inflate(R.layout.fragment_quiz, container, false);

            textQuestion = (TextView) view.findViewById(R.id.txt_question);
            question1 = (RadioButton) view.findViewById(R.id.rb_question_1);
            question2 = (RadioButton) view.findViewById(R.id.rb_question_2);
            question3 = (RadioButton) view.findViewById(R.id.rb_question_3);
            question4 = (RadioButton) view.findViewById(R.id.rb_question_4);
            question5 = (RadioButton) view.findViewById(R.id.rb_question_5);

            //question1.setSelected(true);

            Button btnResp = (Button) view.findViewById(R.id.btn_responder);

            btnResp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verify();
                }
            });

            Button btnFinalizar = (Button) view.findViewById(R.id.btn_finalizar_quiz);
            btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionId = questions.size()+1;
                    verify();
                }
            });

            try {
                setQuestion(questions.get(getQuestionID()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//            result = false;
            view = inflater.inflate(R.layout.fragment_result, container, false);

            TextView txtResult = (TextView) view.findViewById(R.id.txt_result);
            TextView txtExp = (TextView) view.findViewById(R.id.txt_explanation);
            ImageView imgType = (ImageView) view.findViewById(R.id.img_view_type_answer);

            if(success) {
                txtResult.setText("Resposta certa! Parabéns!");
                //txtResult.setBackgroundColor(0xFF88CC88);
                imgType.setImageResource(R.mipmap.right);
            } else {
                txtResult.setText("Resposta Errada!");
                //txtResult.setBackgroundColor(0xFFFFAAAA);
                imgType.setImageResource(R.mipmap.wrong);
            }

            txtExp.setText(question.explanation);

            Button btnOK = (Button) view.findViewById(R.id.btn_ok);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    next();
                }
            });
        }
        return view;
    }

    private void setQuestion(Question question) throws JSONException {
        Random rd = new Random();
        int seq = rd.nextInt(3);

        this.question = question;

        textQuestion.setText(question.title);

        question5.setText("");

        try {
            question1.setText(answers.getJSONObject(question.patterns[seq][0]).getString("text"));
            question2.setText(answers.getJSONObject(question.patterns[seq][1]).getString("text"));
            question3.setText(answers.getJSONObject(question.patterns[seq][2]).getString("text"));
            question4.setText(answers.getJSONObject(question.patterns[seq][3]).getString("text"));
            question5.setText(answers.getJSONObject(question.patterns[seq][4]).getString("text"));
        } catch (Throwable thr) {
            thr.printStackTrace();
        }

        sequence = seq;
    }

    private void refreshView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void showMenu() {
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.detach(QuizFragment.this);

        MainActivityFragment frag = new MainActivityFragment();

        fragmentTransaction.replace(R.id.main_frame_layout, frag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
