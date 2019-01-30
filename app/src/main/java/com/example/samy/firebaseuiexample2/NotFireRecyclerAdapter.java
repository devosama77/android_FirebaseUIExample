package com.example.samy.firebaseuiexample2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NotFireRecyclerAdapter extends FirestoreRecyclerAdapter<Note,NotFireRecyclerAdapter.NoteHolder> {
     OnNoteClickListener listener;
    public NotFireRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.note_layout,viewGroup,false);
        NoteHolder noteHolder=new NoteHolder(view);
        return noteHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
                   holder.titleText.setText(model.getTitle());
                   holder.descText.setText(model.getDescription());
                   String priority=String.valueOf(model.getPriority()) ;
                   holder.priorityText.setText(priority);
    }
    public void deleteItem(int position){
          getSnapshots().getSnapshot(position).getReference().delete();
    }
    public  class NoteHolder extends RecyclerView.ViewHolder{
        TextView titleText,descText,priorityText;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
           titleText= itemView.findViewById(R.id.text_view_title);
           descText=itemView.findViewById(R.id.text_view_description);
           priorityText=itemView.findViewById(R.id.text_view_priority);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position=getAdapterPosition();
                   if(position!=RecyclerView.NO_POSITION&&listener!=null){
                       listener.onNoteClick(getSnapshots().getSnapshot(position),position);
                   }
               }
           });
        }
    }

    public interface OnNoteClickListener{
         void onNoteClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener){
        this.listener=listener;
    }
}
