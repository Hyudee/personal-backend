package com.personaltrainer.accountdraft;


import com.personaltrainer.accountdraft.dto.*;
import com.personaltrainer.billing.payment.PaymentLinkService;
import com.personaltrainer.security.AuthenticatedUser;
import com.personaltrainer.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @AuthenticationPrincipal AuthenticatedUser reviewer){

        User user = accountDraftService.aprovar(id, reviewer.getId());
        return ResponseEntity.ok (UserResponse.from(user));
    }

    @PostMapping ("/{id}/rejeitar")
    public ResponseEntity <Void> rejeitar(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser reviewer,
            @Valid @RequestBody (required = false) ReviewDraftRequest request) {
        String reason = request !=null ? request.reason() : null;
        accountDraftService.rejeitar(id, reviewer.getId(), reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<AccountDraftSummaryResponse> listar(
            @RequestParam (defaultValue = "PENDING_PAYMENT") AccountDraftStatus status) {
        return accountDraftService.listar(status).stream()
                .map(AccountDraftSummaryResponse::from)
                .toList();
    }
}
