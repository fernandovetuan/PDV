package br.com.trainning.pdv.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import br.com.trainning.pdv.R;
import butterknife.Bind;

public class CadastroNovoActivity extends BasicActivity {

    @Bind(R.id.txtDescricao)
    EditText txtDescricao;
    @Bind(R.id.txtUnidade)
    EditText txtUnidade;
    @Bind(R.id.txtPreco)
    EditText txtPreco;
    @Bind(R.id.txtCodigoBarras)
    EditText txtCodigoBarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_novo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

}
