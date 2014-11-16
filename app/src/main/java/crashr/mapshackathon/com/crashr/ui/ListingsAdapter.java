package crashr.mapshackathon.com.crashr.ui;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import crashr.mapshackathon.com.crashr.R;
import crashr.mapshackathon.com.crashr.model.Listing;

/**
 * Created by Phillip on 11/15/2014.
 */
public class ListingsAdapter extends ArrayAdapter<Listing> {
    private List<Listing> mListings;
    private Context mContext;

    public ListingsAdapter(Context context, List<Listing> listings) {
        super(context, R.layout.listing_layout, listings);
        mContext = context;
        mListings = listings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        Listing listing = mListings.get(position);

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            rowView = inflater.inflate(R.layout.listing_layout, null);

            ViewHolder  viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.listing_category_img);
            viewHolder.textView = (TextView) rowView.findViewById(R.id.listing_address);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textView.setText(listing.address);

        return rowView;
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
