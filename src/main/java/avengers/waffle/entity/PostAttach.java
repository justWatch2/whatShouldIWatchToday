package avengers.waffle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Table(name="post_attach")
@DynamicUpdate
@Builder
@AllArgsConstructor
public class PostAttach implements Serializable {
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "post_no", referencedColumnName = "no", nullable = false)
    Post post;

    @Column(name = "file_url", nullable = false, length = 70)
    private String fileUrl;


}
