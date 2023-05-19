package com.cursoandroid.navigationdrawer.ui.principal;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.cursoandroid.navigationdrawer.R;
import com.cursoandroid.navigationdrawer.ui.principal.helper.TarefaDAO;
import com.cursoandroid.navigationdrawer.ui.principal.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

public class AnotacoesImportantes extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anotacoes);

        editTarefa = findViewById(R.id.textTarefa);

        // Recuperar tarefa, caso seja edicao
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        // Configurar tarefa na caixa de texto
        if (tarefaAtual != null) {
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_segunda_tela, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSalvar:
                // Executa acao para o item salvar
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                // Edicao
                if (tarefaAtual != null) {
                    String nomeTarefa = editTarefa.getText().toString();

                    // Se o nome da tarefa nao estiver vazio
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        // Atualizar no banco de dados
                        if (tarefaDAO.atualizar(tarefa)) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Sucesso ao atualizar anotação!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao atualizar anotação!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else { // Salvar
                    String nomeTarefa = editTarefa.getText().toString();

                    // Se o nome da tarefa nao estiver vazio
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);

                        if (tarefaDAO.salvar(tarefa)) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Sucesso ao salvar a anotação!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao salvar a anotação!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}