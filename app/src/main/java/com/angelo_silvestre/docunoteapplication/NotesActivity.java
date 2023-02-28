package com.angelo_silvestre.docunoteapplication;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NotesActivity extends AppCompatActivity {
    FloatingActionButton vcreatenotesfab;
    Button logoutBtn;
    private FirebaseAuth firebaseAuth;

    RecyclerView vrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    BottomNavigationView bottomNavigationView;

    TextView vnoteContent, vuserNameTview, vuserEmailTview;

    RelativeLayout notes_container;
    CardView settings_container;

    SearchView searchView;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    private String userID;

    FirestoreRecyclerAdapter <FirebaseModel, NoteViewHolder> noteAdapter;
    Button gridViewBtn, listViewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


//        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        notes_container = findViewById(R.id.notes_container);
        settings_container = findViewById(R.id.settings_container);
        logoutBtn = findViewById(R.id.logout_btn);
        gridViewBtn = findViewById(R.id.grid_view_button);
        listViewBtn = findViewById(R.id.list_view_button);

        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();
        vnoteContent = findViewById(R.id.notecontent);

        vuserNameTview = findViewById(R.id.name_txtView);
        vuserEmailTview = findViewById(R.id.email_txtView);

        vcreatenotesfab = findViewById(R.id.createnotefab);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        userID = firebaseUser.getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        settings_container.setVisibility(View.GONE);


        getSupportActionBar().hide();


        vrecyclerview = findViewById(R.id.recyclerview);


//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, note_fragment).commit();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filterList(newText);
                return false;
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.dashboard:
                    viewVisibleAnimator(notes_container);
                    viewGoneAnimatortoBottom(settings_container);

                    break;
                case R.id.add_note:
                    startActivity(new Intent(NotesActivity.this, CreateNote.class));

                    break;
                case R.id.settings:
                    viewGoneAnimator(notes_container);
                    viewVisibleAnimator(settings_container);

                    break;
            }

            return false;
        });

        logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
            finish();

        });


        vcreatenotesfab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this, CreateNote.class));
            }
        });


        Query query = firebaseFirestore.collection("DocuNotes").document(firebaseUser.getUid()).collection("userNotes")
                .orderBy("creation_timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FirebaseModel> allusernotes = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query, FirebaseModel.class).build();

//        Log.d("NotesActivity", "COunt noteadapter" + noteAdapter.getItemCount());





        listViewBtn.setOnClickListener(v -> {
            listViewBtn.setVisibility(View.GONE);
            gridViewBtn.setVisibility(View.VISIBLE);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            vrecyclerview.setLayoutManager(staggeredGridLayoutManager);
            vrecyclerview.setAdapter(noteAdapter);

        });

        gridViewBtn.setOnClickListener(v -> {
            gridViewBtn.setVisibility(View.GONE);
            listViewBtn.setVisibility(View.VISIBLE);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            vrecyclerview.setLayoutManager(staggeredGridLayoutManager);
            vrecyclerview.setAdapter(noteAdapter);

        });

        noteAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allusernotes){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel firebasemodel){
                Log.d("NotesActivity", "COunt allusernotes" + allusernotes.getSnapshots().size());
                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.menupopbutton);
                int colourcode = getRandomColor(i);
                noteViewHolder.vnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));


                noteViewHolder.notetitle.setText(firebasemodel.getTitle());
                noteViewHolder.notecontent.setText((firebasemodel.getContent()));

                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), NoteDetails.class);
//                    noteViewHolder.vnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colourcode, null));

                    intent.putExtra("title", firebasemodel.getTitle());
                    intent.putExtra("content", firebasemodel.getContent());
                    intent.putExtra("noteId", docId);

                    v.getContext().startActivity(intent);
                    // Toast.makeText(getApplicationContext(), "This is clicked", Toast.LENGTH_SHORT).show();

                });

                popupbutton.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.setGravity(Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent = new Intent(v.getContext(), EditNote.class);

                            intent.putExtra("title", firebasemodel.getTitle());
                            intent.putExtra("content", firebasemodel.getTitle());
                            intent.putExtra("noteId", docId);

                            v.getContext().startActivity(intent);
                            return false;
                        }
                    });

                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                        //Toast.makeText(v.getContext(), "This note is deleted", Toast.LENGTH_SHORT).show();

                        DocumentReference documentReference = firebaseFirestore.collection("DocuNotes").document(firebaseUser.getUid()).collection("userNotes").document(docId);
                        documentReference.delete().addOnSuccessListener(aVoid -> {
                            Toast.makeText(v.getContext(), "This note is deleted", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(v.getContext(), "This note is deleted", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> Toast.makeText(v.getContext(), "Failed To Delete", Toast.LENGTH_SHORT).show());

                        return false;
                    });

                    popupMenu.show();
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };


        vrecyclerview.setItemAnimator(null);

        vrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        vrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        vrecyclerview.setAdapter(noteAdapter);


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile fetchUser = snapshot.getValue(UserProfile.class);

                if(fetchUser != null){
                    Log.d("UserCredentials", "User Name: " + fetchUser.getName());
                    Log.d("UserCredentials", "User Email: " + fetchUser.getEmail());

                    vuserNameTview.setText(fetchUser.getName());
                    vuserEmailTview.setText(fetchUser.getEmail());  

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotesActivity.this, "Database Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void filterList(String newText) {

        Query query = firebaseFirestore.collection("DocuNotes")
                .document(firebaseUser.getUid()).collection("userNotes")
                .orderBy("title", Query.Direction.ASCENDING)
                .startAt(newText).endAt(newText + "\uf8ff");
        FirestoreRecyclerOptions<FirebaseModel> allusernotes= new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query, FirebaseModel.class).build();

        noteAdapter.updateOptions(allusernotes);
        noteAdapter.startListening();
        vrecyclerview.setAdapter(noteAdapter);
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView notetitle;
        private TextView notecontent;
        LinearLayout vnote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            vnote = itemView.findViewById(R.id.note);
        }
    }

    private void viewGoneAnimator(final View view) {

        view.animate()
                .alpha(0f).translationY(-(view.getHeight()))
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }
    private void viewGoneAnimatortoBottom(final View view) {

        view.animate()
                .alpha(0f).translationY((view.getHeight()))
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    private void viewVisibleAnimator(final View view) {

        view.animate()
                .alpha(1f).translationY(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item){
//        switch(item.getItemId()){
//            case R.id.logout:
//                firebaseAuth.signOut();
//                finish();
//                startActivity(new Intent(NotesActivity.this, MainActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onStart(){
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }


    private int getRandomColor(int num){

        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.lightgreen);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.lightyellow);
        colorcode.add(R.color.orange);
        colorcode.add(R.color.cyan);

//        colorcode.add(R.color.color1);
//        colorcode.add(R.color.color2);
//        colorcode.add(R.color.color3);
//        colorcode.add(R.color.color4);
//        colorcode.add(R.color.color5);
//        colorcode.add(R.color.green);

        if(num >=colorcode.size()){
            num = num % colorcode.size();
        }


        Random random = new Random();
        int number = random.nextInt(colorcode.size());

        return colorcode.get(num);

    }

}