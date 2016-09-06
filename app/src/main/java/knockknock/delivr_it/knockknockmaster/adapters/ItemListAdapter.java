package knockknock.delivr_it.knockknockmaster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.models.Item;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.HolderView> {

    private List<Item> items;

    public ItemListAdapter(List<Item> items) {

        this.items = items;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_row, parent, false);
        return new HolderView(inflatedView);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.itemName.setText(items.get(position).getItem_name());
        holder.itemSection.setText(items.get(position).getSection());
        holder.itemCategory.setText(items.get(position).getCategory());
        holder.itemView.setTag(items.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemCategory;
        TextView itemSection;
        View view;

        public HolderView(View itemView) {
            super(itemView);

            view = itemView;
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemCategory = (TextView) itemView.findViewById(R.id.item_category);
            itemSection = (TextView) itemView.findViewById(R.id.item_section);
        }
    }
}
