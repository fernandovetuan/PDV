package br.com.trainning.pdv.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import br.com.trainning.pdv.R;
import br.com.trainning.pdv.domain.util.ImageInputHelper;
import butterknife.Bind;

public class CadastroNovoActivity extends BasicActivity implements ImageInputHelper.ImageActionListener{

    @Bind(R.id.txtDescricao)
    EditText txtDescricao;
    @Bind(R.id.txtUnidade)
    EditText txtUnidade;
    @Bind(R.id.txtPreco)
    EditText txtPreco;
    @Bind(R.id.txtCodigoBarras)
    EditText txtCodigoBarras;

    private ImageInputHelper imageInputHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_novo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);

        /*txtDescricao = (EditText)findViewById(R.id.txtDescricao);
        txtUnidade = (EditText)findViewById(R.id.txtUnidade);
        txtPreco = (EditText)findViewById(R.id.txtPreco);
        txtCodigoBarras =  (EditText)findViewById(R.id.txtCodigoBarras);*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Cadastro",txtDescricao.getText().toString());
                Log.d("Unidade",txtUnidade.getText().toString());
                Log.d("Preco",txtPreco.getText().toString());
                Log.d("Codigo",txtCodigoBarras.getText().toString());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        // cropping the selected image. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 800, 450, 16, 9);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        // cropping the taken photo. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 800, 450, 16, 9);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
        try {
            // getting bitmap from uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            // showing bitmap in image view
            ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
