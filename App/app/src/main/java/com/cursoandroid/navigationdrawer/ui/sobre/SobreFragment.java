package com.cursoandroid.navigationdrawer.ui.sobre;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cursoandroid.navigationdrawer.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * A simple {@link Fragment} subclass.
 */
public class SobreFragment extends Fragment {

    public SobreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String descricao = "A Viasat é uma empresa global de comunicação que acredita que tudo e todos podem e devem estar conectados. Somos mais de 7.000 colaboradores e em mais de 60 escritórios ao redor do mundo. Por mais de 30 anos, nossas inovações vem ajudando a moldar como consumidores, empresas, governos e forças armadas se comunicam.\n" +
                "\n" +
                "\n" +
                "Em 2020, quando iniciamos as atividades no Brasil, não imaginamos os tamanhos dos desafios e das conquistas que o país traria para a Viasat. Hoje, somos o principal provedor de internet via satélite que consegue cobrir todo o território nacional e estamos colaborando para aumentar a inclusão digital no país, levando internet para pessoas nas regiões mais remotas do país e facilitando, assim, o acesso a serviços de educação, capacitação e geração de renda.";

        Element versao = new Element();
        versao.setTitle( "Versão 1.0" );

        return new AboutPage( getActivity() )
                .setImage( R.drawable.logo )
                .setDescription( descricao )
                .addGroup("Redes sociais")
                .addFacebook("https://www.facebook.com/ViasatBR/", "Facebook")
                .addInstagram("https://www.instagram.com/viasatbrasil/", "Instagram")
                .addYoutube("https://www.youtube.com/channel/UCMcYWKC_n7RBAuyVKOf3sCA", "Youtube")
                .addGitHub("thalitaDomingos", "GitHub")
                .addPlayStore("com.facebook.katana", "Download App")
                .addItem( versao )
                .create();
    }
}
