package br.com.caelum.cadastro.fragment;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.util.AtualizadorDeLocalizacao;
import br.com.caelum.cadastro.util.Localizador;

/**
 * Created by android5843 on 11/12/15.
 */
public class MapaFragment extends SupportMapFragment {

    private GoogleMap map;

    @Override
    public void onResume() {
        super.onResume();
        map = getMap();
        Localizador localizador = new Localizador(getActivity());

        new AtualizadorDeLocalizacao(getActivity(), this);

        AlunoDAO dao = new AlunoDAO(getActivity());
        List<Aluno> alunos = dao.getLista();
        for(Aluno aluno:alunos){
            LatLng coordenada = localizador.getCoordenada(aluno.getEndereco());
            MarkerOptions marcador = new MarkerOptions().position(coordenada).title(aluno.getNome()).snippet(aluno.getEndereco());
            map.addMarker(marcador);
        }
    }

    public void centralizaNo(LatLng local) {
           map.addMarker(new MarkerOptions().position(local));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 11));
    }
}
