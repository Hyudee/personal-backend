package com.personaltrainer.billing.payment;


import com.personaltrainer.accountdraft.AccountDraft;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor

public class PaymentLinkService {

    private final PaymentProperties properties;

    /** Pagar pelo wpp vai retornar null se n configurar o numero */
    public String whatsappPayUrl (AccountDraft draft){
        return buildUrl (properties.payMessageTemplate(), draft);
    }

    /** Pagar pelo pix só existe se o draft recebeu uma chave pix */
    public String whatsappPixPaidUrl (AccountDraft draft){
        if (draft.getPixKey() == null) {
            return null;
        }
        return buildUrl (properties.pixPaidMessageTemplate(), draft);
    }

    private String buildUrl (String template, AccountDraft draft){
        String number = properties.whatsappNumber();
        if (number == null || number.isBlank() || template == null){
            return null;
        }

        String message = template
                .replace("{nome}", draft.getName())
                .replace("{email}", draft.getEmail())
                .replace("{plano}", draft.getPlan() != null ? draft.getPlan().getName() : "")
                .replace("{valor}", draft.getPlan() != null
                        ? NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(draft.getPlan().getPrice())
                        : "")
                .replace("{chavePix}", draft.getPixKey() != null ? draft.getPixKey().getKeyValue() : "")
                .replace("{draftId}", String.valueOf(draft.getId()));

        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8).replace("+", "%20");
        return "https://wa.me/" + number.replaceAll("\\D", "") + "?text=" + encoded;
    }
}
