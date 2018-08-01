package vlad.mihai.com.speedruns;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vlad.mihai.com.speedruns.model.Leaderboard;
import vlad.mihai.com.speedruns.model.RunPlace;

/**
 * Created by Vlad
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardAdapterViewHolder> {

    private List<Leaderboard> leaderboardList;

    private final LeaderboardAdapter.LeaderboardAdapterOnClickHandler clickHandler;
    private Context context;

    public LeaderboardAdapter (Context context, LeaderboardAdapter.LeaderboardAdapterOnClickHandler clickHandler){
        this.clickHandler= clickHandler;
        this.context = context;
    }

    public interface LeaderboardAdapterOnClickHandler{
        void onClick(Leaderboard currentLeaderboard);
    }

    public class LeaderboardAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView categoryTitle;
        public final TextView firstRun;
        public final TextView secondRun;
        public final TextView thirdRun;

        public LeaderboardAdapterViewHolder(View view){
            super(view);
            categoryTitle = view.findViewById(R.id.category_title);
            firstRun = view.findViewById(R.id.first_place);
            secondRun = view.findViewById(R.id.second_place);
            thirdRun = view.findViewById(R.id.third_place);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Leaderboard currentLeaderboard = leaderboardList.get(adapterPosition);
            clickHandler.onClick(currentLeaderboard);
        }
    }

    @Override
    public LeaderboardAdapter.LeaderboardAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.category_leaderboard_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new LeaderboardAdapter.LeaderboardAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.LeaderboardAdapterViewHolder holder, int position) {

        Leaderboard currentLeaderBoard = leaderboardList.get(position);
        holder.categoryTitle.setText(currentLeaderBoard.getCategory());
        List<RunPlace> runPlaces = currentLeaderBoard.getRunPlaceList();
        RunPlace firstRunPlace = runPlaces.get(0);
        RunPlace secondRunPlace = runPlaces.get(1);
        RunPlace thirdRunPlace = runPlaces.get(2);
        holder.firstRun.setText(firstRunPlace.getGameRun().getRunID());
        holder.firstRun.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked on the first run", Toast.LENGTH_SHORT);
            }
        });
        holder.secondRun.setText(secondRunPlace.getGameRun().getRunID());
        holder.secondRun.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked on the secondRun", Toast.LENGTH_SHORT);
            }
        });

        holder.thirdRun.setText(thirdRunPlace.getGameRun().getRunID());
        holder.thirdRun.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked on the thirdRun", Toast.LENGTH_SHORT);
            }
        });

//        Picasso.with(holder.categoryTitle.getContext())
//                .load(currentLeaderBoard.getAssets().getCoverLarge().getCoverLargerUri())
//                .into(holder.categoryTitle);
    }

    @Override
    public int getItemCount() {
        if (null == leaderboardList) return 0;
        return leaderboardList.size();
    }

    public void setLeaderboardData(List<Leaderboard> newLeaderboardList){
        this.leaderboardList = newLeaderboardList;
        notifyDataSetChanged();
    }

}
