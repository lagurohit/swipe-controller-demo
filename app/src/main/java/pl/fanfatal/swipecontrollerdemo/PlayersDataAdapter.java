package pl.fanfatal.swipecontrollerdemo;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

class PlayersDataAdapter extends RecyclerView.Adapter<PlayersDataAdapter.PlayerViewHolder> {
    public List<Player> players;
    private PercentageChangeListener percentageChangeListener;

    public PlayersDataAdapter(List<Player> players, PercentageChangeListener percentageChangeListener) {
        this.players = players;
        this.percentageChangeListener = percentageChangeListener;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_row, parent, false);

        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.name.setText(player.name);
        holder.percentage.setText("" + player.percentage);
        final int pos = position;
        holder.percentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().equals("") && s != null) {
                    Player player = players.get(pos);
                    player.percentage = Integer.parseInt("" + s);
                    percentageChangeListener.onPercentageChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private EditText percentage;

        public PlayerViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            percentage = (EditText) view.findViewById(R.id.percentage);

        }
    }

}
