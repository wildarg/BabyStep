package com.wild.test.tedrss.adapters;
/*
 * Created by Wild on 18.05.2015.
 */


import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.wild.test.tedrss.R;
import com.wild.test.tedrss.provider.RSSContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RSSAdapter extends RecyclerView.Adapter<RSSAdapter.RSSItemViewHolder> {

    private final PlayController playController;
    private Context context;
    private Cursor data;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault());
    private static RSSItemViewHolder currentPlay = null;

    public RSSAdapter(Context context) {
        this.context = context;
        playController = new PlayController(context);
    }

    @Override
    public RSSItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);
        return new RSSItemViewHolder(v, playController);
    }

    @Override
    public void onBindViewHolder(RSSItemViewHolder holder, int position) {
        if (data.moveToPosition(position)) {
            holder.populate(data);
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.getCount();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    protected static void startPlay(RSSItemViewHolder holder) {
        if (currentPlay != null)
            stopCurrentPlay();
    }

    private static void stopCurrentPlay() {
        currentPlay.videoView.stopPlayback();
        currentPlay.image.setVisibility(View.VISIBLE);
        currentPlay.videoView.setVisibility(View.GONE);
        currentPlay = null;
    }

    public static class RSSItemViewHolder extends RecyclerView.ViewHolder {
        private final PlayController playController;
        private TextView title;
        private TextView pubDate;
        private TextView description;
        private ImageView image;
        private ImageView playImage;
        private TextView link;
        private TextView duration;
        private VideoView videoView;
        private View view;
        private Uri videoUri;

        public RSSItemViewHolder(View view, PlayController playController) {
            super(view);
            this.view = view;
            this.playController = playController;
            title = (TextView) view.findViewById(R.id.titleTextView);
            pubDate = (TextView) view.findViewById(R.id.pubDateTextView);
            description = (TextView) view.findViewById(R.id.descriptionTextView);
            image = (ImageView) view.findViewById(R.id.imageView);
            image.setOnClickListener(new ImageClickListener());
            playImage = (ImageView) view.findViewById(R.id.playImageView);
            link = (TextView) view.findViewById(R.id.linkTextView);
            duration = (TextView) view.findViewById(R.id.durationTextView);
            videoView = (VideoView) view.findViewById(R.id.videoView);
        }

        public void populate(Cursor c) {
            image.setVisibility(View.VISIBLE);
            playImage.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            String imageUrl = c.getString(c.getColumnIndex(RSSContract.IMAGE_URL));
            title.setText(c.getString(c.getColumnIndex(RSSContract.TITLE)));
            Date d = new Date(c.getLong(c.getColumnIndex(RSSContract.PUBDATE)));
            pubDate.setText(sdf.format(d));
            description.setText(c.getString(c.getColumnIndex(RSSContract.DESCRIPTION)));
            link.setText("link: " + c.getString(c.getColumnIndex(RSSContract.LINK)));
            duration.setText(c.getString(c.getColumnIndex(RSSContract.DURATION)));
            videoUri = Uri.parse(c.getString(c.getColumnIndex(RSSContract.URL)));
            Picasso.with(view.getContext()).load(imageUrl).into(image);
        }

        protected class ImageClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                playController.startPlay(RSSItemViewHolder.this, videoUri);
            }
        }
    }

    public void swapCursor(Cursor data) {
        this.data = data;
        notifyDataSetChanged();
    }

    protected static class PlayController {

        private final Animation fadeAnimation;
        private RSSItemViewHolder current = null;
        private MediaController mc;

        public PlayController(Context context) {
            mc = new MediaController(context);
            fadeAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        }

        public void stopPlay(RSSItemViewHolder h) {
            h.videoView.stopPlayback();
            h.videoView.setVisibility(View.GONE);
            h.image.setVisibility(View.VISIBLE);
            h.playImage.setVisibility(View.VISIBLE);
            mc.hide();
            current = null;
        }

        public void startPlay(RSSItemViewHolder h, Uri videoUri) {
            if (current != null)
                stopPlay(current);
            current = h;
            fadeAnimation.setAnimationListener(new FadeAnimationListener(h.image));
            h.playImage.setVisibility(View.GONE);
            h.videoView.setVisibility(View.VISIBLE);
            mc.setAnchorView(h.videoView);
            h.videoView.setMediaController(mc);
            h.videoView.setVideoURI(videoUri);
            h.videoView.start();
            h.image.startAnimation(fadeAnimation);
        }

    }

    private static class FadeAnimationListener implements Animation.AnimationListener {
        private final ImageView image;

        public FadeAnimationListener(ImageView image) {
            this.image = image;
        }

        @Override
        public void onAnimationStart(Animation animation) {   }
        @Override
        public void onAnimationEnd(Animation animation) { image.setVisibility(View.GONE); }
        @Override
        public void onAnimationRepeat(Animation animation) {   }
    }

}
