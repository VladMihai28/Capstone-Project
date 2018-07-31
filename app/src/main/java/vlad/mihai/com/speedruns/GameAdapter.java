package vlad.mihai.com.speedruns;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vlad.mihai.com.speedruns.model.Game;

/**
 * Created by Vlad
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameAdapterViewHolder>{

    private List<Game> gameList;

    private final GameAdapterOnClickHandler clickHandler;
    private Context context;

    public GameAdapter (Context context, GameAdapterOnClickHandler clickHandler){
        this.clickHandler= clickHandler;
        this.context = context;
    }

    public interface GameAdapterOnClickHandler{
        void onClick(Game currentGame);
    }

    public class GameAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView gameThumbnail;

        public GameAdapterViewHolder(View view){
            super(view);
            gameThumbnail = view.findViewById(R.id.imageview_game_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Game currentGame = gameList.get(adapterPosition);
            clickHandler.onClick(currentGame);
        }
    }

    @Override
    public GameAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.game_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GameAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameAdapterViewHolder holder, int position) {

        Game currentGame = gameList.get(position);

        Picasso.with(holder.gameThumbnail.getContext())
                .load(currentGame.getAssets().getCoverLarge().getCoverLargerUri())
                .into(holder.gameThumbnail);
    }

    @Override
    public int getItemCount() {
        if (null == gameList) return 0;
        return gameList.size();
    }

    public void setGameData(List<Game> newGameList){
        this.gameList = newGameList;
        notifyDataSetChanged();
    }

}
