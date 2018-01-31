package pl.fanfatal.swipecontrollerdemo;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

class PlayersDataAdapter extends RecyclerView.Adapter<PlayersDataAdapter.PlayerViewHolder> {
    public List<AccountBeneficaryList> accountBeneficaryLists;
    private PercentageChangeListener percentageChangeListener;

    public PlayersDataAdapter(List<AccountBeneficaryList> accountBeneficaryLists, PercentageChangeListener percentageChangeListener) {
        this.accountBeneficaryLists = accountBeneficaryLists;
        this.percentageChangeListener = percentageChangeListener;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_row, parent, false);

        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position) {
        final AccountBeneficaryList accountBeneficaryList = accountBeneficaryLists.get(position);
        holder.name.setText(accountBeneficaryList.name);
        holder.percentage.setText("" + (int) (Double.parseDouble(accountBeneficaryList.percentage.value) * 100));
        holder.id = position;

        holder.percentage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    holder.percentage.addTextChangedListener(holder.percentInputWatcher);
                else
                    holder.percentage.removeTextChangedListener(holder.percentInputWatcher);
            }
        });

    }

    @Override
    public int getItemCount() {
        return accountBeneficaryLists.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private EditText percentage;
        private int id;
        public TextWatcher percentInputWatcher = new TextWatcher() {
            String _tempString;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String newString = s.toString().trim();
                if (_tempString == null) {
                    _tempString = s.toString();
                }
                if (s != null && !newString.equals("") && newString != _tempString) {
                    AccountBeneficaryList accountBeneficaryList = accountBeneficaryLists.get(PlayerViewHolder.this.id);
                    //set % value because of over .88 fo
                    accountBeneficaryList.percentage.value = "" + ((Double.parseDouble("" + s) / 100));
                    percentageChangeListener.onPercentageChanged();
                    Log.d("aa", "afterTextChanged: ");
                }
            }
        };


        public PlayerViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            percentage = (EditText) view.findViewById(R.id.percentage);

        }
    }

}
