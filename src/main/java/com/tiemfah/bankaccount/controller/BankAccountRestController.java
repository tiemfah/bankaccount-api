package com.tiemfah.bankaccount.controller;

import com.tiemfah.bankaccount.data.BankAccountRepository;
import com.tiemfah.bankaccount.data.TransactionRepository;
import com.tiemfah.bankaccount.model.BankAccount;
import com.tiemfah.bankaccount.model.Transaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankaccount")
public class BankAccountRestController {

    private BankAccountRepository bankAccountRepository;
    private TransactionRepository transactionRepository;

    public BankAccountRestController(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    @GetMapping("/{id}")
    public BankAccount getOne(@PathVariable int id) {
        return bankAccountRepository.findById(id).get();
    }

    @GetMapping("/customer/{customerId}")
    public List<BankAccount> getAllCustomerId(@PathVariable int customerId) {
        return bankAccountRepository.findByCustomerId(customerId);
    }

    @PostMapping
    public BankAccount create(@RequestBody BankAccount bankAccount) {
        BankAccount record = bankAccountRepository.save(bankAccount);
        return record;
    }

    @PostMapping("/transaction")
    public BankAccount makeTransaction(@RequestBody Transaction transaction) {
        Transaction transaction1 = transactionRepository.save(transaction);
        BankAccount bankAccount = bankAccountRepository.findById(transaction1.getBankAccountId()).get();
        if (transaction1.getTransactionType().equals("WITHDRAW")) {
            bankAccount.setBalance(bankAccount.getBalance() - transaction1.getAmount());
        } else if (transaction1.getTransactionType().equals("DEPOSIT")) {
            bankAccount.setBalance(bankAccount.getBalance() + transaction1.getAmount());
        }
        bankAccountRepository.save(bankAccount);
        return bankAccount;
    }

    @PutMapping("/{id}")
    public BankAccount update(@PathVariable int id,
                              @RequestBody BankAccount bankAccount) {
        BankAccount record = bankAccountRepository.findById(id).get();
        record.setBalance(bankAccount.getBalance());
        bankAccountRepository.save(record);
        return record;
    }

    @DeleteMapping("/{id}")
    public BankAccount delete(@PathVariable int id) {
        BankAccount record = bankAccountRepository.findById(id).get();

        bankAccountRepository.delete(record);
        return record;
    }
}

