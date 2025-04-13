package com.tarun.walletService.kafka;


import com.tarun.walletService.dto.CreditRequestEvent;
import com.tarun.walletService.dto.TransactionStatusEvent;
import com.tarun.walletService.entity.Wallet;
import com.tarun.walletService.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditRequestConsumer {

    private final WalletRepository walletRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "wallet.transaction.credit.request", groupId = "wallet-group")
    public void consumeCreditRequest(CreditRequestEvent event) {
        Wallet wallet = walletRepository.findByUserId(event.getUserId())
                .orElse(null);

        if (wallet == null) {
            kafkaTemplate.send("wallet.transaction.credit.failed",
                    new TransactionStatusEvent(event.getTransactionId(), "FAILED", "CREDIT"));
            return;
        }

        // Credit the wallet
        wallet.setBalance(wallet.getBalance().add(event.getAmount()));
        walletRepository.save(wallet);

        kafkaTemplate.send("wallet.transaction.credit.success",
                new TransactionStatusEvent(event.getTransactionId(), "SUCCESS", "CREDIT"));
    }
}

