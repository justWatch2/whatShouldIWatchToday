package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "OTT_code")
public class OTTCode {

    @Id
    @Column(name = "code",length = 3)
    private int code;

    @Column(name = "OTT_name" ,length = 20)
    private String OTTName;

    @Builder(toBuilder = true)
    public OTTCode(int code, String OTTName) {
        this.code = code;
        this.OTTName = OTTName;
    }
}