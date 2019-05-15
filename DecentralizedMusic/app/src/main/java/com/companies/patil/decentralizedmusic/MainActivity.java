package com.companies.patil.decentralizedmusic;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Music").child("Hindi");

    ArrayList<String> Songname = new ArrayList<>();
    ArrayList<String> songUrl = new ArrayList<>();

    ListView listView;

    boolean play=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView=findViewById(R.id.list_songs);

        final CustomAdapter customAdapter =new CustomAdapter(this,Songname,songUrl);
        listView.setAdapter(customAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Songname.add(postSnapshot.child("Name").getValue().toString());
                    songUrl.add(postSnapshot.child("Url").getValue().toString());
                }

                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                toastTop("Failed To Retrieve Data...");
            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                toastTop("Item Clicked..."+"\n"+songUrl.get(position));

                try {
                    PlayAudioManager.playAudio(MainActivity.this, songUrl.get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*try {
                    if(play)
                    {
                        PlayAudioManager.playAudio(MainActivity.this, songUrl.get(position));
                        play=false;
                    }
                    else
                    {
                        PlayAudioManager.killMediaPlayer();
                        play=true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }


    public void toastTop(String data)
    {
        Toast toast = Toast.makeText(MainActivity.this,data, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    class CustomAdapter extends BaseAdapter
    {
        Context context;
        ArrayList<String> SongNames,Songurl;
        private LayoutInflater inflater=null;

        CustomAdapter(Context con,ArrayList<String> s1, ArrayList<String> s2)
        {
            this.context=con;
            this.SongNames=s1;
            this.Songurl=s2;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return SongNames.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.songs_list_view, null);

            TextView Name=vi.findViewById(R.id.name);
            final Button playbut=vi. findViewById(R.id.playbut);

            playbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // toastTop("Button Clicked...");

                    try {
                        if(play)
                        {
                            PlayAudioManager.playAudio(MainActivity.this, songUrl.get(position));
                            playbut.setText("Resume");
                            play=false;
                        }
                        else
                        {
                            PlayAudioManager.killMediaPlayer();
                            playbut.setText("Play");
                            play=true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*try {
                        PlayAudioManager.playAudio(MainActivity.this, songUrl.get(position));
                    } catch (Exception e) {
                        toastTop("Failed To play Music..");
                        e.printStackTrace();
                    }*/

                                    }
            });
            Name.setText(SongNames.get(position));

            return vi;
        }
    }
}

