package com.example.neror.trabalho_4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.neror.trabalho_4.Model.Infracao;
import com.example.neror.trabalho_4.Service.InfracaoService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;

public class AlteraInfracaoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Infracao mItem;
    private AutoCompleteTextView mTextComplete;
    public static final int RESULT_DELETED = 999;
    public static final int RESULT_EDITED = 998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_infracao);
        Bundle data = getIntent().getBundleExtra("data");
        Gson gson = new Gson();
        String serializedData = data.getString("data");
        mItem = gson.fromJson(serializedData,Infracao.class);

        mTextComplete = (AutoCompleteTextView)findViewById(R.id.txtDescrition);

        mTextComplete.setText(mItem.getDescricao());

        mImageView  = (ImageView) findViewById(R.id.imgPhoto);

        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTask task = new DeleteTask();
                task.delegate = new DeleteTask.Finish() {
                    @Override
                    public void Finish() {
                        setResult(RESULT_DELETED,getIntentWithData());
                        finish();
                    }
                };
                task.execute(mItem.getId());
            }
        });

        Button btnEdit = (Button)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTask task = new UpdateTask();
                task.delegate = new UpdateTask.Finish() {
                    @Override
                    public void Finish(Infracao pInfracao) {
                        setResult(RESULT_EDITED,getIntentWithData());
                        finish();
                    }
                };
                mItem.setDescricao(mTextComplete.getText().toString());
                task.execute(mItem);
            }
        });

        ImageTask imageTask = new ImageTask(getApplicationContext());
        imageTask.delegate = new ImageTask.Finish() {
            @Override
            public void Finish(Bitmap pImage) {
                mImageView.setImageBitmap(pImage);
            }
        };
        imageTask.execute(mItem);
    }

    private Intent getIntentWithData(){
        Intent intent = new Intent();
        Gson gson = new Gson();
        String serializedData = gson.toJson(mItem);
        intent.putExtra("data", serializedData);
        return intent;
    }

    public static class DeleteTask extends AsyncTask<UUID,Integer,Object>{

        public Finish delegate = null;

        @Override
        protected Object doInBackground(UUID... params) {
            try {
                InfracaoService.DeleteData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            delegate.Finish();
        }

        public interface Finish{
            void Finish();
        }
    }

    private static class ImageTask extends AsyncTask<Infracao,Integer,Bitmap>{
        public Finish delegate = null;
        private Context mContext;
        public ImageTask(Context pContext){
            mContext = pContext;
        }

        @Override
        protected Bitmap doInBackground(Infracao... params) {
            Infracao item = params[0];
            try {
                return InfracaoService.getImage(item.getId());
            } catch (IOException e) {
                Toast.makeText(mContext,"Ocorreu um erro ao tentar obter a imagem do item.",Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap pImage) {
            if(delegate != null)
                delegate.Finish(pImage);
        }

        public interface Finish{
            void Finish(Bitmap pImage);
        }
    }

    private static class UpdateTask extends AsyncTask<Infracao,Integer,Infracao>{
        public Finish delegate = null;
        @Override
        protected Infracao doInBackground(Infracao... params) {
            Infracao item = params[0];
            InfracaoService.updateData(item);
            return item;
        }

        @Override
        protected void onPostExecute(Infracao infracao) {
            super.onPostExecute(infracao);
            if(delegate != null)
                delegate.Finish(infracao);
        }

        public interface Finish{
            void Finish(Infracao pInfracao);
        }
    }

}
