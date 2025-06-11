package avengers.waffle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "OTT_code")
@Builder
@AllArgsConstructor
public class OTTCode  implements Serializable {

    @Id
    @Column(name = "code",length = 3)
    private int code;

    @Column(name = "OTT_name" ,length = 20)
    private String OTTName;

}