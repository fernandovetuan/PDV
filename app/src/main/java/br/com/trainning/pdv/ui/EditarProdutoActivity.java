package br.com.trainning.pdv.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.trainning.pdv.R;
import br.com.trainning.pdv.domain.model.Produto;
import br.com.trainning.pdv.domain.util.Base64Util;
import br.com.trainning.pdv.domain.util.ImageInputHelper;
import butterknife.Bind;
import butterknife.OnClick;
import se.emilsjolander.sprinkles.Query;

public class EditarProdutoActivity extends BasicActivity implements ImageInputHelper.ImageActionListener {

    @Bind(R.id.ddlProduto)
    Spinner ddlProduto;

    private Produto produto;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                produto.setDescricao(txtDescricao.getText().toString());
                produto.setUnidade(txtUnidade.getText().toString());
                produto.setCodigoBarras(txtCodigoBarras.getText().toString());
                if(!txtPreco.getText().toString().equals(""))
                {
                    produto.setPreco(Double.parseDouble(txtPreco.getText().toString()));
                }

                Bitmap imagem = ((BitmapDrawable)imageViewFoto.getDrawable()).getBitmap();

                produto.setFoto(Base64Util.encodeTobase64(imagem));

                produto.save();

                Snackbar.make(view,"Produto alterado com sucesso!",Snackbar.LENGTH_SHORT).show();
            }
        });

        List<Produto> produtos = Query.many(Produto.class, "select * from produto order by codigo_barra").get().asList();

        List<String> barcodeList = new ArrayList<>();

        if (produtos != null)
        {
            for(Produto p : produtos)
            {
                barcodeList.add(p.getCodigoBarras());
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,barcodeList);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        ddlProduto.setAdapter(dataAdapter);

        ddlProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String barCode = parent.getItemAtPosition(position).toString();

                Log.d("BARCODE", "selecionado " + barCode);

                produto = Query.one(Produto.class, "select * from produto where codigo_barra = ?",barCode).get();

                if (produto!= null)
                {
                    txtDescricao.setText(produto.getDescricao().toString());
                    txtUnidade.setText(produto.getUnidade());
                    txtCodigoBarras.setText(produto.getCodigoBarras());
                    txtPreco.setText(String.valueOf(produto.getPreco()));
                    imageViewFoto.setImageBitmap(Base64Util.decodeBase64(produto.getFoto()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


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
