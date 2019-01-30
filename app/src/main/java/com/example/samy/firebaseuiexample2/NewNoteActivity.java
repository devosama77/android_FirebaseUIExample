package com.example.samy.firebaseuiexample2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_TITLE = "com.example.samy.firebaseuiexample2.title";
    public static final String EXTRA_NOTE_DESC = "com.example.samy.firebaseuiexample2.desc";
    public static final String EXTRA_NOTE_PRIORITY = "com.example.samy.firebaseuiexample2.priority";;
    EditText titleText,descText;
    NumberPicker priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        titleText=findViewById(R.id.edit_new_title);
        descText=findViewById(R.id.edit_new_description);
        priority=findViewById(R.id.number_picker);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        setTitle("Add Note");
        priority.setMinValue(0);
        priority.setMaxValue(10);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_id:{
                saveNote();
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String title=titleText.getText().toString().trim();
        String desc=descText.getText().toString().trim();
        int priorityValue= priority.getValue();
        if(title.isEmpty()||desc.isEmpty()){
            Toast.makeText(getApplicationContext()
                    ,"please add Title and Description",Toast.LENGTH_LONG).show();
                 return;
        }
        CollectionReference notebookRef = FirebaseFirestore.getInstance().collection("Notebook");
            notebookRef.add(new Note(title,desc,priorityValue));
            Toast.makeText(getApplicationContext(),"new note is added ",Toast.LENGTH_LONG).show();
//        Intent intent=new Intent();
//        intent.putExtra(EXTRA_NOTE_TITLE,title);
//        intent.putExtra(EXTRA_NOTE_DESC,desc);
//        intent.putExtra(EXTRA_NOTE_PRIORITY,priorityValue);
//        setResult(RESULT_OK,intent);
        finish();
    }

}
