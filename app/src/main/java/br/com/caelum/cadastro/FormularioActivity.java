package br.com.caelum.cadastro;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.helper.FormularioHelper;
import br.com.caelum.cadastro.modelo.Aluno;


public class FormularioActivity extends ActionBarActivity {

    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                if(helper.temNome()) {
                    Aluno aluno = this.helper.pegaAlunoDoFormulario();
                    AlunoDAO dao = new AlunoDAO(this);
                    dao.insere(aluno);
                    dao.close();
                    //Toast.makeText(this, "Aluno: " + this.helper.pegaAlunoDoFormulario().getNome(), Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    helper.mostraErro();
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
