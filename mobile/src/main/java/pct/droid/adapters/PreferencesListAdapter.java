/*
 * This file is part of Popcorn Time.
 *
 * Popcorn Time is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Popcorn Time is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Popcorn Time. If not, see <http://www.gnu.org/licenses/>.
 */

package pct.droid.adapters;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pct.droid.R;
import pct.droid.base.PopcornApplication;
import pct.droid.base.preferences.PrefItem;

public class PreferencesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mItems;
    final int NORMAL = 0, HEADER = 1;

    public PreferencesListAdapter(List<Object> items) {
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case HEADER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subheader, parent, false);
                return new PreferencesListAdapter.HeaderHolder(v);
            case NORMAL:
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon_twoline_item, parent, false);
                return new PreferencesListAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == NORMAL) {
            ViewHolder itemViewHolder = (ViewHolder) viewHolder;
            PrefItem item = (PrefItem) mItems.get(position);
            itemViewHolder.itemView.setClickable(item.isClickable());
            itemViewHolder.icon.setImageResource(item.getIconResource());
            itemViewHolder.icon.setColorFilter(PopcornApplication.getAppContext().getResources().getColor(R.color.text_color), PorterDuff.Mode.SRC_IN);
            itemViewHolder.text1.setText(item.getTitle());
            itemViewHolder.text2.setText(item.getSubTitle());

            if (item.getDefaultValue() instanceof Boolean) {
                itemViewHolder.checkBox.setVisibility(View.VISIBLE);
                itemViewHolder.checkBox.setChecked((boolean) item.getValue());
            } else {
                itemViewHolder.checkBox.setVisibility(View.GONE);
            }
        } else if (getItemViewType(position) == HEADER) {
            HeaderHolder headerViewHolder = (HeaderHolder) viewHolder;
            headerViewHolder.itemView.setText((String) mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof String) {
            return HEADER;
        }
        return NORMAL;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View itemView;
        @InjectView(android.R.id.icon)
        ImageView icon;
        @InjectView(android.R.id.text1)
        TextView text1;
        @InjectView(android.R.id.text2)
        TextView text2;
        @InjectView(android.R.id.checkbox)
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getPosition();
            PrefItem item = (PrefItem) mItems.get(position);
            item.onClick();
        }

    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        TextView itemView;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.itemView = (TextView) itemView;
        }

    }

}
