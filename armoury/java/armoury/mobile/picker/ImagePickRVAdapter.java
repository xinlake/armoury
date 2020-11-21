package armoury.mobile.picker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import xinlake.armoury.R;


/**
 * @author XinLake
 * @version 2020.09
 */
public class ImagePickRVAdapter extends RecyclerView.Adapter<ImagePickRVAdapter.ItemView> {
    private final Activity activity;
    private final Listener listener;

    private final int span;
    private final float ratio;
    private final int maxSelect;

    private final RequestOptions gladeOption;
    private final ArrayList<ModelImage> items = new ArrayList<>();
    private final ArrayList<ModelImage> selection = new ArrayList<>();

    public ImagePickRVAdapter(@NonNull Activity activity, int span, float ratio, int maxSelect,
                              @NonNull Listener listener) {
        this.activity = activity;
        this.span = span;
        this.ratio = ratio;
        this.maxSelect = maxSelect;
        this.listener = listener;

        gladeOption = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    public void setData(List<ModelImage> images) {
        items.clear();
        selection.clear();

        items.addAll(images);
        notifyDataSetChanged();
    }

    public List<ModelImage> getData() {
        return items;
    }

    public List<ModelImage> getSelection() {
        return selection;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemView holder, int position) {
        final ModelImage itemData = items.get(position);

        Glide.with(activity)
            .load(itemData.getUri())
            .apply(gladeOption)
            .into(holder.imageView);

        holder.checkBox.setText(itemData.getResolutionText());
        holder.checkBox.setChecked(selection.contains(itemData));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selection.add(itemData);

                // max select is enabled and selection size reached the count
                if (maxSelect > 0 && selection.size() > maxSelect) {
                    ModelImage deselectItem = selection.remove(0);
                    int deselectPosition = items.indexOf(deselectItem);
                    notifyItemChanged(deselectPosition);
                }
            } else {
                try {
                    selection.remove(itemData);
                } catch (Exception ignored) {
                }
            }
        });

        holder.itemView.setOnClickListener(view -> {
            listener.OnItemClick(itemData);
        });
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    @Override
    @NonNull
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int cellWidth = parent.getWidth() / span;
        int imageHeight = (int) (cellWidth / ratio);

        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_image, parent, false);
        return new ItemView(itemView, imageHeight);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemView extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final CheckBox checkBox;

        private ItemView(View itemView, int imageHeight) {
            super(itemView);

            // Image width is set by layout but height is calculated based on width and ratio
            checkBox = itemView.findViewById(R.id.itemImage_CheckBox);
            imageView = itemView.findViewById(R.id.itemImage_ImageView);
            imageView.getLayoutParams().height = imageHeight;
        }
    }

    public interface Listener {
        void OnItemClick(ModelImage image);
    }
}
