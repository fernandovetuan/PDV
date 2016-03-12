package br.com.trainning.pdv.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;

import java.io.File;
import java.io.IOException;

import br.com.trainning.pdv.R;
import br.com.trainning.pdv.domain.model.Produto;
import br.com.trainning.pdv.domain.util.Base64Util;
import br.com.trainning.pdv.domain.util.ImageInputHelper;
import butterknife.Bind;
import butterknife.OnClick;

public class CadastroNovoActivity extends BasicActivity implements ImageInputHelper.ImageActionListener{

    @Bind(R.id.txtDescricao)
    EditText txtDescricao;
    @Bind(R.id.txtUnidade)
    EditText txtUnidade;
    @Bind(R.id.txtPreco)
    EditText txtPreco;
    @Bind(R.id.txtCodigoBarras)
    EditText txtCodigoBarras;
    @Bind(R.id.imgProduto)
    ImageView imageViewFoto;
    @Bind(R.id.btnCamera)
    ImageView imageViewCamera;
    @Bind(R.id.btnInsertPhoto)
    ImageButton imageButtonGaleria;

    private ImageInputHelper imageInputHelper;
    private Produto produto;
    private Double latitude = 0.0d;
    private Double longitude = 0.0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_novo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LostApiClient lostApiClient = new LostApiClient.Builder(this).build();
        lostApiClient.connect();

        Location location = LocationServices.FusedLocationApi.getLastLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.d("location"," Latitude:" + latitude);
            Log.d("location"," Longitude:" + longitude);
        }

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

                produto = new Produto();
                produto.setId(0L);
                produto.setDescricao(txtDescricao.getText().toString());
                produto.setUnidade(txtUnidade.getText().toString());
                produto.setCodigoBarras(txtCodigoBarras.getText().toString());
                if (!txtPreco.getText().toString().equals(""))
                {
                    produto.setPreco(Double.parseDouble(txtPreco.getText().toString()));
                }
                else
                {
                    produto.setPreco(0.0);
                }
                Bitmap imagem = ((BitmapDrawable)imageViewFoto.getDrawable()).getBitmap();

                produto.setFoto(Base64Util.encodeTobase64(imagem));

                produto.setLatitude(latitude);
                produto.setLongitude(longitude);

                produto.save();
                finish();
            }
        });
    }

    @OnClick(R.id.btnInsertPhoto)
    public void onClickGaleria()
    {
        imageInputHelper.selectImageFromGallery();
    }

    @OnClick(R.id.btnCamera)
    public void onCamera()
    {
        imageInputHelper.takePhotoWithCamera();
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
        imageInputHelper.requestCropImage(uri, 100, 100, 0, 0);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        // cropping the taken photo. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 100, 100, 0, 0);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
        try {
            // getting bitmap from uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            imageViewFoto.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
