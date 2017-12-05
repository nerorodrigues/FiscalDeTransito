package com.example.neror.trabalho_4.Interface;

import com.example.neror.trabalho_4.Model.Infracao;
import com.example.neror.trabalho_4.Utils.InfracaoClusterItem;

/**
 * Created by neror on 25/11/2017.
 */

public interface AsyncResponse {
    void processFinish(Infracao[] infracoes);
}

