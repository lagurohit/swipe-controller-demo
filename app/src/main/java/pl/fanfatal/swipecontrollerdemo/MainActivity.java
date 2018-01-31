package pl.fanfatal.swipecontrollerdemo;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SwipeController swipeController = null;
    private PlayersDataAdapter mAdapter;
    private ProgressBar progressBar = null;
    private CheckBox equalCheckbox = null;
    private PercentageChangeListener percentageChangeListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        equalCheckbox = (CheckBox) findViewById(R.id.equalCheckBox);
        equalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateEqualPercentage();
                }
            }
        });

        percentageChangeListener = new PercentageChangeListener() {
            @Override
            public void onPercentageChanged() {
                updateProgressBar();
            }
        };


        setPlayersDataAdapter();
        setupRecyclerView();


    }

    private void updateEqualPercentage() {
        int totalPlayer = mAdapter.getItemCount();
        int extra = 100 % totalPlayer;
        int percentage = (100 - extra) / totalPlayer;

        for (Player player : mAdapter.players) {
            player.percentage = percentage;
            if (extra > 0) {
                player.percentage++;
                extra--;
            }
        }
        updateProgressBar();
        mAdapter.notifyDataSetChanged();
    }

    private void updateProgressBar() {
        int total = 0;
        for (Player player : mAdapter.players) {
            total = total + player.percentage;
        }

        if (total <= 100) {
            progressBar.setProgress(total);
        } else {
        }

    }

    private void setPlayersDataAdapter() {
        List<Player> players = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("players.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            String[] st;
            while ((line = reader.readLine()) != null) {
                st = line.split(",");
                Player player = new Player();
                player.name = (st[0]);
                player.percentage = (Integer.parseInt(st[14]));
                players.add(player);
            }
        } catch (IOException e) {

        }

        mAdapter = new PlayersDataAdapter(players, percentageChangeListener);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                removeAndUpdateProgress(position);
            }

            @Override
            public void onLeftClicked(int position) {
                removeAndUpdateProgress(position);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void removeAndUpdateProgress(int position) {
        mAdapter.players.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
        updateProgressBar();
    }





}
