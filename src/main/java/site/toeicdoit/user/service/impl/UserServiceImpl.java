package site.toeicdoit.user.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.toeicdoit.user.domain.dto.UserDto;
import site.toeicdoit.user.domain.model.mysql.QRoleModel;
import site.toeicdoit.user.domain.model.mysql.QUserModel;
import site.toeicdoit.user.domain.model.mysql.RoleModel;
import site.toeicdoit.user.domain.model.mysql.UserModel;
import site.toeicdoit.user.domain.vo.MessageStatus;
import site.toeicdoit.user.domain.vo.Messenger;
import site.toeicdoit.user.domain.vo.Registration;
import site.toeicdoit.user.domain.vo.Role;
import site.toeicdoit.user.repository.mysql.RoleRepository;
import site.toeicdoit.user.service.UserService;
import site.toeicdoit.user.repository.mysql.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final QUserModel user = QUserModel.userModel;
    private final QRoleModel role = QRoleModel.roleModel;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Messenger save(UserDto dto) {
        log.info(">>> user save Impl 진입: {} ", dto);
        dto.setRegistration(Registration.LOCAL.name());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        var result = userRepository.save(dtoToEntity(dto));
        log.info(">>> user save result : {}", result);
        var result1 = roleRepository.save(RoleModel.builder().role(0).userId(result).build());
        log.info(">>> user save result : {}", result1);

        return Messenger.builder()
                .message(MessageStatus.SUCCESS.name())
                .build();
    }

    @Transactional
    @Override
    public Messenger localLogin(UserDto dto) {
        log.info(">>> localLogin Impl 진입: {} ", dto);
        var loginUser = userRepository.findByEmail(dto.getEmail()).get();
        log.info(">>> loginUser 결과 : {}", loginUser);

        return passwordEncoder.matches(dto.getPassword(), loginUser.getPassword())  ?
                Messenger.builder().message(MessageStatus.SUCCESS.name())
                        .data(loginUser.getRoleModels().stream()
                        .map(RoleModel::getRole)
                        .peek(System.out::println)
                        .map(Role::getRole)
                        .peek(System.out::println)
                        .findFirst().orElse(null)).build() :
                Messenger.builder().message(MessageStatus.FAILURE.name()).build();
    }

    @Override
    public Messenger existsByEmail(String email) {
        log.info(">>> existsByEmail Impl 진입: {}", email);
        return userRepository.existsByEmail(email) ?
                Messenger.builder().message(MessageStatus.SUCCESS.name()).build() :
                Messenger.builder().message(MessageStatus.FAILURE.name()).build();
    }

    @Transactional
    @Override
    public Messenger deleteById(Long id) {
        log.info(">>> user deleteById Impl 진입: {} ", id);

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return Messenger.builder().message(MessageStatus.SUCCESS.name()).build();
        } else {
            return Messenger.builder().message(MessageStatus.FAILURE.name()).build();
        }
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(this::entityToDto).toList();
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        log.info("user findById 결과 : {}", userRepository.findById(id).map(this::entityToDto));
        // 결과 보고 없을 경우도 코딩 필요
        return userRepository.findById(id).map(this::entityToDto);
    }

    @Override
    public Messenger count() {
        return Messenger.builder()
                .message(userRepository.count() + "")
                .build();
    }

    @Override
    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional
    @Override
    public Messenger modify(UserDto dto) {
        log.info(">>> user modify Impl 진입: {}", dto);

        UserModel ent = dtoToEntity(dto);

        Optional<UserModel> result = userRepository.findById(ent.getId()).stream()
                .map(userRepository::save).findFirst();
        log.info(">>> user modify findFirst() 결과 : {}", result);

        List<UserModel> result1 = userRepository.findById(ent.getId()).stream()
                .map(userRepository::save).toList();
        log.info(">>> user modify toList() 결과 : {}", result1);

        return Messenger.builder()
                .message(MessageStatus.SUCCESS.name())
                .build();
    }

}
