package com.example.samy.firebaseuiexample2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity  {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("Notebook");
    RecyclerView recyclerView;
    NotFireRecyclerAdapter notFireRecyclerAdapter;
    FloatingActionButton floatingActionButton;
    private final int ADD_NOTE_REQUEST=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_view);
        floatingActionButton=findViewById(R.id.floating_action);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NewNoteActivity.class);
                startActivity(intent);
            }
        });
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        recyclerView=findViewById(R.id.recycler_view);
        Query query = notebookRef.orderBy("priority",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        notFireRecyclerAdapter=new NotFireRecyclerAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notFireRecyclerAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                notFireRecyclerAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        notFireRecyclerAdapter.setOnNoteClickListener(new NotFireRecyclerAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                String id=documentSnapshot.getId();
                String path=documentSnapshot.getReference().getPath();
                Toast.makeText(getApplicationContext(),"Position "+position+" Title: "+note.getTitle(),Toast.LENGTH_LONG).show();
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==ADD_NOTE_REQUEST&&resultCode==RESULT_OK){
//            String title = data.getStringExtra(NewNoteActivity.EXTRA_NOTE_TITLE);
//            String desc=data.getStringExtra(NewNoteActivity.EXTRA_NOTE_DESC);
//            int priority=data.getIntExtra(NewNoteActivity.EXTRA_NOTE_PRIORITY,1);
//            Note note=new Note(title,desc,priority);
//
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        notFireRecyclerAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        notFireRecyclerAdapter.startListening();
    }


}
