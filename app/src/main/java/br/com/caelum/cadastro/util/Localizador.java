package br.com.caelum.cadastro.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by android5843 on 11/12/15.
 */
public class Localizador {
    private Geocoder geo;

    public Localizador (Context ctx){
        geo = new Geocoder(ctx);
    }

    public LatLng getCoordenada(String endereco){
        try {
            List<Address> resultados = geo.getFromLocationName(endereco, 1);
            LatLng latLng = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
            return latLng;
        } catch (IOException e) {
            return null;
        }
    }
}
