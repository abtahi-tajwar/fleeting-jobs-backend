package com.fleetingtrails.fleetingjobsbackend.parser.entity;

import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "parser_templates")
public class ParserTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="parser_config", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private ParserTemplateType config;
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="company_id", unique = true)
    private CompanyEntity company;
}
