package com.example.neror.trabalho_4.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.neror.trabalho_4.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by neror on 26/11/2017.
 */

public class InfracaoRenderer extends DefaultClusterRenderer<InfracaoClusterItem> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private final IconGenerator mIconGenerator = new IconGenerator(mContext);
    private final IconGenerator mClusterIconGenerator = new IconGenerator(mContext);
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private final int mDimension;

    public InfracaoRenderer(Context context, GoogleMap map, ClusterManager<InfracaoClusterItem> clusterManager,LayoutInflater layoutInflater) {
        super(context, map, clusterManager);
        mLayoutInflater = layoutInflater;
        mContext = context;


        View multiProfile = mLayoutInflater.inflate(R.layout.multi_profile,null);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

        mImageView = new ImageView(mContext);
        mDimension = (int)mContext.getResources().getDimension(R.dimen.custom_profile_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension,mDimension));
        int padding = (int)mContext.getResources().getDimension(R.dimen.custom_profile_padding);
        mImageView.setPadding(padding,padding,padding,padding);
        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(InfracaoClusterItem item, MarkerOptions markerOptions) {
        //mImageView.setImageResource();
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title("");
    }
}
