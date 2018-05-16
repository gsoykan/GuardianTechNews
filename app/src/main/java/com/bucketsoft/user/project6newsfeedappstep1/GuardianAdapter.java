package com.bucketsoft.user.project6newsfeedappstep1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GuardianAdapter extends RecyclerView.Adapter<GuardianAdapter.GuardianItemViewHolder> {

    private ArrayList<GuardianItem> guardianList = new ArrayList<>();
    private OnItemSelectedListener listener;

    @Override
    public GuardianItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guardian, parent, false);
        return new GuardianItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuardianItemViewHolder holder, int position) {
        GuardianItem item = guardianList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return guardianList.size();
    }

    public ArrayList<GuardianItem> getGuardianList() {
        return guardianList;
    }

    public void setGuardianList(ArrayList<GuardianItem> guardianList) {
        this.guardianList = guardianList;
    }

    public OnItemSelectedListener getListener() {
        return listener;
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public void addAll(List<GuardianItem> guardianItems) {
        guardianList = (ArrayList<GuardianItem>) guardianItems;
        notifyDataSetChanged();
    }

    public void clear() {
        guardianList.clear();
        notifyDataSetChanged();
    }

    class GuardianItemViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        TextView webPublicationDate;
        TextView webTitle;
        TextView contributorName;

        View itemView;

        public GuardianItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            sectionName = itemView.findViewById(R.id.section_name_text_view);
            webPublicationDate = itemView.findViewById(R.id.web_publication_date_text_view);
            webTitle = itemView.findViewById(R.id.web_title_text_view);
            contributorName = itemView.findViewById(R.id.contributor_name_text_view);
        }

        public void bind(final GuardianItem item) {
            sectionName.setText(item.getSectionName());
            webPublicationDate.setText(item.getWebPublicationDate());
            webTitle.setText(item.getWebTitle());
            contributorName.setText(item.getContributorName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getListener() != null){
                        getListener().onItemClick(item);

                    }
                }
            });
        }
    }

    interface OnItemSelectedListener {
        void onItemClick(GuardianItem selectedItem);

    }
}
