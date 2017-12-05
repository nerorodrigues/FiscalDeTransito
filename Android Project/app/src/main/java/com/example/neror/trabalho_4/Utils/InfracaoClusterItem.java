package com.example.neror.trabalho_4.Utils;

import android.graphics.Bitmap;

import com.example.neror.trabalho_4.Model.Infracao;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by neror on 26/11/2017.
 */

public class InfracaoClusterItem implements ClusterItem {
    private final  LatLng mPosition;
    private Infracao mInfracao;
    private Bitmap mImage;

    public InfracaoClusterItem(double pLatitude, double pLongitude, Infracao pInfracao)
    {
        mPosition  = new LatLng(pLatitude,pLongitude);
        mInfracao = pInfracao;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Infracao getInfracao(){
        return  mInfracao;
    }

    public void  setInfracao(Infracao pValue){
        this.mInfracao = pValue;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
    }
}
