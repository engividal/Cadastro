package br.com.caelum.cadastro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.adapter.ListaAlunosAdapter;
import br.com.caelum.cadastro.converter.AlunoConverter;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.support.WebClient;
import br.com.caelum.cadastro.task.EnviaAlunosTask;


public class ListaAlunosActivity extends ActionBarActivity {
    private ListView listaAlunos;
    private List<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        Button botaoAdiciona = (Button) findViewById(R.id.lista_alunos_floating_button);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edicao = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                edicao.putExtra("alunoSelecionado", (Aluno) listaAlunos.getItemAtPosition(position));
                startActivity(edicao);
            }
        });

        botaoAdiciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(listaAlunos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_enviar_notas: {
                new EnviaAlunosTask(this).execute();
                return true;
            }
            case R.id.menu_receber_provas: {
                Intent provas = new Intent(this, ProvasActivity.class);
                startActivity(provas);
                return true;
            }
            case  R.id.menu_mapa: {
                Intent mapa = new Intent(this, MostraAlunosActivity.class);
                startActivity(mapa);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(info.position);

        MenuItem ligar = menu.add("Ligar");
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
        ligar.setIntent(intentLigar);

        MenuItem sms = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
        intentSMS.putExtra("sms_body", "um pedaço da mensagem ");
        sms.setIntent(intentSMS);

        MenuItem mapa = menu.add("Achar no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?z=14&q=" + alunoSelecionado.getEndereco()));
        mapa.setIntent(intentMapa);

        MenuItem site = menu.add("Navegar no Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String endereco = alunoSelecionado.getSite();
        if (!endereco.startsWith("http://")) {
            endereco = "http://" + endereco;
        }
        intentSite.setData(Uri.parse(endereco));
        site.setIntent(intentSite);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ListaAlunosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar?")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                        dao.deleta(alunoSelecionado);
                                        dao.close();
                                        carregaLista();
                                    }
                                }).setNegativeButton("Não", null).show();
                return true;
            }
        });

    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        alunos = dao.getLista();
        dao.close();

        ListaAlunosAdapter adapter = new ListaAlunosAdapter(this, alunos);

        this.listaAlunos.setAdapter(adapter);
    }
}
