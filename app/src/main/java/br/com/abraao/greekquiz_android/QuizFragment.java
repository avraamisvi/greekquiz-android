package br.com.abraao.greekquiz_android;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    Question question;
    int questionId = 0;

    private RadioButton question1;
    private RadioButton question2;
    private RadioButton question3;
    private RadioButton question4;
    private RadioButton question5;

    public QuizFragment() {
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
        BufferedReader r = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(quiz)));
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
    }

    public void next() {
        questionId++;
        refreshView();
    }

    private void verify() {
        next();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        question1 = (RadioButton) view.findViewById(R.id.rb_question_1);
        question2 = (RadioButton) view.findViewById(R.id.rb_question_2);
        question3 = (RadioButton) view.findViewById(R.id.rb_question_3);
        question4 = (RadioButton) view.findViewById(R.id.rb_question_4);
        question5 = (RadioButton) view.findViewById(R.id.rb_question_5);


        Button btnResp = (Button) view.findViewById(R.id.btn_responder);

        btnResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

        try {
            setQuestion(questions.get(questionId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void setQuestion(Question question) throws JSONException {
        Random rd = new Random();
        int seq = rd.nextInt(4);

        question1.setText(answers.getJSONObject(question.patterns[seq][0]).getString("text"));
        question2.setText(answers.getJSONObject(question.patterns[seq][1]).getString("text"));
        question3.setText(answers.getJSONObject(question.patterns[seq][2]).getString("text"));
        question4.setText(answers.getJSONObject(question.patterns[seq][3]).getString("text"));
        question5.setText(answers.getJSONObject(question.patterns[seq][4]).getString("text"));

    }

    private void refreshView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}