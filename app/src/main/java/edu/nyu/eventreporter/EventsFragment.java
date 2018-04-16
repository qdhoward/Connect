package edu.nyu.eventreporter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.nyu.eventreporter.entity.Event;
import edu.nyu.eventreporter.entity.Like;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {
    private ImageView mImageViewAdd;
    private RecyclerView recyclerView;
    private EventListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference database;
    private List<Event> events;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        mImageViewAdd = (ImageView) view.findViewById(R.id.img_event_add);

        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventReportIntent = new Intent(getActivity(), EventReportActivity.class);
                startActivity(eventReportIntent);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        setAdapter();

        return view;

    }


    /**
     * Set adapter for recycler view to show all events
     */
    public void setAdapter() {
        events = new ArrayList<Event>();
        final Set<String> likedEvent = new HashSet<>();
        database.child("like").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Like like = noteDataSnapshot.getValue(Like.class);
                    if (like.getUserId().equals(Utils.username)) {
                        likedEvent.add(like.getEventId());
                    }
                }
                Utils.likedEvent = likedEvent;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: do something
            }
        });

        database.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Event event = noteDataSnapshot.getValue(Event.class);
                    events.add(event);
                }
                mAdapter = new EventListAdapter(events,getActivity());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: do something
            }
        });
    }
}
