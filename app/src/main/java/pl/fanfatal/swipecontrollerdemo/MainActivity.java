package pl.fanfatal.swipecontrollerdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private Button button = null;
    private RecyclerView recyclerView = null;
    private TextView allocationText = null;


    //Temp counter for adding new row after add new button click
    private int newRowcounter = 0;

    // This listener will get called from allocation input field text watcher
    private PercentageChangeListener percentageChangeListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        equalCheckbox = (CheckBox) findViewById(R.id.equalCheckBox);
        button = (Button) findViewById(R.id.mbutton);
        allocationText = (TextView) findViewById(R.id.allocationText);

        equalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateEqualPercentage();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });

        percentageChangeListener = new PercentageChangeListener() {
            @Override
            public void onPercentageChanged() {
                updateProgressWithAllocationText();
            }
        };


        setPlayersDataAdapter();
        setupRecyclerView();
        updateProgressWithAllocationText();

    }


    private void setPlayersDataAdapter() {
        List<AccountBeneficaryList> accountBeneficaryLists = new ArrayList<>();
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("players.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            String[] st;
            while ((line = reader.readLine()) != null) {
                st = line.split(",");
                AccountBeneficaryList accountBeneficaryListElement = new AccountBeneficaryList();
                accountBeneficaryListElement.name = (st[0]);
                accountBeneficaryListElement.percentage = new Percentage();
                accountBeneficaryListElement.percentage.value = "0.20";
                accountBeneficaryLists.add(accountBeneficaryListElement);
            }
        } catch (IOException e) {

        }

        mAdapter = new PlayersDataAdapter(accountBeneficaryLists, percentageChangeListener);

    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

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

    private void addNewItem() {

        int position = 0;


        AccountBeneficaryList accountBeneficaryList = new AccountBeneficaryList();
        accountBeneficaryList.percentage = new Percentage();
        accountBeneficaryList.percentage.value = "0";
        String itemLabel = "new name " + newRowcounter;
        newRowcounter++;
        accountBeneficaryList.name = itemLabel;

        mAdapter.accountBeneficaryLists.add(position, accountBeneficaryList);
        mAdapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(position);

        //un select divide equal checkbox on addition of any item
        equalCheckbox.setChecked(false);
        updateProgressWithAllocationText();

    }

    private void removeAndUpdateProgress(int position) {

        mAdapter.accountBeneficaryLists.remove(position);
        mAdapter.notifyDataSetChanged();
        updateProgressWithAllocationText();
        //un select divide equal checkbox on removal of any item
        equalCheckbox.setChecked(false);
    }

    private void updateEqualPercentage() {

        int totalPlayer = mAdapter.accountBeneficaryLists.size();
        int remainingPercentage = 100 % totalPlayer;
        int percentage = (100 - remainingPercentage) / totalPlayer;

        for (AccountBeneficaryList accountBeneficaryList : mAdapter.accountBeneficaryLists) {
            //
            accountBeneficaryList.percentage.value = "" + (percentage * .01);
            if (remainingPercentage > 0) {
                accountBeneficaryList.percentage.value = "" + (Double.parseDouble(accountBeneficaryList.percentage.value) + 0.01);
                remainingPercentage--;
            }
        }
        updateProgressWithAllocationText();
        mAdapter.notifyDataSetChanged();
    }

    private void updateProgressWithAllocationText() {
        int total = 0;
        for (AccountBeneficaryList accountBeneficaryList : mAdapter.accountBeneficaryLists) {
            // convert percentage value .0088 to normal number 88
            total = total + (int) Math.round(Double.parseDouble(accountBeneficaryList.percentage.value) * 100.00);
        }

        if (total <= 100) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bene_alloc_progressbar);
            progressBar.setProgressDrawable(drawable);
            progressBar.setProgress(total);
            allocationText.setTextColor(Color.parseColor("#727272"));
            if (total < 100)
                allocationText.setText((100 - total) + "% remaining");
            else
                allocationText.setText("100% allocated");


        } else {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bene_alloc_progressbar_error);
            progressBar.setProgressDrawable(drawable);
            progressBar.setProgress(total);
            allocationText.setText("");
            allocationText.setTextColor(Color.parseColor("#FF0000"));
            allocationText.setText(total + "% allocation must equal to 100%");
        }

    }

}
