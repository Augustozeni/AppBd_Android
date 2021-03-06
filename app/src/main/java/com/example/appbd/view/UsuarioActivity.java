package com.example.appbd.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appbd.R;
import com.example.appbd.adapters.UsuarioAdapter;
import com.example.appbd.config.Globais;
import com.example.appbd.controller.UsuarioController;
import com.example.appbd.model.Usuario;

import java.util.ArrayList;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class UsuarioActivity extends AppCompatActivity {

    Context context;
    Usuario objeto;
    TextView lblId;
    EditText txtLogin, txtEmail, txtSenha;
    MaskedEditText txtTelefone;
    UsuarioController usuarioController;
    Usuario objUsuario;
    Button btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        context = UsuarioActivity.this;

        lblId = findViewById(R.id.lblIdUsuario_usuario);
        txtLogin = findViewById(R.id.txtLogin_usuario);
        txtEmail = findViewById(R.id.txtEmail_usuario);
        txtTelefone = findViewById(R.id.txtTelefone_usuario);
        btnExcluir = findViewById(R.id.btnExcluir_usuario);
        txtSenha = findViewById(R.id.txtSenha_usuario);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioController = new UsuarioController(context);
                objUsuario.setId(Integer.parseInt(lblId.getText().toString()));
                boolean retorno = usuarioController.excluir(objUsuario);
                if(retorno){
                    Globais.exibirMensagem(context, "Usuário excluído com sucesso");
                    finish();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getInt("id") > 0){
            //objeto = new
            int codigo = bundle.getInt("id");
            usuarioController = new UsuarioController(context);
            objUsuario = usuarioController.buscar(codigo);
            if(objUsuario != null){
                preencheCampos(objUsuario);
            }
        }else{
            btnExcluir.setVisibility(View.INVISIBLE);
        }

    }

    //Funcao para inflar o menu na tela
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cad, menu);

        return super.onCreateOptionsMenu(menu);
    }
    //Funcao que captura o click em um elemento
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean retorno;

        switch (id){

            case R.id.action_ok:

                objUsuario = new Usuario();
                usuarioController = new UsuarioController(context);

                objUsuario.setLogin(txtLogin.getText().toString());
                objUsuario.setEmail(txtEmail.getText().toString());
                objUsuario.setTelefone(txtTelefone.getRawText());

                String senhaMd5 = Globais.md5(txtSenha.getText().toString());
                objUsuario.setSenha(senhaMd5);

                if(!lblId.getText().equals("ID")){
                    objUsuario.setId(Integer.parseInt(lblId.getText().toString()));
                    retorno = usuarioController.alterar(objUsuario);
                    if (retorno){
                        Globais.exibirMensagem(context, "Usuário alterado com sucesso");
                        finish();
                    }
                }else{
                    retorno = usuarioController.incluir(objUsuario);
                    if (retorno) {
                        Globais.exibirMensagem(context, "Usuário alterado com sucesso");
                        finish();
                    }
                }

            case R.id.action_cancel:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void preencheCampos(Usuario objeto){
        try {

            lblId.setText(String.valueOf(objeto.getId()));
            txtLogin.setText(objeto.getLogin());
            txtLogin.setEnabled(false);

            txtTelefone.setText(objeto.getTelefone());
            txtEmail.setText(objeto.getEmail());

        }catch (Exception ex){
            Globais.exibirMensagem(context, ex.getMessage());
        }
    }
}