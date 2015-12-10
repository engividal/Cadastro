package br.com.caelum.cadastro.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.converter.AlunoConverter;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.support.WebClient;

/**
 * Created by android5843 on 10/12/15.
 */
public class EnviaAlunosTask extends AsyncTask<Object, Object, String> {
    private final Context context;
    private ProgressDialog progress;

    public EnviaAlunosTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para a web", true, true);
    }

    @Override
    protected String doInBackground(Object... params) {
        List<Aluno> alunos;
        AlunoDAO dao = new AlunoDAO(context);
        alunos = dao.getLista();
        String json = new AlunoConverter().toJson(alunos);
        WebClient client = new WebClient();
        String resposta = client.post(json);

        return resposta;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        progress.dismiss();

    }
}
