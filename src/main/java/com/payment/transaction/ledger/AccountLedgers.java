package com.payment.transaction.ledger;

import com.payment.customers.entity.BOD;
import com.payment.customers.repository.BODRepository;
import com.payment.customers.service.AccountService;
import com.payment.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccountLedgers {
@Autowired
    private BODRepository bodRepository;

    public void updateAccountLedger(List<BOD> bods) {
        bodRepository.saveAll(bods);


    }

    public List<BOD> accountLedgers() {
       return bodRepository.findAll();


    }

    public List<BOD> accountLedgerByPaymentId(Long paymentId) {
        List<BOD> bods=bodRepository.findAllByPaymentId(paymentId);
        if(bods.isEmpty()){
            throw new NotFoundException("Not found the paymentId:"+paymentId);
        }
        return bods;


    }


}
