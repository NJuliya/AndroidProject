package com.samples.yulya.phcollage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Image> mThumb = null;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public ImageAdapter(Context context, ArrayList<Image> str) {

        mContext = context;
        mThumb = str;
        imageLoader =  ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(mContext);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .memoryCacheExtraOptions(200, 200)
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        imageLoader.init(config);
    }

    @Override
    public int getCount() {
        return mThumb.size();
    }

    @Override
    public Object getItem(int position) {
        return mThumb.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View imView;
        if (convertView==null) {
            imView = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            imView = inflater.inflate(R.layout.cell_grid, parent, false);
        }
        else
        {
            imView = convertView;
        }
        ImageView imageView = (ImageView) imView.findViewById(R.id.imview);
        imageLoader.displayImage(mThumb.get(position).getUrl(), imageView, options);
        return imView;
    }
}
