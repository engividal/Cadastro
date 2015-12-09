package br.com.caelum.cadastro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.cadastro.modelo.Aluno;

/**
 * Created by android5843 on 08/12/15.
 */
public class AlunoDAO extends SQLiteOpenHelper {
    private static final int VERSAO = 2;
    private static final String TABELA = "Alunos";
    private static final String DATABASE = "CadastroCaelum";

    public AlunoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ TABELA
                + " (id INTEGER PRIMARY KEY, "
                + " nome TEXT NOT NULL, "
                + " telefone TEXT, "
                + " endereco TEXT, "
                + " site TEXT, "
                + " nota REAL);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto TEXT;";
        db.execSQL(sql);
    }

    public void insere(Aluno aluno){
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public List<Aluno> getLista(){
        List<Aluno> alunos = new ArrayList<Aluno>();
        Cursor c = null;
        try {
            c = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

            while (c.moveToNext()) {
                Aluno aluno = new Aluno();

                aluno.setId(c.getLong(c.getColumnIndex("id")));
                aluno.setNome(c.getString(c.getColumnIndex("nome")));
                aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
                aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
                aluno.setSite(c.getString(c.getColumnIndex("site")));
                aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
                aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

                alunos.add(aluno);

            }


        }finally {
            c.close();
        }
        return alunos;
    }

    public void deleta(Aluno alunoSelecionado) {
        String[] id = {alunoSelecionado.getId().toString()};

        getWritableDatabase().delete(TABELA, "id=?", id);
    }

    public void altera(Aluno aluno){
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        String[] idParaSerAlterado = {aluno.getId().toString()};

        getWritableDatabase().update(TABELA, values, "id=?", idParaSerAlterado);
    }

    public boolean isAluno(String telefone){
        String[] parametros = {telefone};

        Cursor rawQuery = getReadableDatabase()
                .rawQuery("SELECT telefone FROM " + TABELA
                + " WHERE telefone = ?", parametros);

        int total = rawQuery.getCount();
        rawQuery.close();

        return total > 0;
    }
}
