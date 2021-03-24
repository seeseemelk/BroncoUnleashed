package bike.hackboy.bronco;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

import bike.hackboy.bronco.bean.PropertiesBean;

public class DetailsViewAdapter extends RecyclerView.Adapter<DetailsViewAdapter.ViewHolder> {
	private final List<PropertiesBean> data;
	private final LayoutInflater inflater;
	private final Context context;

	DetailsViewAdapter(Context context, List<PropertiesBean> data) {
		this.inflater = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
	}

	@NotNull
	@Override
	public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		PropertiesBean entry = data.get(position);

		try {
			Field resourceField = R.string.class.getDeclaredField(entry.getName());
			int resourceId = resourceField.getInt(resourceField);

			holder.name.setText(context.getString(resourceId));
			holder.value.setText(entry.getValue());

			if(entry.isGeoLink()) {
				SpannableString spanStr = new SpannableString(entry.getValue());
				spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
				holder.value.setText(spanStr);

				holder.container.setOnClickListener(v -> {
					Uri uri = Uri.parse(entry.getValue());

					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);
				});


			} else {
				holder.value.setText(entry.getValue());
			}

			if (entry.isLast()) {
				holder.setIsRecyclable(false);
				holder.divider.setVisibility(View.INVISIBLE);
				holder.value.setPadding(0, 0, 0, 160);
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			Log.e("details_view", e.getMessage(), e);
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView value;
		TextView divider;
		ConstraintLayout container;

		ViewHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			value = itemView.findViewById(R.id.value);
			divider = itemView.findViewById(R.id.divider);
			container = itemView.findViewById(R.id.list_item);
		}
	}
}