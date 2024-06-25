package site.toeicdoit.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import site.toeicdoit.user.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(exclude = {"id"})
public class TokenModel extends BaseEntity {

    @Id
    @Column(name ="id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime expired;

}