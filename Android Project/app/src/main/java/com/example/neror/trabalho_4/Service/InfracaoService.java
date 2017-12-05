package com.example.neror.trabalho_4.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.neror.trabalho_4.Interface.AsyncResponse;
import com.example.neror.trabalho_4.Model.Infracao;
import com.example.neror.trabalho_4.Utils.MultipartUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by neror on 25/11/2017.
 */

public class InfracaoService {

    //public static final String Server="http://192.168.15.5";
    public static final String Server="http://10.0.2.2";
    //public static final String Server="http://10.61.30.58";
    public static final String API = Server + "/FiscalDeTransitoApi/api/";
    public static final String SERVICE = "Infracoes";
    public static final String THUMBNAIL_SERVICE = API + SERVICE + "/thumbnail/";
    public static final String IMAGE_SERVICE = API + SERVICE + "/image/";

    public static Infracao[] getData(String pUrl) throws IOException {

        StringBuilder returnData = new StringBuilder();
        URL url = new URL(API + pUrl);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type","application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String response;
        while ((response = in.readLine()) != null) {
            returnData.append(response);
        }
        in.close();
        if(returnData.length() == 0)
            return null;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson.fromJson(returnData.toString(), Infracao[].class);
    }

    public static void DeleteData(UUID pUID) throws IOException {
        URL url = new URL(InfracaoService.API + InfracaoService.SERVICE + "?InfracaoID=" + pUID.toString());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        if(responseCode != 200)
            throw new InternalError("Ocorreu um erro ao tentar excluir o registro.");
    }

    public static Bitmap getThumbnail(UUID pImageID) throws IOException {
        URL url = new URL(THUMBNAIL_SERVICE + pImageID.toString());// "http://10.0.2.2/FiscalDeTransitoApi/api/Infracoes/thumbnail/"
        return getImage(url);
    }

    public static Bitmap getImage(UUID pImageID) throws IOException {
        URL url = new URL(IMAGE_SERVICE + pImageID.toString());
        return getImage(url);
    }

    private static Bitmap getImage(URL pUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) pUrl.openConnection();
        connection.setRequestMethod("GET");
        InputStream stream = connection.getInputStream();
        Bitmap image = BitmapFactory.decodeStream(stream);
        return image;
    }

    public static Infracao postData(Infracao pData, byte[] pFileData, String pUrl) {
        try {

            Gson gson1 = new Gson();
            String data1 = gson1.toJson(pData);
            MultipartUtility utility = new MultipartUtility(API + pUrl,"UTF-8","POST");

            if(pData.getDescricao() != null && pData.getDescricao() != "")
                utility.addFormField("Descricao",pData.getDescricao());

            if(pData.getIMEI() != null && pData.getIMEI() > 0)
                utility.addFormField("IMEI",pData.getIMEI().toString());

            if(pData.getLatitude() != null &&  pData.getLatitude() != 0)
                utility.addFormField("Latitude", pData.getLatitude().toString());

            if(pData.getLongitude() != null  && pData.getLongitude() != 0)
                utility.addFormField("Longitude",pData.getLongitude().toString());

            if(pData.getDataInfracao() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                String dDate = sdf.format(pData.getDataInfracao());
                utility.addFormField("DataInfracao", dDate);
            }
            if(pFileData != null)
                utility.addFilePart("InfracaoFile",pFileData);


            String data = utility.finish();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            Infracao infracao =  gson.fromJson(data,Infracao.class);

            return infracao;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

    public static Infracao updateData(Infracao pData) {
        try {

            MultipartUtility utility = new MultipartUtility(API + SERVICE + "/edit","UTF-8","POST");

            if(pData.getId() != null){
                utility.addFormField("Id",pData.getId().toString());
            }

            if(pData.getDescricao() != null && pData.getDescricao() != "")
                utility.addFormField("Descricao",pData.getDescricao());

            if(pData.getIMEI() != null && pData.getIMEI() > 0)
                utility.addFormField("IMEI",pData.getIMEI().toString());

            if(pData.getLatitude() != null &&  pData.getLatitude() != 0)
                utility.addFormField("Latitude", pData.getLatitude().toString());

            if(pData.getLongitude() != null  && pData.getLongitude() != 0)
                utility.addFormField("Longitude",pData.getLongitude().toString());

            if(pData.getDataInfracao() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                String dDate = sdf.format(pData.getDataInfracao());
                utility.addFormField("DataInfracao", dDate);
            }

            String data = utility.finish();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            Infracao infracao =  gson.fromJson(data,Infracao.class);
            return  infracao;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class AsyncTaskUtil extends AsyncTask<Object,Object,Infracao> {
        public  PostDataDelegate delegate = null;
        @Override
        protected Infracao doInBackground(Object... pInfracao) {
            return postData((Infracao) pInfracao[0],(byte[])pInfracao[1],SERVICE);
        }

        @Override
        protected void onPostExecute(Infracao infracao) {
            delegate.FinishPost(infracao);
        }

        public interface PostDataDelegate{
            void FinishPost(Infracao infracao);
        }
    }

    public  static class AsynTaskGetInfracao extends AsyncTask<Object,Integer,Infracao[]>
    {
        public AsyncResponse delegate = null;

        @Override
        protected Infracao[] doInBackground(Object... objects) {
            try {
                return getData(SERVICE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Infracao[] infracoes) {
            delegate.processFinish(infracoes);
        }
    }
}
