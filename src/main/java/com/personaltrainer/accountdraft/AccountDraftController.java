package com.personaltrainer.accountdraft;


import com.personaltrainer.accountdraft.dto.AccountDraftResponse;
import com.personaltrainer.accountdraft.dto.CreateAccountDraftRequest;
import com.personaltrainer.accountdraft.dto.ReviewDraftRequest;
import com.personaltrainer.accountdraft.dto.UserResponse;
import com.personaltrainer.billing.payment.PaymentLinkService;
import com.personaltrainer.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/account-drafts")
@RequiredArgsConstructor
public class AccountDraftController {

    private final AccountDraftService accountDraftService;
    private final PaymentLinkService paymentLinkService;

    @PostMapping
    public ResponseEntity<AccountDraftResponse> criar(@Valid @RequestBody CreateAccountDraftRequest request){
        AccountDraft draft = accountDraftService.criarDraft(
                request.name(), request.email(), request.password(), request.planId(), request.paymentDay());

        AccountDraftResponse body = AccountDraftResponse.from(
                draft,
                paymentLinkService.whatsappPayUrl(draft),
                paymentLinkService.whatsappPixPaidUrl(draft));

        return ResponseEntity.status(HttpStatus.CREATED).body(body);

    }

    @PostMapping("/{id}/aprovar")
    public ResponseEntity<UserResponse> aprovar(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDraftRequest request){

        User user = accountDraftService.aprovar(id, request.reviewerId());
        return ResponseEntity.ok (UserResponse.from(user));
    }

    @PostMapping ("/{id}/rejeitar")
    public ResponseEntity <Void> rejeitar(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDraftRequest request){
        accountDraftService.rejeitar(id, request.reviewerId(), request.reason());
        return ResponseEntity.noContent().build();
    }
}
