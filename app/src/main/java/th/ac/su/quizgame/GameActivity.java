package th.ac.su.quizgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import th.ac.su.quizgame.model.WordItem;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mQuestionImageView;
    private Button[] mButtons = new Button[4];
    private TextView mScore;
    int score = 0;
    int count = 0;
    String strScore = "0 คะแนน";
    private String mAnswerWord;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionImageView = findViewById(R.id.question_images_view);
        mButtons[0] = findViewById(R.id.choice_1_button);
        mButtons[1] = findViewById(R.id.choice_2_button);
        mButtons[2] = findViewById(R.id.choice_3_button);
        mButtons[3] = findViewById(R.id.choice_4_button);

        mButtons[0].setOnClickListener(this);
        mButtons[1].setOnClickListener(this);
        mButtons[2].setOnClickListener(this);
        mButtons[3].setOnClickListener(this);

        mRandom = new Random();
        mScore = findViewById(R.id.score_text_view);
        mScore.setText(strScore);
        newQuiz();
    }

    private void newQuiz() {
        List<WordItem> mItemList = new ArrayList<>(Arrays.asList(WordListActivity.items));

        // สุ่ม index ของคำตอบ (คำถาม)
        int answerIndex = mRandom.nextInt(mItemList.size());
        // เข้าถึง WordItem ตาม index ที่สุ่มได้
        WordItem item = mItemList.get(answerIndex);
        // แสดงรูปคำถาม
        mQuestionImageView.setImageResource(item.imageResId);

        mAnswerWord = item.word;

        // สุ่มตำแหน่งปุ่มที่จะแสดงคำตอบ
        int randomButton = mRandom.nextInt(4);
        // แสดงคำศัพท์ที่เป็นคำตอบ
        mButtons[randomButton].setText(item.word);
        // ลบ WordItem ที่เป็นคำตอบออกจาก list
        mItemList.remove(item);

        // เอา list ที่เหลือมา shuffle
        Collections.shuffle(mItemList);

        // เอาคำศัพท์จาก list ที่ตำแหน่ง 0 ถึง 3 มาแสดงที่ปุ่ม 3 ปุ่มที่เหลือ โดยข้ามปุ่มที่เป็นคำตอบ
        for (int i = 0; i < 4; i++) {
            if (i == randomButton) { // ถ้า i คือ index ของปุ่มคำตอบ ให้ข้ามไป
                continue;
            }
            mButtons[i].setText(mItemList.get(i).word);
        }
    }


    @Override
    public void onClick(View view) {
       Button  b = findViewById(view.getId());
       String ButtonText =  b.getText().toString();
       if(mAnswerWord.equals(ButtonText)){
           Toast.makeText(GameActivity.this , "Correct", Toast.LENGTH_SHORT).show();
            score++;
            String str = String.valueOf(score) + " คะแนน";
            mScore.setText(str);
       }else{
           Toast.makeText(GameActivity.this , "Incorrect" , Toast.LENGTH_SHORT).show();
       }
       count++;
       if(count == 5){
           AlertDialog.Builder dialog = new AlertDialog.Builder(GameActivity.this);
           dialog.setTitle("สรุปผล");
           dialog.setMessage("คุณได้ " + String.valueOf(score) + " คะแนน" +"\n\n" + "ต้องการเล่นเกมต่อหรือไม่");
           dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   score = 0;
                   count = 0;
                   newQuiz();
                   String str = String.valueOf(score) + " คะแนน";
                   mScore.setText(str);
               }
           });
           dialog.setNegativeButton("NO" , new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(GameActivity.this , MainActivity.class);
                    startActivity(in);
               }
           });
           dialog.show();
       }else{
           newQuiz();
       }

    }
}