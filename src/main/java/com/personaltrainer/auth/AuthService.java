package com.personaltrainer.auth;

import com.personaltrainer.accountdraft.AccountDraftRepository;
import com.personaltrainer.accountdraft.AccountDraftStatus;
import com.personaltrainer.auth.dto.LoginRequest;
import com.personaltrainer.auth.dto.LoginResponse;
import com.personaltrainer.security.AuthenticatedUser;
import com.personaltrainer.security.JwtService;
import com.personaltrainer.student.StudentRepository;
import com.personaltrainer.user.User;
import com.personaltrainer.user.UserRepository;
import com.personaltrainer.user.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AccountDraftRepository accountDraftRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        if (!userRepository.existsByEmail(request.email())) {
            throw fixDraftFailure(request.email());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos"));
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        boolean profileCompleted = user.getRole() != UserRole.STUDENT
                || studentRepository.findByUserId(user.getId())
                .map(s -> Boolean.TRUE.equals(s.getProfileCompleted()))
                .orElse(false);

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getId(), user.getName(), user.getRole(), profileCompleted);
    }

    public AuthenticatedUser currentUSer (Object principal) {
        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }
        throw new IllegalStateException("Usuário não autenticado");
    }

    private RuntimeException fixDraftFailure(String email) {
        return accountDraftRepository.findByEmailAndStatus(email, AccountDraftStatus.PENDING_PAYMENT)
                .<RuntimeException>map(d -> d.getExpiresAt().isBefore(LocalDateTime.now())
                        ? new DraftExpiredException()
                        : new PendingApprovalException())
                .orElseGet(() -> new BadCredentialsException("E-mail ou senha inválidos"));
    }
}
