package chat.hola.com.app.wallet.transaction.Model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import chat.hola.com.app.wallet.transaction.TransactionUtilModule;

public class TransactionModel {

    @Named(TransactionUtilModule.ALL_LIST)
    @Inject
    ArrayList<TransactionData> allList;

    @Named(TransactionUtilModule.CREDIT_LIST)
    @Inject
    ArrayList<TransactionData> creditList;

    @Named(TransactionUtilModule.DEBIT_LIST)
    @Inject
    ArrayList<TransactionData> debitList;

    @Inject
    public TransactionModel() {
    }

    public void filterList(List<TransactionData> data) {
        if(!data.isEmpty()){
            allList.addAll(data);

            for(TransactionData txn : data){
                if(txn.getTxnTypeCode()==1)
                    creditList.add(txn);
                else if(txn.getTxnTypeCode()==4)
                    debitList.add(txn);
            }
        }
    }
}
