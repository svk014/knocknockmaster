package knockknock.delivr_it.knockknockmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import knockknock.delivr_it.knockknockmaster.models.Offer;
import knockknock.delivr_it.knockknockmaster.R;


public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.MyViewHolder> {

    private Context context;
    private List<Offer> offerList;
    private ArrayList<String> idsToDelete;

    public OffersListAdapter(Context context, List<Offer> offerList, ArrayList<String> idsToDelete) {
        this.context = context;
        this.offerList = offerList;
        this.idsToDelete = idsToDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_offer_in_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Offer offer = offerList.get(position);
        holder.offerTitle.setText(offer.getOffer_title());
        holder.offerText.setText(offer.getOffer_text());
        Picasso.with(context).load(new File(offer.getImage_local_path())).into(holder.offerImage);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked())
                    idsToDelete.add(offer.getId());
                else
                    idsToDelete.remove(offer.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView offerTitle;
        public final TextView offerText;
        public final ImageView offerImage;
        public final CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            offerTitle = (TextView) view.findViewById(R.id.textView_offer_title);
            offerText = (TextView) view.findViewById(R.id.textView_offer_text);
            offerImage = (ImageView) view.findViewById(R.id.imageView);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}