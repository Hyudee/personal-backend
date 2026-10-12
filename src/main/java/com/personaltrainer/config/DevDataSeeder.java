package com.personaltrainer.config;

import com.personaltrainer.billing.payment.PixKey;
import com.personaltrainer.billing.payment.PixKeyRepository;
import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.billing.plan.PlanPeriodicity;
import com.personaltrainer.billing.plan.PlanRepository;
import com.personaltrainer.user.User;
import com.personaltrainer.user.UserRepository;
import com.personaltrainer.user.UserRole;
import com.personaltrainer.user.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class DevDataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PixKeyRepository pixKeyRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.dev-seed.personal-email:}")
    private String personalEmail;

    @Value("${app.dev-seed.personal-password:}")
    private String personalPassword;

    @Value("${app.dev-seed.pix-key:}")
    private String pixKey;

    @Override
    public void run(ApplicationArguments args) {
        seedPersonal();
        seedPlans();
        seedPixKey();
    }

    private void seedPersonal() {
        if (personalEmail.isBlank() || personalPassword.isBlank()) {
            log.warn("[seed] app.dev-seed.personal-email/password não definidos: personal não criado");
            return;
        }

        if (userRepository.existsByEmail(personalEmail)) {
            return;
        }

        userRepository.save(User.builder()
                .name("Personal Teste")
                .email(personalEmail)
                .password(passwordEncoder.encode(personalPassword))
                .role(UserRole.PERSONAL)
                .status(UserStatus.ACTIVE)
                .build());
        log.info("[seed] Personal criado: {}", personalEmail);
    }

    private void seedPlans() {
        Set<String> existentes = planRepository.findAll().stream()
                .map(Plan::getName)
                .collect(Collectors.toSet());

        List<Plan> novos = List.of(
                        novoPlano("Básico", "149.90"),
                        novoPlano("Intermediário", "249.90"),
                        novoPlano("Premium", "399.90"))
                .stream()
                .filter(p -> !existentes.contains(p.getName()))
                .toList();

        if (!novos.isEmpty()) {
            planRepository.saveAll(novos);
            log.info("[seed] {} plano(s) criado(s)", novos.size());
        }
    }

    private Plan novoPlano(String nome, String preco) {
        return Plan.builder()
                .name(nome)
                .price(new BigDecimal(preco))
                .periodicity(PlanPeriodicity.MONTHLY)
                .build();
    }

    private void seedPixKey() {
        if (pixKey.isBlank() || pixKeyRepository.count() > 0) {
            return;
        }
        pixKeyRepository.save(PixKey.builder().keyValue(pixKey).build());
        log.info("[seed] Chave Pix de teste criada");
    }
}
