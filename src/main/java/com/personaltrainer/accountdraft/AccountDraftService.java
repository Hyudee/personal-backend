package com.personaltrainer.accountdraft;

import com.personaltrainer.billing.BillingRecord;
import com.personaltrainer.billing.BillingRecordRepository;
import com.personaltrainer.billing.BillingStatus;
import com.personaltrainer.billing.payment.PaymentDayRepository;
import com.personaltrainer.billing.payment.PixKey;
import com.personaltrainer.billing.payment.PixKeyRepository;
import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.billing.plan.PlanRepository;
import com.personaltrainer.student.Student;
import com.personaltrainer.student.StudentRepository;
import com.personaltrainer.user.User;
import com.personaltrainer.user.UserRepository;
import com.personaltrainer.user.UserRole;
import com.personaltrainer.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountDraftService {

    private static final long EXPIRATION_DAYS = 30;

    private final AccountDraftRepository accountDraftRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PlanRepository planRepository;
    private final PixKeyRepository pixKeyRepository;
    private final PaymentDayRepository paymentDayRepository;
    private final BillingRecordRepository billingRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AccountDraft criarDraft(String name, String email, String rawPassword, Long planId, Integer paymentDay) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(email);
        }

        FreeEmailIfPendingDraftExpired(email);

        Plan plan = planRepository.findByIdAndActiveTrue(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + planId));

        if (!paymentDayRepository.existsByDayOfMonthAndActiveTrue(paymentDay)){
            throw new InvalidPaymentDayException (paymentDay);
        }

        PixKey pixKey = pixKeyRepository.findRandomActive().orElse(null);

        AccountDraft draft = AccountDraft.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .plan(plan)
                .paymentDay(paymentDay)
                .pixKey(pixKey)
                .status(AccountDraftStatus.PENDING_PAYMENT)
                .expiresAt(LocalDateTime.now().plusDays(EXPIRATION_DAYS))
                .build();

        return accountDraftRepository.save(draft);
    }

    @Transactional
    public User aprovar(Long draftId, Long reviewerId) {
        AccountDraft draft = getPendingDraft(draftId);
        User reviewer = getReviewer(reviewerId);

        User user = User.builder()
                .name(draft.getName())
                .email(draft.getEmail())
                .password(draft.getPassword())
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        Student student = Student.builder()
                .user(user)
                .paymentDay(draft.getPaymentDay())
                .build();
        studentRepository.save(student);

        LocalDateTime now = LocalDateTime.now();
        billingRecordRepository.save(BillingRecord.builder()
                .student(student)
                .plan(draft.getPlan())
                .dueDate(LocalDate.now())
                .paidAt(now)
                .status(BillingStatus.PAID)
                .build());

        draft.setStatus(AccountDraftStatus.APPROVED);
        draft.setPaymentConfirmedAt(now);
        draft.setReviewedAt(now);
        draft.setReviewer(reviewer);
        accountDraftRepository.save(draft);

        eventPublisher.publishEvent (new AccountApprovedEvent(user.getName(), user.getEmail()));

        return user;
    }

    @Transactional
    public void rejeitar(Long draftId, Long reviewerId, String reason) {
        AccountDraft draft = getPendingDraft(draftId);
        User reviewer = getReviewer(reviewerId);

        draft.setStatus(AccountDraftStatus.REJECTED);
        draft.setReviewedAt(LocalDateTime.now());
        draft.setReviewer(reviewer);
        draft.setRejectionReason(reason);

        accountDraftRepository.save(draft);
    }

    private void FreeEmailIfPendingDraftExpired (String email) {
        accountDraftRepository.findByEmailAndStatus (email, AccountDraftStatus.PENDING_PAYMENT)
                .ifPresent(exist -> {
                    if (exist.getExpiresAt().isAfter(LocalDateTime.now())) {
                        throw new EmailAlreadyInUseException (email);
                    }

                    exist.setStatus(AccountDraftStatus.EXPIRED);

                    accountDraftRepository.saveAndFlush(exist);
                });
    }

    private AccountDraft getPendingDraft(Long draftId) {
        AccountDraft draft = accountDraftRepository.findById(draftId)
                .orElseThrow(() -> new IllegalArgumentException("Draft não encontrado: " + draftId));

        if (draft.getStatus() != AccountDraftStatus.PENDING_PAYMENT) {
            throw new IllegalStateException(
                    "Draft não está pendente (status atual: " + draft.getStatus() + ")");
        }

        if (draft.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Draft expirado em " + draft.getExpiresAt());
        }

        return draft;
    }

    private User getReviewer(Long reviewerId) {
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Revisor não encontrado: " + reviewerId));

        if (reviewer.getRole() != UserRole.PERSONAL) {
            throw new IllegalStateException("Apenas um personal pode revisar um draft");
        }

        return reviewer;
    }
}