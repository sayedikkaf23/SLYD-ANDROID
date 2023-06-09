package chat.hola.com.app.motionView.motionviews.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;



/**
 * selects sticker
 * result - Integer, resource id of the sticker, bundled at key EXTRA_STICKER_ID
 * <p>
 * Stickers borrowed from : http://www.flaticon.com/packs/pokemon-go
 */

public class StickerSelectActivity extends AppCompatActivity {

    public static final String EXTRA_STICKER_ID = "extra_sticker_id";

    private final int[] stickerIds = {
            R.drawable.abra,
            R.drawable.bellsprout,
            R.drawable.bracelet,
            R.drawable.bullbasaur,
            R.drawable.camera,
            R.drawable.candy,
            R.drawable.caterpie,
            R.drawable.charmander,
            R.drawable.mankey,
            R.drawable.map,
            R.drawable.mega_ball,
            R.drawable.meowth,
            R.drawable.pawprints,
            R.drawable.pidgey,
            R.drawable.pikachu,
            R.drawable.pikachu_1,
            R.drawable.pikachu_2,
            R.drawable.player,
            R.drawable.pointer,
            R.drawable.pokebag,
            R.drawable.pokeball,
            R.drawable.pokeballs,
            R.drawable.pokecoin,
            R.drawable.pokedex,
            R.drawable.potion,
            R.drawable.psyduck,
            R.drawable.rattata,
            R.drawable.revive,
            R.drawable.squirtle,
            R.drawable.star,
            R.drawable.star_1,
            R.drawable.superball,
            R.drawable.tornado,
            R.drawable.venonat,
            R.drawable.weedle,
            R.drawable.zubat
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sticker_activity);

        //noinspection ConstantConditions
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String capturedImagePath = "";
        if (getIntent().getStringExtra("capturedImagePath")!=null)
        capturedImagePath = getIntent().getStringExtra("capturedImagePath");

        ImageView backgroundImage = (ImageView) findViewById(R.id.iv_image);
        Glide.with(StickerSelectActivity.this)
                .load(capturedImagePath)
                .into(backgroundImage);

        ImageView backButton = (ImageView) findViewById(R.id.close);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stickers_recycler_view);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(glm);

        List<Integer> stickers = new ArrayList<>(stickerIds.length);
        for (Integer id : stickerIds) {
            stickers.add(id);
        }

        recyclerView.setAdapter(new StickersAdapter(stickers, this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onStickerSelected(int stickerId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STICKER_ID, stickerId);
        setResult(RESULT_OK, intent);
        finish();
    }

    class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.StickerViewHolder> {

        private final List<Integer> stickerIds;
        private final Context context;
        private final LayoutInflater layoutInflater;

        StickersAdapter(@NonNull List<Integer> stickerIds, @NonNull Context context) {
            this.stickerIds = stickerIds;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StickerViewHolder(layoutInflater.inflate(R.layout.sticker_item, parent, false));
        }

        @Override
        public void onBindViewHolder(StickerViewHolder holder, int position) {
            holder.image.setImageDrawable(ContextCompat.getDrawable(context, getItem(position)));
        }

        @Override
        public int getItemCount() {
            return stickerIds.size();
        }

        private int getItem(int position) {
            return stickerIds.get(position);
        }

        class StickerViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            StickerViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.sticker_image);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos >= 0) { // might be NO_POSITION
                            onStickerSelected(getItem(pos));
                        }
                    }
                });
            }
        }
    }
}
