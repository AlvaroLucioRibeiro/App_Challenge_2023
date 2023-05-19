package com.cursoandroid.navigationdrawer.ui.contato;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cursoandroid.navigationdrawer.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class ContatoFragment extends Fragment {

    public ContatoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String descricao = "Você conhece o Serviço de Relacionamento Viasat?\n" +
                "Entre em contato conosco via nosso Suporte 24 horas Whatsapp (61) 93300-7478 ou ligue para nossos especialistas: 0800591 1563.";

        Element versao = new Element();
        versao.setTitle( "Versão 1.0" );

        return new AboutPage( getActivity() )
                .setImage( R.drawable.logo )
                .setDescription( descricao )
                .addGroup("Entre em contato")
                .addEmail("inatel@inatel.br", "Envie um e-mail")
                .addWebsite("www.viasat.com", "Acesse nosso site")
                .addItem( versao )
                .create();
        }
}
