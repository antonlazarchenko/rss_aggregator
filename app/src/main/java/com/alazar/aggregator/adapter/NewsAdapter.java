package com.alazar.aggregator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alazar.aggregator.R;
import com.alazar.aggregator.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<NewsItem> news = new ArrayList<>();

    private static RecyclerViewClickListener itemListener;

    public NewsAdapter (RecyclerViewClickListener itemListener) {
        NewsAdapter.itemListener = itemListener;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    public void setItems(List<NewsItem> news) {
        this.news.addAll(news);
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.news.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {

        holder.title.setText(news.get(position).getTitle());
        holder.date.setText(news.get(position).getDate());
        holder.desc.setText(news.get(position).getShortDescription());

        holder.setLink(news.get(position).getLink());

    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title, date, desc;
        public String link;

        public void setLink(String link) {
            this.link = link;
        }

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.titleTxt);
            date = view.findViewById(R.id.dateTxt);
            desc = view.findViewById(R.id.descTxt);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(link, v, this.getLayoutPosition());
        }
    }
}


