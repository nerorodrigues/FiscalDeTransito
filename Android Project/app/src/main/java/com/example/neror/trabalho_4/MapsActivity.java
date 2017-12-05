package com.example.neror.trabalho_4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.neror.trabalho_4.Model.Infracao;
import com.example.neror.trabalho_4.Service.InfracaoService;
import com.example.neror.trabalho_4.Utils.InfracaoClusterItem;
import com.example.neror.trabalho_4.Utils.MultiDrawable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MapsActivity extends MapsBase implements ClusterManager.OnClusterClickListener<InfracaoClusterItem>,
    ClusterManager.OnClusterInfoWindowClickListener<InfracaoClusterItem>,
    ClusterManager.OnClusterItemClickListener<InfracaoClusterItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<InfracaoClusterItem> {//FragmentActivity implements OnMapReadyCallback,AsyncResponse {

    //public AsyncResponse delegate = null;
    //private GoogleMap mMap;
    //private FloatingActionButton mFloatingActionButton;
    private ClusterManager<InfracaoClusterItem> mManager;

    @Override
    public boolean onClusterClick(Cluster<InfracaoClusterItem> cluster) {
        Infracao infracao = cluster.getItems().iterator().next().getInfracao();
        Toast.makeText(this,cluster.getSize() + " (including " + infracao.getDescricao() + ")", Toast.LENGTH_LONG).show();

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (InfracaoClusterItem item: cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,10));
        }catch (Exception pEx){
            pEx.printStackTrace();
        }
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<InfracaoClusterItem> cluster){

    }

    @Override
    public boolean onClusterItemClick(InfracaoClusterItem item){
        Intent intent = new Intent(getApplicationContext(),AlteraInfracaoActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String serializeData = gson.toJson(item.getInfracao());
        bundle.putString("data",serializeData);
        intent.putExtra("data",bundle);
        startActivityForResult(intent,1);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(InfracaoClusterItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    public class InfracaoRenderer extends DefaultClusterRenderer<InfracaoClusterItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public InfracaoRenderer() {
            super(getApplicationContext(), getMap(), mManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile,null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int)getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension,mDimension));
            int padding = (int)getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding,padding,padding,padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(InfracaoClusterItem item, MarkerOptions markerOptions) {

            mImageView.setImageBitmap(item.getImage());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title("");
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<InfracaoClusterItem> cluster, MarkerOptions markerOptions) {
            List<Drawable> profilePhotos = new ArrayList(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (InfracaoClusterItem p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = new BitmapDrawable(getResources(),p.getImage());
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<InfracaoClusterItem> cluster) {
            return cluster.getSize() > 1;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        setUpCluster(super.getLocation(false));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setUpCluster(LatLng pLocation){

        if(pLocation!= null)
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(pLocation,13));

        mManager = new ClusterManager(this,getMap());
        getMap().setOnCameraIdleListener(mManager);
        getMap().setOnMarkerClickListener(mManager);
        getMap().setOnInfoWindowClickListener(mManager);
        mManager.setOnClusterClickListener(this);
        mManager.setOnClusterInfoWindowClickListener(this);
        mManager.setOnClusterItemClickListener(this);
        mManager.setOnClusterItemInfoWindowClickListener(this);
        mManager.setRenderer(new InfracaoRenderer());
        mManager.cluster();
    }


    //Result from New Infracao Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Gson gson = new Gson();
            String serializedData =data.getExtras().getString("data");
            Infracao alteredItem = gson.fromJson(serializedData,Infracao.class);
            if(resultCode == AlteraInfracaoActivity.RESULT_DELETED)
                removeItem(alteredItem);
            else if(resultCode == AlteraInfracaoActivity.RESULT_EDITED)
                updateItem(alteredItem);
        }
        else{
            if (resultCode == RESULT_OK) {
                Bundle resultData = data.getExtras();

                byte[] imageData = resultData.getByteArray("ImageData");
                String description = resultData.getString("Description");

                Infracao infracao = new Infracao();

                infracao.setDataInfracao(Calendar.getInstance().getTime());
                infracao.setDescricao(description);

                LatLng location = getLocation(true);

                infracao.setLatitude(location.latitude);
                infracao.setLongitude(location.longitude);

                InfracaoService.AsyncTaskUtil task = new InfracaoService.AsyncTaskUtil();
                task.delegate = new InfracaoService.AsyncTaskUtil.PostDataDelegate() {
                    @Override
                    public void FinishPost(Infracao infracao) {
                        addItem(new InfracaoClusterItem(infracao.getLatitude(),infracao.getLongitude(),infracao));
                        Toast.makeText(getApplicationContext(),"Novo item adicionado.",Toast.LENGTH_LONG).show();
                    }
                };
                task.execute(infracao, imageData);
            }
        }
    }

    private void updateItem(Infracao alteredItem) {
        for(InfracaoClusterItem item : mManager.getAlgorithm().getItems()){
            if(item.getInfracao().getId().equals(alteredItem.getId())) {
                item.setInfracao(alteredItem);
                mManager.cluster();
                Toast.makeText(getApplicationContext(),"Elemento foi alterado.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void removeItem(Infracao alteredItem) {
         for(InfracaoClusterItem item : mManager.getAlgorithm().getItems()){
             if(item.getInfracao().getId().equals(alteredItem.getId())) {
                 mManager.removeItem(item);
                 mManager.cluster();
                 Toast.makeText(getApplicationContext(),"Elemento foi removido.",Toast.LENGTH_LONG).show();
             }
         }
    }

    //Process returned Server Data
    @Override
    public void processFinish(Infracao[] infracoes) {
        for (Infracao item : infracoes) {
            if ((item.getLatitude() == null || item.getLatitude() == 0) || (item.getLongitude() == null || item.getLongitude() == 0))
                continue;
            addItem(new InfracaoClusterItem(item.getLatitude(),item.getLongitude(),item));
        }
    }

    //Add Cluster Item
    private void addItem(InfracaoClusterItem pItem){

        ThumbnailTask task = new ThumbnailTask(getApplicationContext());
        task.delegate = new ThumbnailTask.Finish() {
            @Override
            public void Finish (InfracaoClusterItem infracoes) {
                mManager.addItem(infracoes);
                mManager.cluster();
            }
        };
        task.execute(pItem);
    }

    private static class ThumbnailTask extends AsyncTask<InfracaoClusterItem,Integer,InfracaoClusterItem>{
        public Finish delegate = null;
        private Context mContext;
        public ThumbnailTask(Context pContext){
            mContext = pContext;
        }

        @Override
        protected InfracaoClusterItem doInBackground(InfracaoClusterItem... params) {
            InfracaoClusterItem item = params[0];
            try {
                Bitmap bitmap  = InfracaoService.getThumbnail(item.getInfracao().getId());
                item.setImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(mContext,"Ocorreu um erro ao tentar obter thumbnail do item.",Toast.LENGTH_LONG).show();
            }
            return item;
        }

        @Override
        protected void onPostExecute(InfracaoClusterItem infracaoClusterItem) {
            if(delegate != null)
                delegate.Finish(infracaoClusterItem);
        }

        public interface Finish{
            void Finish(InfracaoClusterItem pInfracao);
        }
    }
}